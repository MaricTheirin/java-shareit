package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {

    private static final String PATH = "/users";

    @MockBean
    private final UserClient userClient;

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    private final UserDto userDtoWithEmptyName = new UserDto(1L, "", "user1@server.com");
    private final UserDto userDtoWithoutName = new UserDto(1L, null, "user1@server.com");
    private final UserDto userDtoWithEmptyEmail = new UserDto(1L, "user#1", "");
    private final UserDto userDtoWithoutEmail = new UserDto(1L, "user#1", null);
    private final UserDto userDtoWithBadEmail = new UserDto(1L, "user#1", "user1server.com");

    @Test
    void createTest() throws Exception {
        mockMvc.perform(post(PATH)
                        .content(objectMapper.writeValueAsString(userDtoWithEmptyName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH)
                        .content(objectMapper.writeValueAsString(userDtoWithoutName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH)
                        .content(objectMapper.writeValueAsString(userDtoWithEmptyEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH)
                        .content(objectMapper.writeValueAsString(userDtoWithoutEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH)
                        .content(objectMapper.writeValueAsString(userDtoWithBadEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTest() throws Exception {
        mockMvc.perform(patch(PATH.concat("/1"))
                        .content(objectMapper.writeValueAsString(userDtoWithBadEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

}
