package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {

    private static final String PATH = "/requests";

    @MockBean
    ItemRequestClient itemRequestClient;

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    private final ItemRequestDto itemRequestTooShortDto = new ItemRequestDto("");

    @Test
    void createRequestTest_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemRequestTooShortDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    void readAllUserRequestsTest_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get(PATH.concat("/all"))
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "-1")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(PATH.concat("/all"))
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

}
