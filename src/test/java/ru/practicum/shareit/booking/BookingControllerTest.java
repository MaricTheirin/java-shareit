package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {

    @MockBean
    BookingController bookingController;

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    private BookingDto dto;
    private BookingResponseDto responseDto;
    private UserResponseDto userDto;
    private CommentResponseDto commentDto;
    private ItemResponseDto itemDto;


    @BeforeEach
    private void setUp() {
        userDto = new UserResponseDto(1L, "User#1", "User@server.com");
        commentDto =
                new CommentResponseDto(1L, "Comment#1", 1L, "Author#1", LocalDateTime.now().withNano(0));
        itemDto = new ItemResponseDto(
                1L,
                null,
                "Item#1",
                "Item#1_Description",
                true,
                null,
                null,
                List.of(commentDto)
        );
        dto = new BookingDto(
                0L,
                1L,
                LocalDateTime.now().plusDays(1).withNano(0),
                LocalDateTime.now().plusDays(2).withNano(0)
        );
        responseDto = new BookingResponseDto(1L, dto.getStart(), dto.getEnd(), BookingStatus.WAITING, userDto, itemDto);
    }

    @Test
    void bookTest() throws Exception{
        Mockito
                .when(bookingController.book(Mockito.anyLong(), Mockito.any(BookingDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(jsonPath("$.id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", Is.is(responseDto.getStart().toString())))
                .andExpect(jsonPath("$.end", Is.is(responseDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", Is.is(responseDto.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", Is.is(responseDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", Is.is(responseDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", Is.is(responseDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.item.id", Is.is(responseDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", Is.is(responseDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", Is.is(responseDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", Is.is(responseDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.lastBooking", Is.is(IsNull.nullValue())))
                .andExpect(jsonPath("$.item.nextBooking", Is.is(IsNull.nullValue())))
                .andExpect(jsonPath("$.item.comments.size()", Is.is(1)))
                .andExpect(jsonPath("$.item.comments.[0].id",
                        Is.is(responseDto.getItem().getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.item.comments.[0].text",
                        Is.is(responseDto.getItem().getComments().get(0).getText())))
                .andExpect(jsonPath("$.item.comments.[0].itemId",
                        Is.is(responseDto.getItem().getComments().get(0).getItemId()), Long.class))
                .andExpect(jsonPath("$.item.comments.[0].authorName",
                        Is.is(responseDto.getItem().getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$.item.comments.[0].created",
                        Is.is(responseDto.getItem().getComments().get(0).getCreated().toString())));
    }

    @Test
    void reviewTest() throws Exception{
        Mockito
                .when(bookingController.review(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(jsonPath("$.id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", Is.is(responseDto.getStart().toString())))
                .andExpect(jsonPath("$.end", Is.is(responseDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", Is.is(responseDto.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", Is.is(responseDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", Is.is(responseDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", Is.is(responseDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.item.id", Is.is(responseDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", Is.is(responseDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", Is.is(responseDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", Is.is(responseDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.lastBooking", Is.is(IsNull.nullValue())))
                .andExpect(jsonPath("$.item.nextBooking", Is.is(IsNull.nullValue())))
                .andExpect(jsonPath("$.item.comments.size()", Is.is(1)))
                .andExpect(jsonPath("$.item.comments.[0].id",
                        Is.is(responseDto.getItem().getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.item.comments.[0].text",
                        Is.is(responseDto.getItem().getComments().get(0).getText())))
                .andExpect(jsonPath("$.item.comments.[0].itemId",
                        Is.is(responseDto.getItem().getComments().get(0).getItemId()), Long.class))
                .andExpect(jsonPath("$.item.comments.[0].authorName",
                        Is.is(responseDto.getItem().getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$.item.comments.[0].created",
                        Is.is(responseDto.getItem().getComments().get(0).getCreated().toString())));
    }

    @Test
    void getTest() throws Exception{
        Mockito
                .when(bookingController.get(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(jsonPath("$.id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", Is.is(responseDto.getStart().toString())))
                .andExpect(jsonPath("$.end", Is.is(responseDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", Is.is(responseDto.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", Is.is(responseDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", Is.is(responseDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", Is.is(responseDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.item.id", Is.is(responseDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", Is.is(responseDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", Is.is(responseDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", Is.is(responseDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.lastBooking", Is.is(IsNull.nullValue())))
                .andExpect(jsonPath("$.item.nextBooking", Is.is(IsNull.nullValue())))
                .andExpect(jsonPath("$.item.comments.size()", Is.is(1)))
                .andExpect(jsonPath("$.item.comments.[0].id",
                        Is.is(responseDto.getItem().getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.item.comments.[0].text",
                        Is.is(responseDto.getItem().getComments().get(0).getText())))
                .andExpect(jsonPath("$.item.comments.[0].itemId",
                        Is.is(responseDto.getItem().getComments().get(0).getItemId()), Long.class))
                .andExpect(jsonPath("$.item.comments.[0].authorName",
                        Is.is(responseDto.getItem().getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$.item.comments.[0].created",
                        Is.is(responseDto.getItem().getComments().get(0).getCreated().toString())));
    }

    @Test
    void getOwnBookingsTest() throws Exception{
        Mockito.when(bookingController.getOwnBookings(
                Mockito.anyLong(), Mockito.any(BookingState.class), Mockito.anyLong(), Mockito.anyLong())
        ).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/bookings", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Is.is(1)))
                .andExpect(jsonPath("$[0].id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", Is.is(responseDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", Is.is(responseDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", Is.is(responseDto.getStatus().toString())))
                .andExpect(jsonPath("$[0].booker.id", Is.is(responseDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.name", Is.is(responseDto.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Is.is(responseDto.getBooker().getEmail())))
                .andExpect(jsonPath("$[0].item.id", Is.is(responseDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", Is.is(responseDto.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Is.is(responseDto.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Is.is(responseDto.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.lastBooking", Is.is(IsNull.nullValue())))
                .andExpect(jsonPath("$[0].item.nextBooking", Is.is(IsNull.nullValue())))
                .andExpect(jsonPath("$[0].item.comments.size()", Is.is(1)))
                .andExpect(jsonPath("$[0].item.comments.[0].id",
                        Is.is(responseDto.getItem().getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].item.comments.[0].text",
                        Is.is(responseDto.getItem().getComments().get(0).getText())))
                .andExpect(jsonPath("$[0].item.comments.[0].itemId",
                        Is.is(responseDto.getItem().getComments().get(0).getItemId()), Long.class))
                .andExpect(jsonPath("$[0].item.comments.[0].authorName",
                        Is.is(responseDto.getItem().getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[0].item.comments.[0].created",
                        Is.is(responseDto.getItem().getComments().get(0).getCreated().toString())));

        mockMvc.perform(get("/bookings", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "UNKNOWN_STATE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

    }

    @Test
    void findOwnItemsBookingsTest() throws Exception{
        Mockito
                .when(bookingController.findOwnItemsBookings(
                        Mockito.anyLong(), Mockito.any(BookingState.class), Mockito.anyLong(), Mockito.anyLong())
                ).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/bookings/owner", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(jsonPath("$.size()", Is.is(1)))
                .andExpect(jsonPath("$[0].id", Is.is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", Is.is(responseDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", Is.is(responseDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", Is.is(responseDto.getStatus().toString())))
                .andExpect(jsonPath("$[0].booker.id", Is.is(responseDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.name", Is.is(responseDto.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Is.is(responseDto.getBooker().getEmail())))
                .andExpect(jsonPath("$[0].item.id", Is.is(responseDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", Is.is(responseDto.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Is.is(responseDto.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Is.is(responseDto.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.lastBooking", Is.is(IsNull.nullValue())))
                .andExpect(jsonPath("$[0].item.nextBooking", Is.is(IsNull.nullValue())))
                .andExpect(jsonPath("$[0].item.comments.size()", Is.is(1)))
                .andExpect(jsonPath("$[0].item.comments.[0].id",
                        Is.is(responseDto.getItem().getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].item.comments.[0].text",
                        Is.is(responseDto.getItem().getComments().get(0).getText())))
                .andExpect(jsonPath("$[0].item.comments.[0].itemId",
                        Is.is(responseDto.getItem().getComments().get(0).getItemId()), Long.class))
                .andExpect(jsonPath("$[0].item.comments.[0].authorName",
                        Is.is(responseDto.getItem().getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[0].item.comments.[0].created",
                        Is.is(responseDto.getItem().getComments().get(0).getCreated().toString())));
    }

}
