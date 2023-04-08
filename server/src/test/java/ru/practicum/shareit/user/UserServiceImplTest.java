package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    private final UserDto userDto = new UserDto(1L, "User#1", "user1@server.com");
    private final UserDto updatedUserDto =
            new UserDto(1L, "upd_".concat(userDto.getName()), "upd_".concat(userDto.getEmail()));
    private final User user = UserDtoMapper.mapDtoToUser(userDto);
    private final User updatedUser = UserDtoMapper.mapDtoToUser(updatedUserDto);
    private final UserResponseDto userResponseDto = UserDtoMapper.mapUserToResponseDto(user);
    private final UserResponseDto updatedUserResponseDto = UserDtoMapper.mapUserToResponseDto(updatedUser);

    @Test
    void createTest() {
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        UserResponseDto createdUser = userService.create(userDto);
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    void readTest() {
        Mockito.when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(eq(Long.MAX_VALUE))).thenReturn(Optional.empty());

        UserResponseDto foundUser = userService.read(user.getId());
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertThrows(UserNotFoundException.class, () -> userService.read(Long.MAX_VALUE));
    }

    @Test
    void updateTest() {
        Mockito.when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.of(user));

        UserResponseDto serviceUpdatedUser = userService.update(user.getId(), updatedUserDto);
        assertEquals(user.getId(), serviceUpdatedUser.getId());
        assertEquals(updatedUser.getName(), serviceUpdatedUser.getName());
        assertEquals(updatedUser.getEmail(), serviceUpdatedUser.getEmail());
    }

    @Test
    void deleteTest() {
        Mockito.when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.of(user));

        UserResponseDto deletedUser = userService.delete(user.getId());
        assertEquals(user.getId(), deletedUser.getId());
        assertEquals(user.getName(), deletedUser.getName());
        assertEquals(user.getEmail(), deletedUser.getEmail());
    }

    @Test
    void findAllTest() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDto> foundUsers = userService.findAll();
        assertEquals(1, foundUsers.size());
        assertEquals(user.getId(), foundUsers.get(0).getId());
        assertEquals(user.getName(), foundUsers.get(0).getName());
        assertEquals(user.getEmail(), foundUsers.get(0).getEmail());
    }

}
