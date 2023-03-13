package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto readItem(Long userId, Long itemId);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemDto deleteItem(Long userId, Long itemId);

    List<ItemDto> findAvailableItems(String searchQuery);

    List<ItemDto> readAllItems(Long userId);

}
