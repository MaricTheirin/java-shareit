package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemRequestDtoMapper {

    private static final String OBJECT_MAPPED_MESSAGE = "Выполнено преобразование объекта из {} в {}";

    public static ItemRequest mapDtoToItemRequest(User author, ItemRequestDto itemRequestDto) {
        ItemRequest mappedRequest = new ItemRequest(
                0L,
                author,
                itemRequestDto.getDescription(),
                LocalDateTime.now(),
                Collections.emptyList()
        );

        log.trace(OBJECT_MAPPED_MESSAGE, itemRequestDto, mappedRequest);
        return mappedRequest;
    }

    public static ItemRequestResponseDto mapItemRequestToResponseDto(ItemRequest request) {

        List<ItemShortResponseDto> items =
                request.getItems().stream().map(ItemDtoMapper::mapItemToShortResponseDto).collect(Collectors.toList());

        ItemRequestResponseDto mappedResponseDto = new ItemRequestResponseDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                items
        );

        log.trace(OBJECT_MAPPED_MESSAGE, request, mappedResponseDto);
        return mappedResponseDto;
    }

}
