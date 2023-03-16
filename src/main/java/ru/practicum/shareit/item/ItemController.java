package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import javax.validation.Valid;
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
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto read(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemService.read(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto
    ) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAll(userId);
    }

    @DeleteMapping("/{itemId}")
    public ItemDto delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemService.delete(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> findAvailable(
            @RequestParam("text") String text
    ) {
        return itemService.findAvailable(text);
    }

}
