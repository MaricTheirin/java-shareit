package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {

    private static final String PATH = "/items";

    @MockBean
    ItemClient itemClient;

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    private final ItemDto itemDto = new ItemDto(1L, 1L, "Item#1", "Item#1_D", true);
    private final ItemDto itemDtoWithoutAvailable =
            new ItemDto(1L, 1L, "Item#1", "Item#1_D", null);
    private final ItemDto itemDtoWithoutName =
            new ItemDto(1L, 1L, null, "Item#1_D", true);
    private final ItemDto itemDtoWithEmptyName =
            new ItemDto(1L, 1L, "", "Item#1_D", true);
    private final ItemDto itemDtoWithoutDescription =
            new ItemDto(1L, 1L, "Item#1", null, true);
    private final ItemDto itemDtoWithShortDescription =
            new ItemDto(1L, 1L, "Item#1", "i", true);

    private final CommentDto commentDtoWithoutText = new CommentDto(1L, null);
    private final CommentDto commentDtoWithEmptyText = new CommentDto(1L, "C");

    @Test
    void createTest() throws Exception {

        mockMvc.perform(post(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDtoWithoutName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDtoWithEmptyName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDtoWithoutDescription))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDtoWithShortDescription))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDtoWithoutAvailable))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAvailableItemsBySearchQueryTest() throws Exception {

        mockMvc.perform(get(PATH.concat("/search"))
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "")
                        .param("from", "0")
                        .param("size", "20")
                        .content(objectMapper.writeValueAsString(itemDtoWithoutDescription))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        mockMvc.perform(get(PATH.concat("/search"))
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "search query")
                        .param("from", "-1")
                        .param("size", "20")
                        .content(objectMapper.writeValueAsString(itemDtoWithoutDescription))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(PATH.concat("/search"))
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "search query")
                        .param("from", "0")
                        .param("size", "0")
                        .content(objectMapper.writeValueAsString(itemDtoWithoutDescription))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

    }

    @Test
    void crateCommentTest() throws Exception {

        mockMvc.perform(post(PATH.concat("/1/comment"))
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(commentDtoWithoutText))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH.concat("/1/comment"))
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(commentDtoWithEmptyText))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }


}
