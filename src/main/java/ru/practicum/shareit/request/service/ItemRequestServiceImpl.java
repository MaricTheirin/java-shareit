package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService{

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestDtoMapper itemRequestDtoMapper;

    @Override
    public ItemRequestResponseDto create(Long userId, ItemRequestDto itemRequestDto) {
        log.debug("Пользователь с ID = {} создал запрос на предмет {}", userId, itemRequestDto);
        User user = userRepository.getReferenceById(userId);

        ItemRequest savedRequest =
                itemRequestRepository.save(itemRequestDtoMapper.mapDtoToItemRequest(user, itemRequestDto));
        log.trace("Запрос сохранён: {}", savedRequest);
        return itemRequestDtoMapper.mapItemRequestToResponseDto(savedRequest);
    }

    @Override
    public ItemRequestResponseDto read(Long userId, Long itemRequestId) {
        log.debug("Пользователь с ID = {} запросил данные о запросе на предмет {}", userId, itemRequestId);
        checkIfRequestExist(itemRequestId);
        checkIfUserExist(userId);

        ItemRequest savedRequest = itemRequestRepository.getReferenceById(itemRequestId);
        log.trace("Получен запрос: {}", savedRequest);
        return itemRequestDtoMapper.mapItemRequestToResponseDto(savedRequest);
    }

    @Override
    public List<ItemRequestResponseDto> readUserRequests(Long userId) {
        log.debug("Пользователь с ID = {} запросил список своих запросов на предметы", userId);
        checkIfUserExist(userId);

        List<ItemRequest> foundRequests = itemRequestRepository.findAllByUserIdOrderByCreatedDesc(userId);
        log.trace("Получен список запросов: {}", foundRequests);
        return foundRequests.stream()
                .map(itemRequestDtoMapper::mapItemRequestToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestResponseDto> readAllUsersRequests(Long userId, int from, int size) {
        log.debug("Пользователь с ID = {} запросил список всех запросов на предметыс разбивкой [{},{}]",
                userId, from, size
        );
        checkIfUserExist(userId);

        List<ItemRequest> foundRequests =
                itemRequestRepository.findAllByUserIdNotOrderByCreatedDesc(userId, PageRequest.of(from, size));
        log.trace("Получен список запросов: {}", foundRequests);

        return foundRequests.stream()
                .map(itemRequestDtoMapper::mapItemRequestToResponseDto)
                .collect(Collectors.toList());
    }

    private void checkIfRequestExist(Long itemRequestId) {
        if (!itemRequestRepository.existsById(itemRequestId)) {
            log.warn("Запрос с ID = {} не существует", itemRequestId);
            throw new ItemRequestNotFoundException("Запрос не существует");
        }
    }

    private void checkIfUserExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с ID = {} не существует", userId);
            throw new ItemRequestNotFoundException("Пользователь не существует");
        }
    }

}
