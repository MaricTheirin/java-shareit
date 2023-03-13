package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemRepository {

    Item saveItem(Item item);

    Item getItem(Long itemId);

    Item updateItem(Item item);

    Item deleteItem(Long userId, Long itemId);

    List<Item> getAllItems(Long userId);

    List<Item> getAvailableItems(String searchQuery);

    boolean isExist(Long itemId);

    boolean isExistAndBelongsToUser(Long userId, Long itemId);

}
