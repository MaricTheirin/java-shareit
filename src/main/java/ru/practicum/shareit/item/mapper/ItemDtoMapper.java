package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ItemDtoMapper {

    private static final String OBJECT_MAPPED_MESSAGE = "Выполнено преобразование объекта из {} в {}";
    private final CommentRepository commentRepository;
    private final CommentDtoMapper commentDtoMapper;

    @Autowired
    public ItemDtoMapper(
            CommentRepository commentRepository,
            CommentDtoMapper commentDtoMapper
    ) {
        this.commentRepository = commentRepository;
        this.commentDtoMapper = commentDtoMapper;
    }

    public ItemDto mapItemToDto(Item item) {
        return mapItemToDto(item, false);
    }

    public ItemDto mapItemToDto(Item item, boolean belongsToRequestedUser) {
        ItemDto mappedDto = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable()
        );

        log.trace(OBJECT_MAPPED_MESSAGE, item, mappedDto);
        return mappedDto;
    }

    public ItemResponseDto mapItemToResponseDto(Item item) {
        return mapItemToResponseDto(item, null, null);
    }

    public ItemResponseDto mapItemToResponseDto(Item item, BookingDto lastBooking, BookingDto nextBooking) {
        List<CommentResponseDto> itemComments = commentRepository
                .findAllByItemId(item.getId())
                .stream()
                .map(commentDtoMapper::mapCommentToResponseDto)
                .collect(Collectors.toList());

        ItemResponseDto mappedItemResponseDto = new ItemResponseDto(
                item.getId(),
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

    public Item mapDtoToItem(ItemDto itemDto, User owner) {
        Item mappedItem = new Item(
                itemDto.getId(),
                owner,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
        log.trace(OBJECT_MAPPED_MESSAGE, itemDto, mappedItem);
        return mappedItem;
    }

}
