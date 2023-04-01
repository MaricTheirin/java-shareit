package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.service.validation.Create;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
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
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        return itemRequestService.readAllUsersRequests(userId, from, size);
    }

}
