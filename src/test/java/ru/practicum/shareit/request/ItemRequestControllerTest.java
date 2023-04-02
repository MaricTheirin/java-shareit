package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {

    @MockBean
    ItemRequestService itemRequestService;

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    private final User user = new User(1L, "user#1", "user1@server.com");
    private final ItemRequestDto itemRequestDto = new ItemRequestDto("Request#1_description");
    private final ItemRequestDto itemRequestTooShortDto = new ItemRequestDto("");
    private final ItemRequest itemRequest = new ItemRequest(
            1L, user, itemRequestDto.getDescription(), LocalDateTime.now().withNano(0), Collections.emptyList()
    );
    private final ItemRequestResponseDto itemRequestResponseDto = ItemRequestDtoMapper.mapItemRequestToResponseDto(itemRequest);

    @Test
    void createRequestTest() throws Exception {
        Mockito.when(itemRequestService.create(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(itemRequestResponseDto);

        mockMvc.perform(post("/requests")
                    .header("X-Sharer-User-Id", "1")
                    .content(objectMapper.writeValueAsString(itemRequestDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.created", Is.is(itemRequestResponseDto.getCreated().toString())))
                .andExpect(jsonPath("$.description", Is.is(itemRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$.items.size()", Is.is(itemRequestResponseDto.getItems().size())));

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemRequestTooShortDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestByIdTest() throws Exception {
        Mockito.when(itemRequestService.read(anyLong(), eq(itemRequest.getId())))
                .thenReturn(itemRequestResponseDto);

        mockMvc.perform(get("/requests/{requestId}", itemRequest.getId())
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.created", Is.is(itemRequestResponseDto.getCreated().toString())))
                .andExpect(jsonPath("$.description", Is.is(itemRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$.items.size()", Is.is(itemRequestResponseDto.getItems().size())));
    }

    @Test
    void readUserRequestsTest() throws Exception {
        Mockito.when(itemRequestService.readUserRequests(anyLong()))
                .thenReturn(List.of(itemRequestResponseDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Is.is(1)))
                .andExpect(jsonPath("$[0].id", Is.is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].created", Is.is(itemRequestResponseDto.getCreated().toString())))
                .andExpect(jsonPath("$[0].description", Is.is(itemRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$[0].items.size()", Is.is(itemRequestResponseDto.getItems().size())));
    }

    @Test
    void readAllUsersRequests() throws Exception {
        Mockito.when(itemRequestService.readAllUsersRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestResponseDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Is.is(1)))
                .andExpect(jsonPath("$[0].id", Is.is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].created", Is.is(itemRequestResponseDto.getCreated().toString())))
                .andExpect(jsonPath("$[0].description", Is.is(itemRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$[0].items.size()", Is.is(itemRequestResponseDto.getItems().size())));
    }

}
