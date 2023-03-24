package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemRepository {

    Item save(Item item);

    Item get(Long itemId);

    Item update(Item item);

    Item delete(Long userId, Long itemId);

    List<Item> findAll(Long userId);

    List<Item> findAvailable(String searchQuery);

    boolean isExist(Long itemId);

    boolean isExistAndBelongsToUser(Long userId, Long itemId);

}
