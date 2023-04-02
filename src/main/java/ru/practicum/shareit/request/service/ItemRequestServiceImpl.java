package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestResponseDto create(Long userId, ItemRequestDto itemRequestDto) {
        log.debug("Пользователь с ID = {} создал запрос на предмет {}", userId, itemRequestDto);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        ItemRequest savedRequest =
                itemRequestRepository.save(ItemRequestDtoMapper.mapDtoToItemRequest(user, itemRequestDto));
        log.trace("Запрос сохранён: {}", savedRequest);
        return ItemRequestDtoMapper.mapItemRequestToResponseDto(savedRequest);
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
        return ItemRequestDtoMapper.mapItemRequestToResponseDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> readUserRequests(Long userId) {
        log.debug("Пользователь с ID = {} запросил список своих запросов на предметы", userId);
        checkIfUserExist(userId);

        List<ItemRequest> foundRequests = itemRequestRepository
                .findAllByUserIdOrderByCreatedDesc(userId)
                .orElseThrow(ItemRequestNotFoundException::new);
        log.trace("Получен список запросов: {}", foundRequests);
        return foundRequests.stream()
                .map(ItemRequestDtoMapper::mapItemRequestToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> readAllUsersRequests(Long userId, int from, int size) {
        log.debug("Пользователь с ID = {} запросил список всех запросов на предметыс разбивкой [{},{}]",
                userId, from, size
        );
        checkIfUserExist(userId);

        List<ItemRequest> foundRequests = itemRequestRepository
                .findAllByUserIdNotOrderByCreatedDesc(userId, PageRequest.of(from, size))
                .orElseThrow(ItemRequestNotFoundException::new);
        log.trace("Получен список запросов: {}", foundRequests);

        return foundRequests.stream()
                .map(ItemRequestDtoMapper::mapItemRequestToResponseDto)
                .collect(Collectors.toList());
    }

    private void checkIfUserExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }

}
