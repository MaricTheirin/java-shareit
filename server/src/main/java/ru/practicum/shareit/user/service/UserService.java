package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import java.util.List;

public interface UserService {

    UserResponseDto create(UserDto userDto);

    UserResponseDto read(Long userId);

    UserResponseDto update(Long userId, UserDto userDto);

    UserResponseDto delete(Long userId);

    List<UserResponseDto> findAll();

}
