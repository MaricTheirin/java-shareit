package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.service.validation.Create;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    ItemRequestResponseDto createRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated(value = Create.class) @RequestBody ItemRequestDto requestDto
    ) {
        return itemRequestService.create(userId, requestDto);
    }

    @GetMapping("/{requestId}")
    ItemRequestResponseDto getRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId
    ) {
        return itemRequestService.read(userId, requestId);
    }

    @GetMapping
    List<ItemRequestResponseDto> readUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.readUserRequests(userId);
    }

    @GetMapping("/all")
    List<ItemRequestResponseDto> readAllUsersRequests(
            @RequestHeader("X-Sharer-User-Id")
            Long userId,
            @PositiveOrZero(message = "Начальная позиция должна быть не меньше 0")
            @RequestParam(name = "from", defaultValue = "0")
            int from,
            @Positive(message = "Размер выдачи должен быть больше 0") @RequestParam(name = "size", defaultValue = "20")
            int size
    ) {
        return itemRequestService.readAllUsersRequests(userId, from, size);
    }

}
