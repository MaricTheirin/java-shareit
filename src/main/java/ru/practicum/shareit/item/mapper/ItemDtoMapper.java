package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.List;

@Slf4j
@Component
public class ItemDtoMapper {

    private static final String OBJECT_MAPPED_MESSAGE = "Выполнено преобразование объекта из {} в {}";

    public static ItemResponseDto mapItemToResponseDto(Item item) {
        return mapItemToResponseDto(item, null);
    }

    public static ItemResponseDto mapItemToResponseDto(Item item, List<CommentResponseDto> comments) {
        return mapItemToResponseDto(item, null, null, comments);
    }

    public static ItemResponseDto mapItemToResponseDto(
            Item item,
            BookingShortResponseDto lastBooking,
            BookingShortResponseDto nextBooking,
            List<CommentResponseDto> itemComments
    ) {
        ItemResponseDto mappedItemResponseDto = new ItemResponseDto(
                item.getId(),
                item.getRequestId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                lastBooking,
                nextBooking,
                itemComments
        );

        log.trace(OBJECT_MAPPED_MESSAGE, item, mappedItemResponseDto);
        return mappedItemResponseDto;
    }

    public static Item mapDtoToItem(ItemDto itemDto, User owner) {
        Item mappedItem = new Item(
                itemDto.getId(),
                itemDto.getRequestId(),
                owner,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
        log.trace(OBJECT_MAPPED_MESSAGE, itemDto, mappedItem);
        return mappedItem;
    }

    public static ItemShortResponseDto mapItemToShortResponseDto(Item item) {
        ItemShortResponseDto mappedShortResponseDto = new ItemShortResponseDto(
                item.getId(),
                item.getRequestId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner().getId()
        );
        log.trace(OBJECT_MAPPED_MESSAGE, item, mappedShortResponseDto);
        return mappedShortResponseDto;
    }

}
