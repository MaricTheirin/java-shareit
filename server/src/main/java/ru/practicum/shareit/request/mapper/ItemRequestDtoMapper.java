package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestDtoMapper {

    private static final String OBJECT_MAPPED_MESSAGE = "Выполнено преобразование объекта из {} в {}";

    public static ItemRequest mapDtoToItemRequest(User author, ItemRequestDto itemRequestDto) {
        ItemRequest mappedRequest = new ItemRequest(
                0L,
                author,
                itemRequestDto.getDescription(),
                LocalDateTime.now()
        );

        log.trace(OBJECT_MAPPED_MESSAGE, itemRequestDto, mappedRequest);
        return mappedRequest;
    }

    public static ItemRequestResponseDto mapItemRequestToResponseDto(
            ItemRequest request,
            List<ItemShortResponseDto> items
    ) {

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
