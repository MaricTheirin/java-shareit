package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.service.validation.Create;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    ResponseEntity<Object> createRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(value = Create.class) @RequestBody ItemRequestDto requestDto
    ) {
        return itemRequestClient.create(userId, requestDto);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId
    ) {
        return itemRequestClient.read(userId, requestId);
    }

    @GetMapping
    ResponseEntity<Object> readUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.readUserRequests(userId);
    }

    @GetMapping("/all")
    ResponseEntity<Object> readAllUsersRequests(
            @RequestHeader("X-Sharer-User-Id")
            Long userId,
            @PositiveOrZero(message = "Начальная позиция должна быть не меньше 0")
            @RequestParam(name = "from", defaultValue = "0")
            int from,
            @Positive(message = "Размер выдачи должен быть больше 0") @RequestParam(name = "size", defaultValue = "20")
            int size
    ) {
        return itemRequestClient.readAllUsersRequests(userId, from, size);
    }

}
