package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.booking.exception.BookingException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingUnsupportedStateException;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.exception.ItemNotAvailableException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.service.exception.AccessException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final BookingDtoMapper bookingDtoMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(
            BookingRepository bookingRepository,
            BookingDtoMapper bookingDtoMapper,
            ItemRepository itemRepository,
            UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingDtoMapper = bookingDtoMapper;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookingResultDto create(Long userId, BookingDto bookingDto) {
        log.debug("Пользователь с id = {} пытается взять в аренду: {}", userId, bookingDto);
        checkBeforeSave(userId, bookingDto);

        Booking savedBooking = bookingRepository.saveAndFlush(bookingDtoMapper.mapDtoToBooking(userId, bookingDto));
        log.trace("Сохранённый предмет: {}", savedBooking);
        return bookingDtoMapper.mapBookingToResultDto(savedBooking);
    }

    @Override
    public BookingResultDto read(Long userId, Long bookingId) {
        log.debug("Пользователь с id = {} запросил информацию об аренде с id = {}", userId, bookingId);

        checkIfBookingExists(bookingId);
        Booking booking = bookingRepository.getReferenceById(bookingId);
        log.trace("По bookingId = {} получен {}", bookingId, booking);
        checkBeforeGet(userId, booking);
        return bookingDtoMapper.mapBookingToResultDto(booking);
    }

    @Override
    public BookingResultDto review(Long userId, Long bookingId, Boolean approved) {
        log.debug(
                "Добавление ответа на бронирование от пользователя с id = {} для предмета с id = {}. Результат = {}",
                userId,
                bookingId,
                approved
        );
        if (!bookingRepository.existsById(bookingId)) {
            log.warn("Запроса на бронирование с id = {} не существует", bookingId);
            throw new BookingNotFoundException("Запроса на бронирование с id = " + bookingId + " не существует");
        }
        Booking booking = bookingRepository.getReferenceById(bookingId);
        checkBeforeReview(userId, booking);

        booking.setStatus(approved ? BookingStatus.APPROVED: BookingStatus.REJECTED);
        bookingRepository.saveAndFlush(booking);
        return bookingDtoMapper.mapBookingToResultDto(booking);
    }

    @Override
    public List<BookingResultDto> findOwnBookings(Long userId, String state) {
        log.debug("Пользователь с id = {} запросил информацию о всех своих арендах в состоянии {}", userId, state);
        checkIfUserExists(userId);
        BookingState bookingState = findStateForUserString(state);

        List<Booking> foundBookings = bookingRepository.findAllByUserBookingsAndFilterByState(userId, bookingState);
        log.trace("Полученный результат: {}", foundBookings);
        return foundBookings.stream().map(bookingDtoMapper::mapBookingToResultDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingResultDto> findOwnItemsBookings(Long userId, String state) {
        log.debug("Пользователь с id = {} запросил информацию об арендах своих вещей в состоянии {}", userId, state);
        BookingState bookingState = findStateForUserString(state);

        checkIfUserExists(userId);

        List<Booking> foundBookings = bookingRepository.findAllByUserItemsAndFilterByState(userId, bookingState);
        log.trace("Полученный результат: {}", foundBookings);
        return foundBookings.stream().map(bookingDtoMapper::mapBookingToResultDto).collect(Collectors.toList());
    }

    private void checkBeforeGet(Long userId, Booking booking) {
        checkIfUserExists(userId);

        Item item = itemRepository.getReferenceById(booking.getItemId());
        if (!userId.equals(booking.getBookerId()) && !userId.equals(item.getOwnerId())) {
            log.warn("Пользователь {} не имеет доступа к просмотру бронирования {}", userId, booking);
            throw new AccessException("Доступ запрещён");
        }
    }

    private void checkBeforeSave(Long userId, BookingDto bookingDto) {
        checkIfUserExists(userId);
        checkIfItemExists(bookingDto.getItemId());

        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        if (userId == item.getOwnerId()) {
            log.warn("Пользователь {} пытается забронировать свою же вещь с id {}", userId, bookingDto.getItemId());
            throw new BookingNotFoundException("Нельзя забронировать собственную вещь");
        }
        if (!isItemAvailable(bookingDto.getItemId())) {
            log.warn("Пользователь {} пытается забронировать недоступную вещь с id {}", userId, bookingDto.getItemId());
            throw new ItemNotAvailableException("В настоящее время аренда предмета запрещена");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            log.warn("Дата окончания бронирования не может быть раньше даты начала");
            throw new BookingException("Дата окончания бронирования не может быть раньше даты начала");
        }
    }

    private void checkBeforeReview(Long userId, Booking booking) {
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            log.warn("Пользователь {} пытается изменить окончательный статус бронирования {}", userId, booking);
            throw new BookingException("Нельзя изменить окончательный статус");
        }
        checkIfUserExists(userId);
        checkIfItemExists(booking.getItemId());
        Item item = itemRepository.getReferenceById(booking.getItemId());
        if (item.getOwnerId() != userId) {
            log.warn("Пользователь {} попытался обновить бронь чужого предмета {}", userId, booking);
            throw new BookingNotFoundException("Изменить статус бронирования может только владелец предмета");
        }
    }

    private void checkIfUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь не существует");
        }
    }

    private void checkIfItemExists(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ItemNotFoundException("Запрошенный предмет не существует");
        }
    }

    private void checkIfBookingExists(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new BookingNotFoundException("Запрошенная бронь не существует");
        }
    }

    private BookingState findStateForUserString(String stringState) {
        try {
            return BookingState.valueOf(stringState.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BookingUnsupportedStateException("Unknown state: " + stringState);
        }
    }

    private boolean isItemAvailable(Long itemId) {
        return itemRepository.existsItemByIdAndAvailableIsTrue(itemId);
    }

}