package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.service.exception.AccessException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingDtoMapper bookingDtoMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponseDto create(Long userId, BookingDto bookingDto) {
        log.debug("Пользователь с id = {} пытается взять в аренду: {}", userId, bookingDto);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(ItemNotFoundException::new);
        checkBeforeSave(userId, bookingDto);
        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Booking savedBooking = bookingRepository.saveAndFlush(
                BookingDtoMapper.mapDtoToBooking(bookingDto, item, booker)
        );
        log.trace("Сохранённый предмет: {}", savedBooking);
        return BookingDtoMapper.mapBookingToResultDto(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto read(Long userId, Long bookingId) {
        log.debug("Пользователь с id = {} запросил информацию об аренде с id = {}", userId, bookingId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
        log.trace("По bookingId = {} получен {}", bookingId, booking);
        checkBeforeGet(userId, booking);
        return BookingDtoMapper.mapBookingToResultDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto review(Long userId, Long bookingId, Boolean approved) {
        log.debug(
                "Добавление ответа на бронирование от пользователя с id = {} для предмета с id = {}. Результат = {}",
                userId,
                bookingId,
                approved
        );
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Запроса на бронирование с id = " + bookingId + " не существует"));

        checkBeforeReview(userId, booking);

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingDtoMapper.mapBookingToResultDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> findOwnBookings(Long userId, BookingState bookingState, long from, long size) {
        log.debug(
                "Пользователь с id = {} запросил информацию о всех своих арендах в состоянии {} с лимитами [{}, {}]",
                userId, bookingState, from, size
        );
        checkIfUserExists(userId);
        checkPagingParameters(from, size);

        List<Booking> foundBookings =
                bookingRepository.findAllByUserBookingsAndFilterByStateOrderByIdAsc(userId, bookingState, from, size);
        log.trace("Полученный результат: {}", foundBookings);
        return foundBookings.stream().map(BookingDtoMapper::mapBookingToResultDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> findOwnItemsBookings(Long userId, BookingState bookingState, long from, long size) {
        log.debug(
                "Пользователь с id = {} запросил информацию об арендах своих вещей в состоянии {} с лимитами [{}, {}]",
                userId, bookingState, from, size
        );
        checkIfUserExists(userId);
        checkPagingParameters(from, size);

        List<Booking> foundBookings = bookingRepository.findAllByUserItemsAndFilterByState(userId, bookingState, from, size);
        log.trace("Полученный результат: {}", foundBookings);
        return foundBookings.stream().map(BookingDtoMapper::mapBookingToResultDto).collect(Collectors.toList());
    }

    private void checkBeforeGet(Long userId, Booking booking) {
        Item item = booking.getItem();
        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(item.getOwner().getId())) {
            log.warn("Пользователь {} не имеет доступа к просмотру бронирования {}", userId, booking);
            throw new AccessException("Доступ запрещён");
        }
    }

    private void checkBeforeSave(Long userId, BookingDto bookingDto) {
        if (checkIfItemIsOwnedByUser(bookingDto.getItemId(), userId)) {
            log.warn("Пользователь {} пытается забронировать свою же вещь с id {}", userId, bookingDto.getItemId());
            throw new BookingNotFoundException("Нельзя забронировать собственную вещь");
        }
        if (!isItemAvailable(bookingDto.getItemId())) {
            log.warn("Пользователь {} пытается забронировать недоступную вещь с id {}", userId, bookingDto.getItemId());
            throw new ItemNotAvailableException("В настоящее время аренда предмета запрещена");
        }
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
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
        if (booking.getItem().getOwner().getId() != userId) {
            log.warn("Пользователь {} попытался обновить бронь чужого предмета {}", userId, booking);
            throw new BookingNotFoundException("Изменить статус бронирования может только владелец предмета");
        }
    }

    private void checkIfUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь не существует");
        }
    }

    private boolean checkIfItemIsOwnedByUser(long itemId, long userId) {
        return itemRepository.existsItemByIdAndOwnerId(itemId, userId);
    }

    private void checkPagingParameters(long page, long size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }
        if (page < 0) {
            throw new IllegalArgumentException("Page number must not be less than zero");
        }
    }

    private boolean isItemAvailable(Long itemId) {
        return itemRepository.existsItemByIdAndAvailableIsTrue(itemId);
    }

}
