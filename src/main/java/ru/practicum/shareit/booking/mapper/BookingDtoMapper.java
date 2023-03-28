package ru.practicum.shareit.booking.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Component
public class BookingDtoMapper {

    private static final String OBJECT_MAPPED_MESSAGE = "Выполнено преобразование объекта из {} в {}";
    private final UserDtoMapper userDtoMapper;
    private final ItemDtoMapper itemDtoMapper;

    @Autowired
    public BookingDtoMapper(UserDtoMapper userDtoMapper, ItemDtoMapper itemDtoMapper) {
        this.userDtoMapper = userDtoMapper;
        this.itemDtoMapper = itemDtoMapper;
    }

    public BookingResponseDto mapBookingToResultDto(Booking booking) {
        BookingResponseDto resultDto = new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                userDtoMapper.mapUserToDto(booking.getBooker()),
                itemDtoMapper.mapItemToDto(booking.getItem())
        );
        log.trace(OBJECT_MAPPED_MESSAGE, booking, resultDto);
        return resultDto;
    }

    public Booking mapDtoToBooking(BookingDto bookingDto, Item item, User booker) {
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

}
