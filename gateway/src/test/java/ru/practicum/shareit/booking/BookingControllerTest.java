package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {

    private static final String PATH = "/bookings";

    @MockBean
    BookingClient bookingClient;

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    private final LocalDateTime startDate = LocalDateTime.now().plusDays(1);

    private final BookItemRequestDto requestDto
            = new BookItemRequestDto(1L, startDate, startDate.plusDays(1));
    private final BookItemRequestDto requestDtoWithoutItemId
            = new BookItemRequestDto(null, startDate, startDate.plusDays(1));
    private final BookItemRequestDto requestDtoWithoutStartDate
            = new BookItemRequestDto(1L, null, startDate.plusDays(1));
    private final BookItemRequestDto requestDtoWithoutEndDate
            = new BookItemRequestDto(1L, startDate, null);

    @Test
    void bookTest() throws Exception {
        mockMvc.perform(post(PATH)
                    .header("X-Sharer-User-Id", "1")
                    .content(objectMapper.writeValueAsString(requestDtoWithoutItemId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(requestDtoWithoutStartDate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(requestDtoWithoutEndDate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reviewTest() throws Exception {
        mockMvc.perform(patch(PATH.concat("/1"))
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOwnBookingsTest() throws Exception {
        mockMvc.perform(get(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ERROR_STATE")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "0")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

    }

    @Test
    void findOwnItemsBookingsTest() throws Exception {
        mockMvc.perform(get(PATH.concat("/owner"))
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ERROR_STATE")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(PATH.concat("/owner"))
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(PATH.concat("/owner"))
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "0")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

    }

}
