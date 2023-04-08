package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.service.exception.ShareItException;

public class BookingUnsupportedStateException extends ShareItException {

    public BookingUnsupportedStateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
