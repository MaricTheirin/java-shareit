package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final MockMvc mockMvc;

    @MockBean
    UserService userService;

    private final UserDto dto = new UserDto(0L, "name", "user@server.com");
    private final UserResponseDto responseDto = new UserResponseDto(1L, "name", "user@server.com");

    @Test
    void createTest() throws Exception {

        Mockito.when(userService.create(Mockito.any())).thenReturn(responseDto);

        mockMvc.perform(post("/users")
                    .content(objectMapper.writeValueAsString(dto))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(1L), Long.class))
                .andExpect(jsonPath("$.name", Is.is(responseDto.getName())))
                .andExpect(jsonPath("$.email", Is.is(responseDto.getEmail())));
    }

    @Test
    void readTest() throws Exception {

        Mockito.when(userService.read(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/users/{userId}", 1L)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(1L), Long.class))
                .andExpect(jsonPath("$.name", Is.is(responseDto.getName())))
                .andExpect(jsonPath("$.email", Is.is(responseDto.getEmail())));
    }

    @Test
    void updateTest() throws Exception {

        UserDto userUpdatedDto =
                new UserDto(1L, "updatedUser", "updatedEmail");
        UserResponseDto userUpdatedResponseDto =
                new UserResponseDto(userUpdatedDto.getId(), userUpdatedDto.getName(),userUpdatedDto.getEmail());

        Mockito.when(userService.update(Mockito.anyLong(), Mockito.any(UserDto.class)))
                .thenReturn(userUpdatedResponseDto);

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(1L), Long.class))
                .andExpect(jsonPath("$.name", Is.is(userUpdatedResponseDto.getName())))
                .andExpect(jsonPath("$.email", Is.is(userUpdatedResponseDto.getEmail())));
    }

    @Test
    void deleteTest() throws Exception {

        Mockito.when(userService.delete(Mockito.anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(delete("/users/{userId}", 1L)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(1L), Long.class))
                .andExpect(jsonPath("$.name", Is.is(dto.getName())))
                .andExpect(jsonPath("$.email", Is.is(dto.getEmail())));
    }

    @Test
    void findAllTest() throws Exception {
        Mockito.when(userService.findAll()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Is.is(1)))
                .andExpect(jsonPath("$[0].id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", Is.is(responseDto.getName())))
                .andExpect(jsonPath("$[0].email", Is.is(responseDto.getEmail())));

    }

}
