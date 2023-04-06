package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDtoMapper {

    private static final String DEFAULT_MESSAGE = "Выполнено преобразование объекта из {} в {}";

    public static User mapDtoToUser(UserDto userDto) {
        User user = new User(0, userDto.getName(), userDto.getEmail());
        log.trace(DEFAULT_MESSAGE, userDto, user);
        return user;
    }

    public static UserResponseDto mapUserToResponseDto(User user) {
        UserResponseDto userResultDto = new UserResponseDto(user.getId(), user.getName(), user.getEmail());
        log.trace(DEFAULT_MESSAGE, user, userResultDto);
        return userResultDto;
    }

}
