package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto read(Long userId);

    UserDto update(Long userId, UserDto userDto);

    UserDto delete(Long userId);

    List<UserDto> findAll();

}
