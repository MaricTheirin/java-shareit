package ru.practicum.shareit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DefaultExceptionHandlerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    private final UserDto userDto = new UserDto(1L, "User#1", "user1@server.com");
    private final User user = UserDtoMapper.mapDtoToUser(userDto);
    private final UserResponseDto userResponseDto = UserDtoMapper.mapUserToResponseDto(user);

    @Test
    void testExceptionHandler_handleMethodArgumentNotValidException() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                    .header("X-Sharer-User-Id", "1")
                    .param("from", "-1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                ).andExpect(status().isBadRequest());

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                .header("X-Sharer-User-Id", "1")
                .param("size", "-1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testExceptionHandler_handleMissingRequestHeaderException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/bookings/review")
                .header("X-Sharer-User-Id", "1")
                .param("from", "-1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testExceptionHandler_handleDataIntegrityViolationException() throws Exception {
        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        ).andExpect(status().isOk());

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        ).andExpect(status().isConflict());
    }

}
