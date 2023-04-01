package ru.practicum.shareit.request.exception;

import ru.practicum.shareit.service.exception.NotFoundException;

public class ItemRequestNotFoundException extends NotFoundException {

    public ItemRequestNotFoundException(String message) {
        super(message);
    }

    public ItemRequestNotFoundException() {
        super("Запрос на бронь не найден");
    }
}
