package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.service.validation.Create;
import ru.practicum.shareit.item.service.ItemService;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemResponseDto create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(Create.class) @RequestBody ItemDto itemDto
    ) {
        return itemService.create(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto read(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId
    ) {
        return itemService.read(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto
    ) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemResponseDto> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAll(userId);
    }

    @DeleteMapping("/{itemId}")
    public ItemResponseDto delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemService.delete(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> findAvailable(
            @RequestParam("text") String text
    ) {
        return itemService.findAvailableItemsBySearchQuery(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto createComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Validated(Create.class) @RequestBody CommentDto commentDto
    ) {
        return itemService.createComment(userId, itemId, commentDto);
    }

}
