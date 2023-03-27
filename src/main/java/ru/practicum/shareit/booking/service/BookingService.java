package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import java.util.List;

public interface BookingService {

    BookingResultDto create(Long userId, BookingDto bookingDto);

    BookingResultDto read(Long userId, Long bookingId);

    BookingResultDto review(Long userId, Long bookingId, Boolean approved);

    List<BookingResultDto> findOwnBookings(Long userId, String state);

    List<BookingResultDto> findOwnItemsBookings(Long userId, String state);

}
