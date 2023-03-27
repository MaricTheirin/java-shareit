package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;

@Slf4j
@Component
public class ItemDtoMapper {

    private static final String OBJECT_MAPPED_MESSAGE = "Выполнено преобразование объекта из {} в {}";
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemDtoMapper(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public ItemDto mapItemToDto(Item item) {
        return mapItemToDto(item, false);
    }

    public ItemDto mapItemToDto(Item item, boolean belongsToRequestedUser) {
        ItemDto mappedDto = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                null,
                null
        );
        if (belongsToRequestedUser) {
            mappedDto.setLastBooking(bookingRepository.getLastBooking(item.getId()));
            mappedDto.setNextBooking(bookingRepository.getNextBooking(item.getId()));
        }
        log.trace(OBJECT_MAPPED_MESSAGE, item, mappedDto);
        return mappedDto;
    }

    public Item mapDtoToItem(Long userId, ItemDto itemDto) {
        Item mappedItem = new Item(
                itemDto.getId(),
                userId,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
        log.trace(OBJECT_MAPPED_MESSAGE, itemDto, mappedItem);
        return mappedItem;
    }

}
