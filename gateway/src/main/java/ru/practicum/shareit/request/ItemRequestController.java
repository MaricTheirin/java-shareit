package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.service.validation.Create;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    ResponseEntity<Object> createRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(value = Create.class) @RequestBody ItemRequestDto requestDto
    ) {
        log.info("Пользователь с id={} создал запрос на подбор вещи {}", userId, requestDto);
        return itemRequestClient.create(userId, requestDto);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId
    ) {
        log.info("Пользователь с id={} запросил информацию о подборе вещи с id={}", userId, requestId);
        return itemRequestClient.read(userId, requestId);
    }

    @GetMapping
    ResponseEntity<Object> readUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пользователь с id={} запросил список своих обращений на подбор вещей", userId);
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
        log.info("Пользователь с id={} запросил информацию о всех заявках на подбор с разбивкой [{},{}]",
                userId, from, size
        );
        return itemRequestClient.readAllUsersRequests(userId, from, size);
    }

}
