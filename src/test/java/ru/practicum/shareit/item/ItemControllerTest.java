package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    private ItemDto dto;
    private ItemResponseDto responseDto;
    private CommentDto commentDto;
    private CommentResponseDto commentResponseDto;

    @BeforeEach
    public void setUp() {
        dto = new ItemDto(0, null, "Item#1", "Item#1_Desc", true);
        responseDto = new ItemResponseDto(
                1,
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getAvailable(),
                null,
                null,
                Collections.emptyList()
        );
        commentDto = new CommentDto(0L, "Comment_Text");
        commentResponseDto = new CommentResponseDto(
                1L,
                "Comment_Text",
                1L,
                "Comment_Author",
                LocalDateTime.now().withNano(0)
        );
    }


    @Test
    void createTest() throws Exception {
        Mockito
                .when(itemService.create(Mockito.any(), Mockito.any(ItemDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", Is.is(responseDto.getName())))
                .andExpect(jsonPath("$.description", Is.is(responseDto.getDescription())))
                .andExpect(jsonPath("$.available", Is.is(responseDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking", Is.is(responseDto.getLastBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$.nextBooking", Is.is(responseDto.getNextBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    void readTest() throws Exception {
        Mockito
                .when(itemService.read(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", Is.is(responseDto.getName())))
                .andExpect(jsonPath("$.description", Is.is(responseDto.getDescription())))
                .andExpect(jsonPath("$.available", Is.is(responseDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking", Is.is(responseDto.getLastBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$.nextBooking", Is.is(responseDto.getNextBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    void updateTest() throws Exception {
        Mockito
                .when(itemService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", Is.is(responseDto.getName())))
                .andExpect(jsonPath("$.description", Is.is(responseDto.getDescription())))
                .andExpect(jsonPath("$.available", Is.is(responseDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking", Is.is(responseDto.getLastBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$.nextBooking", Is.is(responseDto.getNextBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    void deleteTest() throws Exception {
        Mockito
                .when(itemService.delete(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(delete("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", Is.is(responseDto.getName())))
                .andExpect(jsonPath("$.description", Is.is(responseDto.getDescription())))
                .andExpect(jsonPath("$.available", Is.is(responseDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking", Is.is(responseDto.getLastBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$.nextBooking", Is.is(responseDto.getNextBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    void getTest() throws Exception {
        Mockito
                .when(itemService.findAll(Mockito.anyLong()))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/items", 1L)
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", Is.is(responseDto.getName())))
                .andExpect(jsonPath("$[0].description", Is.is(responseDto.getDescription())))
                .andExpect(jsonPath("$[0].available", Is.is(responseDto.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking", Is.is(responseDto.getLastBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$[0].nextBooking", Is.is(responseDto.getNextBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$[0].comments", hasSize(0)));
    }

    @Test
    public void searchTest() throws Exception {
        Mockito
                .when(itemService.findAvailableItemsBySearchQuery(Mockito.anyString()))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "searchText")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", Is.is(responseDto.getName())))
                .andExpect(jsonPath("$[0].description", Is.is(responseDto.getDescription())))
                .andExpect(jsonPath("$[0].available", Is.is(responseDto.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking", Is.is(responseDto.getLastBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$[0].nextBooking", Is.is(responseDto.getNextBooking()), BookingShortResponseDto.class))
                .andExpect(jsonPath("$[0].comments", hasSize(0)));
    }

    @Test
    void createCommentTest() throws Exception {
        Mockito
                .when(itemService.createComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentDto.class)))
                .thenReturn(commentResponseDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(commentResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", Is.is(commentResponseDto.getText())))
                .andExpect(jsonPath("$.itemId", Is.is(commentResponseDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.authorName", Is.is(commentResponseDto.getAuthorName())))
                .andExpect(jsonPath("$.created", Is.is(commentResponseDto.getCreated().toString())));
    }

}
