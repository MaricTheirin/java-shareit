package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.service.exception.NotFoundException;

public class BookingNotFoundException extends NotFoundException {

    public BookingNotFoundException(String message) {
        super(message);
    }

}
