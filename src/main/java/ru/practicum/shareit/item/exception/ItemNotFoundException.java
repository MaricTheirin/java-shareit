package ru.practicum.shareit.item.exception;

import ru.practicum.shareit.service.exception.NotFoundException;

public class ItemNotFoundException extends NotFoundException {

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException() {
        super("Предмет не найден");
    }

}
