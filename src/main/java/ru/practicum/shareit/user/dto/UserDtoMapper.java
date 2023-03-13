package ru.practicum.shareit.user.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Component
public class UserDtoMapper {

    //В этом месте должна быть константа private static final String DEFAULT_MESSAGE, но checkstyle её не пропускает
    private final String defaultMessage = "Выполнено преобразование объекта из {} в {}";

    public User mapDtoToUser(Long userId, UserDto userDto) {
        return new User(userId, userDto.getName(), userDto.getEmail());
    }

    public User mapDtoToUser(UserDto userDto) {
        User user = new User(0, userDto.getName(), userDto.getEmail());
        log.trace(defaultMessage, userDto, user);
        return user;
    }

    public UserDto mapUserToDto(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        log.trace(defaultMessage, user, userDto);
        return userDto;
    }

}
