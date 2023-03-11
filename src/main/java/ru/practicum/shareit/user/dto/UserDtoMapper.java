package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Component
public class UserDtoMapper {

    public User mapDtoToUser(Long userId, UserDto userDto) {
        return new User(userId, userDto.getName(), userDto.getEmail());
    }

    public User mapDtoToUser(UserDto userDto) {
        return new User(0, userDto.getName(), userDto.getEmail());
    }

    public UserDto mapUserToDto(User user) {
        return new UserDto(user.getName(), user.getEmail());
    }

}
