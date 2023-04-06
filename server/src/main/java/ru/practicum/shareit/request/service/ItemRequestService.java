package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestResponseDto create(Long userId, ItemRequestDto itemRequestDto);

    ItemRequestResponseDto read(Long userId, Long itemRequestId);

    List<ItemRequestResponseDto> readUserRequests(Long userId);

    List<ItemRequestResponseDto> readAllUsersRequests(Long userId, int from, int size);

}
