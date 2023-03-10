package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {

    Item saveItem(Item item);

    Item getItem(Long userId, Long itemId);

    Item updateItem(Item item);

    Item deleteItem(Long userId, Long itemId);

}
