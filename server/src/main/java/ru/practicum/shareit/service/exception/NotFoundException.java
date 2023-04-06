package ru.practicum.shareit.service.exception;

import org.springframework.http.HttpStatus;

public abstract class NotFoundException extends ShareItException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
