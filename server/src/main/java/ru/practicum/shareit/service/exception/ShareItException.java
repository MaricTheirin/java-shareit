package ru.practicum.shareit.service.exception;

import org.springframework.http.HttpStatus;

public abstract class ShareItException extends RuntimeException {

    private final HttpStatus status;

    public ShareItException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
