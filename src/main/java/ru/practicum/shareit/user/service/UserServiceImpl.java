package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public UserServiceImpl (UserRepository userRepository, UserDtoMapper userDtoMapper) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.debug("Запрошено сохранение пользователя: {}", userDto);
        checkBeforeSave(userDto);

        User savedUser = userRepository.saveUser(userDtoMapper.mapDtoToUser(userDto));
        log.trace("Сохранённый пользователь: {}", savedUser);
        return userDtoMapper.mapUserToDto(savedUser);
    }

    @Override
    public UserDto readUser(Long userId) {
        log.debug("Запрошен пользователь с id = {}", userId);
        checkIfUserExist(userId);

        User requestedUser = userRepository.getUser(userId);
        log.trace("Найден пользователь: {}", requestedUser);
        return userDtoMapper.mapUserToDto(requestedUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        log.debug("Запрошено обновление пользователя с id = {}", userDto);
        checkBeforeUpdate(userId, userDto);

        User updatedUser = userRepository.updateUser(userDtoMapper.mapDtoToUser(userId, userDto));
        log.trace("Обновлённый пользователь: {}", updatedUser);
        return userDtoMapper.mapUserToDto(updatedUser);
    }

    @Override
    public UserDto deleteUser(Long userId) {
        log.debug("Запрошено удаление пользователя с id = {}", userId);
        checkIfUserExist(userId);

        User requestedUser = userRepository.deleteUser(userId);
        log.trace("Удалён пользователь: {}", requestedUser);
        return userDtoMapper.mapUserToDto(requestedUser);
    }

    private void checkBeforeSave(UserDto userDto) {

    }

    private void checkBeforeUpdate(Long userId, UserDto userDto) {
        checkIfUserExist(userId);
    }

    private void checkIfUserExist(Long userId) {
        if (!userRepository.isExist(userId)) {
            log.warn("Пользователь с id = {} не существует", userId);
            throw new UserNotFoundException("Пользователь не обнаружен");
        }
    }

}
