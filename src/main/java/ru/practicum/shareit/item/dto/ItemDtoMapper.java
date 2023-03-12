package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemDtoMapper {

    public ItemDto mapItemToDto (Item item) {
        return new ItemDto(
                item.getItemId(),
                item.getName(),
                item.getDescription(),
                item.getShareCounter(),
                item.isAvailable()
        );
    }

    public Item mapDtoToItem (Long userId, ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                userId,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getShareCounter(),
                itemDto.isAvailable()
        );
    }

}
