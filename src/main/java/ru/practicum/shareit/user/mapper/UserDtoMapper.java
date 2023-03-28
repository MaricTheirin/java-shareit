package ru.practicum.shareit.user.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Component
public class UserDtoMapper {

    private static final String DEFAULT_MESSAGE = "Выполнено преобразование объекта из {} в {}";

    public User mapDtoToUser(UserDto userDto) {
        User user = new User(0, userDto.getName(), userDto.getEmail());
        log.trace(DEFAULT_MESSAGE, userDto, user);
        return user;
    }

    public UserDto mapUserToDto(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        log.trace(DEFAULT_MESSAGE, user, userDto);
        return userDto;
    }

}
