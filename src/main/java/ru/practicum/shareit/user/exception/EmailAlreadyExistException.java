package ru.practicum.shareit.user.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.service.exception.ShareItException;

public class EmailAlreadyExistException extends ShareItException {

    public EmailAlreadyExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}
