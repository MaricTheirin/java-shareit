package ru.practicum.shareit.service.exception;

import org.springframework.http.HttpStatus;

public abstract class CreationException extends ShareItException {

    public CreationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
