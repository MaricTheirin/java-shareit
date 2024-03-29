package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto create(
            @RequestBody UserDto userDto
    ) {
        return userService.create(userDto);
    }

    @GetMapping("/{userId}")
    public UserResponseDto read(@PathVariable Long userId) {
        return userService.read(userId);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto update(
            @PathVariable Long userId,
            @RequestBody UserDto userDto
    ) {
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public UserResponseDto delete(@PathVariable Long userId) {
        return userService.delete(userId);
    }

    @GetMapping()
    public List<UserResponseDto> findAll() {
        return userService.findAll();
    }

}
