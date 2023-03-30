package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto create(Long userId, ItemDto itemDto);

    ItemResponseDto read(Long userId, Long itemId);

    ItemResponseDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemResponseDto delete(Long userId, Long itemId);

    List<ItemResponseDto> findAvailableItemsBySearchQuery(String searchQuery);

    List<ItemResponseDto> findAll(Long userId);

    CommentResponseDto createComment(Long userId, Long itemId, CommentDto commentDto);

}
