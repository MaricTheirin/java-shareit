package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestResponseDto create(Long userId, ItemRequestDto itemRequestDto) {
        log.debug("Пользователь с ID = {} создал запрос на предмет {}", userId, itemRequestDto);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        ItemRequest savedRequest =
                itemRequestRepository.save(ItemRequestDtoMapper.mapDtoToItemRequest(user, itemRequestDto));
        log.trace("Запрос сохранён: {}", savedRequest);
        return ItemRequestDtoMapper.mapItemRequestToResponseDto(savedRequest, Collections.emptyList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestResponseDto read(Long userId, Long itemRequestId) {
        log.debug("Пользователь с ID = {} запросил данные о запросе на предмет {}", userId, itemRequestId);
        checkIfUserExist(userId);

        ItemRequest savedRequest = itemRequestRepository
                .findById(itemRequestId)
                .orElseThrow(ItemRequestNotFoundException::new);
        log.trace("Получен запрос: {}", savedRequest);
        return addItemsAndMapToItemResponseDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> readUserRequests(Long userId) {
        log.debug("Пользователь с ID = {} запросил список своих запросов на предметы", userId);
        checkIfUserExist(userId);

        List<ItemRequest> foundRequests = itemRequestRepository.findAllByUserIdOrderByCreatedDesc(userId);

        log.trace("Получен список запросов: {}", foundRequests);
        return addItemsAndMapToItemResponseDtoList(foundRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> readAllUsersRequests(Long userId, int from, int size) {
        log.debug("Пользователь с ID = {} запросил список всех запросов на предметы с разбивкой [{},{}]",
                userId, from, size
        );
        checkIfUserExist(userId);

        List<ItemRequest> foundRequests = itemRequestRepository
                .findAllByUserIdNotOrderByCreatedDesc(userId, PageRequest.of(from, size));

        log.trace("Получен список запросов: {}", foundRequests);

        return addItemsAndMapToItemResponseDtoList(foundRequests);
    }

    private ItemRequestResponseDto addItemsAndMapToItemResponseDto(ItemRequest request) {
        return ItemRequestDtoMapper.mapItemRequestToResponseDto(request, findItemsForItemRequest(request.getId()));
    }

    private List<ItemRequestResponseDto> addItemsAndMapToItemResponseDtoList(List<ItemRequest> requests) {
        Map<Long, List<ItemShortResponseDto>> requestsItems = findItemsForItemRequests(requests);
        return requests.stream()
                .map(request -> ItemRequestDtoMapper.mapItemRequestToResponseDto(
                        request,
                        requestsItems.getOrDefault(request.getId(), Collections.emptyList())
                )).collect(Collectors.toList());
    }

    private List<ItemShortResponseDto> findItemsForItemRequest(Long itemRequestId) {
        return itemRepository
                .findAllItemsByItemRequestIdIn(Set.of(itemRequestId))
                .stream()
                .map(ItemDtoMapper::mapItemToShortResponseDto)
                .collect(Collectors.toList());
    }

    private Map<Long, List<ItemShortResponseDto>> findItemsForItemRequests(List<ItemRequest> requests) {
        return findItemsForItemRequests(requests.stream().map(ItemRequest::getId).collect(Collectors.toSet()));
    }

    private Map<Long, List<ItemShortResponseDto>> findItemsForItemRequests(Set<Long> itemRequestsIds) {
        return itemRepository
                .findAllItemsByItemRequestIdIn(itemRequestsIds)
                .stream()
                .map(ItemDtoMapper::mapItemToShortResponseDto)
                .collect(Collectors.groupingBy(ItemShortResponseDto::getRequestId, Collectors.toList()));
    }

    private void checkIfUserExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }

}
