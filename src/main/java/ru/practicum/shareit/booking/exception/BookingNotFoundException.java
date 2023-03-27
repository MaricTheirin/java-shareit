package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.service.exception.ShareItException;

public class BookingNotFoundException extends ShareItException {
    public BookingNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
