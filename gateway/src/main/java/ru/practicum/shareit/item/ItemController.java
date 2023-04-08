package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.service.validation.Create;
import ru.practicum.shareit.service.validation.Update;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(Create.class) @RequestBody ItemDto itemDto
    ) {
        log.info("Пользователь с id={} запросил создание предмета={}", userId, itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> read(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId
    ) {
        log.info("Пользователь с id={} запросил предмет с id={}", userId, itemId);
        return itemClient.getItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Validated(Update.class) @RequestBody ItemDto itemDto
    ) {
        log.info("Пользователь с id={} запросил обновление предмета с id={} на {}", userId, itemId, itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пользователь с id={} запросил список всех предметов", userId);
        return itemClient.findAll(userId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Пользователь с id={} запросил удаление предмета с id={}", userId, itemId);
        return itemClient.delete(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findAvailableItemsBySearchQuery(
            @RequestParam("text") String text,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") long from,
            @Positive @RequestParam(defaultValue = "20") long size
    ) {
        log.info("Пользователь с id={} ищет предметы по запросу \"{}\" с разбивкой [{},{}]", userId, text, from, size);
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.findAvailableItemsBySearchQuery(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Validated(Create.class) @RequestBody CommentDto commentDto
    ) {
        log.info("Пользователь с id={} создаёт комментарий {} для предмета с id={}", userId, commentDto, itemId);
        return itemClient.createComment(userId, itemId, commentDto);
    }

}
