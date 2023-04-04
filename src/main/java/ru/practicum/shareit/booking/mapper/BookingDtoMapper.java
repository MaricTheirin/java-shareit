package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingDtoMapper {

    private static final String OBJECT_MAPPED_MESSAGE = "Выполнено преобразование объекта из {} в {}";

    public static BookingResponseDto mapBookingToResponseDto(Booking booking) {
        BookingResponseDto resultDto = new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                UserDtoMapper.mapUserToResponseDto(booking.getBooker()),
                ItemDtoMapper.mapItemToResponseDto(booking.getItem())
        );
        log.trace(OBJECT_MAPPED_MESSAGE, booking, resultDto);
        return resultDto;
    }

    public static Booking mapDtoToBooking(BookingDto bookingDto, Item item, User booker) {
        Booking mappedBooking = new Booking(
                bookingDto.getId(),
                item,
                booker,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                BookingStatus.WAITING
        );
        log.trace(OBJECT_MAPPED_MESSAGE, bookingDto, mappedBooking);
        return mappedBooking;
    }

    public static BookingShortResponseDto mapBookingShortToDto(BookingShort bookingShort) {
        BookingShortResponseDto shortResponseDto =  new BookingShortResponseDto(
                bookingShort.getId(), bookingShort.getBooker().getId(), bookingShort.getStart(), bookingShort.getEnd()
        );
        log.trace(OBJECT_MAPPED_MESSAGE, bookingShort, shortResponseDto);
        return shortResponseDto;
    }

}
