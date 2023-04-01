package ru.practicum.shareit.request.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.service.exception.ShareItException;

public class ItemRequestNotFoundException extends ShareItException {
    public ItemRequestNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
