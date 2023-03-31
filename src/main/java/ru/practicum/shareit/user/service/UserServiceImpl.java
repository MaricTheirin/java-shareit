package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Override
    @Transactional
    public UserResponseDto create(UserDto userDto) {
        log.debug("Запрошено сохранение пользователя: {}", userDto);

        User savedUser = userRepository.save(userDtoMapper.mapDtoToUser(userDto));
        log.trace("Сохранённый пользователь: {}", savedUser);
        return userDtoMapper.mapUserToResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto read(Long userId) {
        log.debug("Запрошен пользователь с id = {}", userId);
        checkIfUserExist(userId);

        User requestedUser = userRepository.getReferenceById(userId);
        log.trace("Найден пользователь: {}", requestedUser);
        return userDtoMapper.mapUserToResponseDto(requestedUser);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long userId, UserDto userDto) {
        log.debug("Запрошено обновление пользователя с id = {}", userDto);
        checkBeforeUpdate(userId, userDto);

        User savedUser = userRepository.getReferenceById(userId);
        updateUserFields(savedUser, userDto);
        log.trace("Пользователь обновлён. Сохранённое значение: {}", savedUser);
        return userDtoMapper.mapUserToResponseDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto delete(Long userId) {
        log.debug("Запрошено удаление пользователя с id = {}", userId);
        checkIfUserExist(userId);

        User requestedUser = userRepository.getReferenceById(userId);
        userRepository.delete(requestedUser);
        log.trace("Удалён пользователь: {}", requestedUser);
        return userDtoMapper.mapUserToResponseDto(requestedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        log.debug("Запрошен список всех пользователей");
        List<UserResponseDto> users =
                userRepository.findAll().stream().map(userDtoMapper::mapUserToResponseDto).collect(Collectors.toList());
        log.trace("Полученное значение: {}", users);
        return users;
    }

    private void checkBeforeUpdate(Long userId, UserDto userDto) {
        checkIfUserExist(userId);
    }

    private void checkIfUserExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с id = {} не существует", userId);
            throw new UserNotFoundException("Пользователь не обнаружен");
        }
    }

    private void updateUserFields(User savedUser, UserDto userUpdatedDto) {
        log.debug("Обновление пользователя {} данными из DTO: {}", savedUser, userUpdatedDto);

        if (userUpdatedDto.getEmail() != null && !userUpdatedDto.getEmail().isBlank()) {
            savedUser.setEmail(userUpdatedDto.getEmail());
            log.debug(
                    "У пользователя {} изменён email. Старое значение - {}, новое значение - {}",
                    savedUser,
                    savedUser.getEmail(),
                    userUpdatedDto.getEmail()
            );
        }

        if (userUpdatedDto.getName() != null && !userUpdatedDto.getName().isBlank()) {
            savedUser.setName(userUpdatedDto.getName());
            log.debug(
                    "У пользователя {} измено имя. Старое значение - {}, новое значение - {}",
                    savedUser,
                    savedUser.getName(),
                    userUpdatedDto.getName()
            );
        }

        log.trace("Обновлённый пользователь: {}", savedUser);
    }
}
