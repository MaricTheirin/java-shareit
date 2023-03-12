package ru.practicum.shareit.service;

import java.time.LocalDateTime;

public class ExceptionMessage {

    private final String message;
    private final LocalDateTime localDateTime;
    private final String path;

    public ExceptionMessage(Throwable throwable, String path) {
        message = throwable.getMessage();
        localDateTime = LocalDateTime.now();
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public String getPath() {
        return path;
    }

}
