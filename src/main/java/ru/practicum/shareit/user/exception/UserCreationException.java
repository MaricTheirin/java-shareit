package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.service.exception.CreationException;

public class UserCreationException extends CreationException {

    public UserCreationException(String message) {
        super(message);
    }

}
