package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto readItem(Long userId, Long itemId) {
        return null;
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto deleteItem(Long userId, Long itemId) {
        return null;
    }

    @Override
    public ItemDto findAvailableItems(String text) {
        return null;
    }

}
