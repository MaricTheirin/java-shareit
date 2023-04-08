package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.ResolvableType;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingSerializationTest {

    private final ObjectMapper objectMapper;
    private final LocalDateTime bookingStart = LocalDateTime.now().withNano(0);
    private final LocalDateTime bookingEnd = LocalDateTime.now().plusDays(1).withNano(0);
    private final User owner = new User(1L, "User#1", "user1@server.com");
    private final User booker = new User(2L, "User#2", "user2@server.com");
    private final UserResponseDto bookerResponseDto = new UserResponseDto(booker.getId(), booker.getName(),booker.getEmail());
    private final Item item = new Item(1L, 1L, owner, "Item#1", "Item#1_OfUser#1", true);
    private final ItemResponseDto itemResponseDto = new ItemResponseDto(
            item.getId(),
            item.getRequestId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            null,
            null,
            Collections.emptyList()
    );

    private final Booking booking = new Booking(
            1L,
            item,
            booker,
            bookingStart,
            bookingEnd,
            BookingStatus.WAITING
    );

    private final BookingResponseDto bookingResponseDto = new BookingResponseDto(
            booking.getId(),
            bookingStart,
            bookingEnd,
            booking.getStatus(),
            bookerResponseDto,
            itemResponseDto
    );

    private final BookingShortResponseDto bookingShortResponseDto = new BookingShortResponseDto(
            booking.getId(),
            booker.getId(),
            bookingStart,
            bookingEnd
    );


    @Test
    void bookingResponseDtoSerializationTest() throws IOException {
        JacksonTester<BookingResponseDto> bookingTester =
                new JacksonTester<>(BookingResponseDto.class, ResolvableType.forClass(BookingResponseDto.class), objectMapper);
        JsonContent<BookingResponseDto> content = bookingTester.write(bookingResponseDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);

        assertThat(content).hasJsonPath("$.start");
        assertThat(content).extractingJsonPathStringValue("$.start")
                .isEqualTo(booking.getStart().toString());

        assertThat(content).hasJsonPath("$.end");
        assertThat(content).extractingJsonPathStringValue("$.end")
                .isEqualTo(booking.getEnd().toString());

        assertThat(content).hasJsonPath("$.status");
        assertThat(content).extractingJsonPathStringValue("$.status")
                .isEqualTo(booking.getStatus().toString());

        assertThat(content).hasJsonPath("$.booker");
        assertThat(content).hasJsonPath("$.item");
    }

    @Test
    void bookingShortResponseDtoSerializationTest() throws IOException {
        JacksonTester<BookingShortResponseDto> bookingTester = new JacksonTester<>(
                BookingShortResponseDto.class,
                ResolvableType.forClass(BookingShortResponseDto.class),
                objectMapper
        );
        JsonContent<BookingShortResponseDto> content = bookingTester.write(bookingShortResponseDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);

        assertThat(content).hasJsonPath("$.bookerId");
        assertThat(content).extractingJsonPathNumberValue("$.id")
                .isEqualTo((int)bookingShortResponseDto.getId());

        assertThat(content).hasJsonPath("$.start");
        assertThat(content).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingShortResponseDto.getStart().toString());

        assertThat(content).hasJsonPath("$.end");
        assertThat(content).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingShortResponseDto.getEnd().toString());
    }


}
