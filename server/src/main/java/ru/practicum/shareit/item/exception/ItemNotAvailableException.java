package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.service.exception.ShareItException;

public class ItemNotAvailableException extends ShareItException {

    public ItemNotAvailableException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
