package ru.practicum.shareit.service.exception;

import org.springframework.http.HttpStatus;

public class AccessException extends ShareItException {
    public AccessException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
