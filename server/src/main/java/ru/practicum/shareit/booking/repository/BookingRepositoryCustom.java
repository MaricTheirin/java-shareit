package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import java.util.List;

public interface BookingRepositoryCustom {

    List<Booking> findAllByUserItemsAndFilterByState(Long userId, BookingState state, int from, int size);

    List<Booking> findAllByUserBookingsAndFilterByStateOrderByIdAsc(Long userId, BookingState state, int from, int size);

}
