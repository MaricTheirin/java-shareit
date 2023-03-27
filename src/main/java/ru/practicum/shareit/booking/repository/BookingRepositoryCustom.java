package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import java.util.List;

public interface BookingRepositoryCustom {

    List<Booking> findAllByUserItemsAndFilterByState(Long userId, BookingState state);

    List<Booking> findAllByUserBookingsAndFilterByState(Long userId, BookingState state);

}