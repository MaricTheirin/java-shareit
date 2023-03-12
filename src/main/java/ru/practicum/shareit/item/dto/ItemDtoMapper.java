package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Slf4j
@Component
public class ItemDtoMapper {

    private final String OBJECT_MAPPED_MESSAGE = "Выполнено преобразование объекта из {} в {}";

    public ItemDto mapItemToDto (Item item) {
        ItemDto mappedDto = new ItemDto(
                item.getItemId(),
                item.getName(),
                item.getDescription(),
                item.getShareCounter(),
                item.isAvailable()
        );
        log.trace(OBJECT_MAPPED_MESSAGE, item, mappedDto);
        return mappedDto;
    }

    public Item mapDtoToItem (Long userId, ItemDto itemDto) {
        Item mappedItem = new Item(
                itemDto.getId(),
                userId,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getShareCounter(),
                itemDto.isAvailable()
        );
        log.trace(OBJECT_MAPPED_MESSAGE, itemDto, mappedItem);
        return mappedItem;
    }

}
