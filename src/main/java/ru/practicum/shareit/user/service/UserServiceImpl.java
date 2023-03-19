package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.exception.UserAlreadyExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDtoMapper userDtoMapper) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public UserDto create(UserDto userDto) {
        log.debug("Запрошено сохранение пользователя: {}", userDto);
        checkBeforeSave(userDto);

        User savedUser = userRepository.save(userDtoMapper.mapDtoToUser(userDto));
        log.trace("Сохранённый пользователь: {}", savedUser);
        return userDtoMapper.mapUserToDto(savedUser);
    }

    @Override
    public UserDto read(Long userId) {
        log.debug("Запрошен пользователь с id = {}", userId);
        checkIfUserExist(userId);

        User requestedUser = userRepository.get(userId);
        log.trace("Найден пользователь: {}", requestedUser);
        return userDtoMapper.mapUserToDto(requestedUser);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        log.debug("Запрошено обновление пользователя с id = {}", userDto);
        checkBeforeUpdate(userId, userDto);

        User savedUser = userRepository.get(userId);
        updateUserFields(savedUser, userDto);
        log.trace("Пользователь обновлён. Сохранённое значение: {}", savedUser);
        return userDtoMapper.mapUserToDto(savedUser);
    }

    @Override
    public UserDto delete(Long userId) {
        log.debug("Запрошено удаление пользователя с id = {}", userId);
        checkIfUserExist(userId);

        User requestedUser = userRepository.delete(userId);
        log.trace("Удалён пользователь: {}", requestedUser);
        return userDtoMapper.mapUserToDto(requestedUser);
    }

    @Override
    public List<UserDto> findAll() {
        log.debug("Запрошен список всех пользователей");
        List<UserDto> users =
                userRepository.findAll().stream().map(userDtoMapper::mapUserToDto).collect(Collectors.toList());
        log.trace("Полученное значение: {}", users);
        return users;
    }

    private void checkBeforeSave(UserDto userDto) {
        checkIfEmailExist(userDto.getEmail());
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

    private void checkIfEmailExist(String email) {
        if (userRepository.isExist(email)) {
            log.warn("Пользователь с email = {} уже существует", email);
            throw new UserAlreadyExistException("Пользователь с таким email уже существует");
        }
    }

    private void updateUserFields(User savedUser, UserDto userUpdatedDto) {
        log.debug("Обновление пользователя {} данными из DTO: {}", savedUser, userUpdatedDto);

        if (userUpdatedDto.getEmail() != null && !userUpdatedDto.getEmail().isBlank()) {
            if (!savedUser.getEmail().equals(userUpdatedDto.getEmail())) {
                checkIfEmailExist(userUpdatedDto.getEmail());
            }

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
