package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto read(Long userId, Long itemId);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto delete(Long userId, Long itemId);

    List<ItemDto> findAvailable(String searchQuery);

    List<ItemDto> findAll(Long userId);

}
