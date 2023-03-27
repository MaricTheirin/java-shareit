package ru.practicum.shareit.booking.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Component
public class BookingDtoMapper {

    private static final String OBJECT_MAPPED_MESSAGE = "Выполнено преобразование объекта из {} в {}";
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final ItemDtoMapper itemDtoMapper;

    @Autowired
    public BookingDtoMapper(
            ItemRepository itemRepository,
            UserRepository userRepository,
            UserDtoMapper userDtoMapper,
            ItemDtoMapper itemDtoMapper
    ) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.itemDtoMapper = itemDtoMapper;
    }

    public BookingResultDto mapBookingToResultDto(Booking booking) {
        BookingResultDto resultDto = new BookingResultDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                userDtoMapper.mapUserToDto(userRepository.getReferenceById(booking.getBookerId())),
                itemDtoMapper.mapItemToDto(itemRepository.getReferenceById(booking.getItemId()))
        );
        log.trace(OBJECT_MAPPED_MESSAGE, booking, resultDto);
        return resultDto;
    }

    public BookingDto mapBookingToBookingDto(Booking booking) {
        BookingDto resultDto = new BookingDto(
                booking.getId(),
                booking.getItemId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBookerId()
        );
        log.trace(OBJECT_MAPPED_MESSAGE, booking, resultDto);
        return resultDto;
    }

    public Booking mapDtoToBooking(Long userId, BookingDto bookingDto) {
        Booking mappedBooking = new Booking(
                bookingDto.getId(),
                bookingDto.getItemId(),
                userId,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                BookingStatus.WAITING
        );
        log.trace(OBJECT_MAPPED_MESSAGE, bookingDto, mappedBooking);
        return mappedBooking;
    }

}
