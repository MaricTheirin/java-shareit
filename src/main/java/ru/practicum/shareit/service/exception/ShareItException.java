package ru.practicum.shareit.service.exception;

import org.springframework.http.HttpStatus;

public abstract class ShareItException extends RuntimeException {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public ShareItException(String message) {
        super(message);
    }

    public ShareItException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
