package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.service.validation.Create;
import ru.practicum.shareit.service.validation.Update;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @Validated(Create.class) @RequestBody UserDto userDto
    ) {
        log.info("Запрошено создание пользователя {}", userDto);
        return userClient.create(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> read(@PathVariable Long userId) {
        log.info("Запрошена информация о пользователе с id={}", userId);
        return userClient.read(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(
            @PathVariable Long userId,
            @Validated(Update.class) @RequestBody UserDto userDto
    ) {
        log.info("Запрошено обновление пользователя с id={} данными {}", userId, userDto);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        log.info("Запрошено удаление пользователя с id={}", userId);
        return userClient.delete(userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Запрошен список всех пользователей");
        return userClient.findAll();
    }

}
