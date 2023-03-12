package ru.practicum.shareit.user.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.service.exception.ShareItException;

public class UserAlreadyExistException extends ShareItException {

    public UserAlreadyExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
