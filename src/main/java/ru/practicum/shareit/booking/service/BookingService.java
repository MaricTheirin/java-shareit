package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;
import java.util.List;

public interface BookingService {

    BookingResponseDto create(Long userId, BookingDto bookingDto);

    BookingResponseDto read(Long userId, Long bookingId);

    BookingResponseDto review(Long userId, Long bookingId, Boolean approved);

    List<BookingResponseDto> findOwnBookings(Long userId, BookingState state, long from, long size);

    List<BookingResponseDto> findOwnItemsBookings(Long userId, BookingState state, long from, long size);

}
