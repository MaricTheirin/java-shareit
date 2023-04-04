package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.ResolvableType;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemSerializationTest {

    private final ObjectMapper objectMapper;
    private final LocalDateTime commentCreationTime = LocalDateTime.now().withNano(0);
    private final User owner = new User(1L, "User#1", "user1@server.com");
    private final Item item = new Item(1L, 1L, owner, "Item#1", "Item#1_OfUser#1", true);

    private final CommentResponseDto commentResponseDto = new CommentResponseDto(
            1L,
            "Comment#1_text",
            item.getId(),
            "Comment#1_AuthorName",
            commentCreationTime
    );

    private final ItemResponseDto itemResponseDto = new ItemResponseDto(
            item.getId(),
            item.getRequestId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            null,
            null,
            List.of(commentResponseDto)
    );

    private final ItemShortResponseDto itemShortResponseDto = new ItemShortResponseDto(
            item.getId(),
            item.getRequestId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            item.getOwner().getId()
    );

    @Test
    void commentResponseDtoSerializationTest() throws IOException {
        JacksonTester<CommentResponseDto> tester = new JacksonTester<>(
                CommentResponseDto.class,
                ResolvableType.forClass(CommentResponseDto.class),
                objectMapper
        );
        JsonContent<CommentResponseDto> content = tester.write(commentResponseDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);

        assertThat(content).hasJsonPath("$.text");
        assertThat(content).extractingJsonPathStringValue("$.text").isEqualTo(commentResponseDto.getText());

        assertThat(content).hasJsonPath("$.itemId");
        assertThat(content).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);

        assertThat(content).hasJsonPath("$.authorName");
        assertThat(content).extractingJsonPathStringValue("$.authorName")
                .isEqualTo(commentResponseDto.getAuthorName());

        assertThat(content).hasJsonPath("$.created");
        assertThat(content).extractingJsonPathStringValue("$.created")
                .isEqualTo(commentResponseDto.getCreated().toString());
    }

    @Test
    void itemResponseDtoSerializationTest() throws IOException {
        JacksonTester<ItemResponseDto> tester = new JacksonTester<>(
                ItemResponseDto.class,
                ResolvableType.forClass(ItemResponseDto.class),
                objectMapper
        );
        JsonContent<ItemResponseDto> content = tester.write(itemResponseDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);

        assertThat(content).hasJsonPath("$.requestId");
        assertThat(content).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);

        assertThat(content).hasJsonPath("$.name");
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo(itemResponseDto.getName());

        assertThat(content).hasJsonPath("$.description");
        assertThat(content).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemResponseDto.getDescription());

        assertThat(content).hasJsonPath("$.available");
        assertThat(content).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemResponseDto.getAvailable());

        assertThat(content).hasJsonPath("$.lastBooking");

        assertThat(content).hasJsonPath("$.nextBooking");

        assertThat(content).hasJsonPath("$.comments");
        assertThat(content).extractingJsonPathArrayValue("$.comments")
                .size()
                .isEqualTo(itemResponseDto.getComments().size());

    }

    @Test
    void itemShortResponseDtoTest() throws IOException {
        JacksonTester<ItemShortResponseDto> tester = new JacksonTester<>(
                ItemShortResponseDto.class,
                ResolvableType.forClass(ItemShortResponseDto.class),
                objectMapper
        );
        JsonContent<ItemShortResponseDto> content = tester.write(itemShortResponseDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);

        assertThat(content).hasJsonPath("$.requestId");
        assertThat(content).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);

        assertThat(content).hasJsonPath("$.name");
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo(itemShortResponseDto.getName());

        assertThat(content).hasJsonPath("$.description");
        assertThat(content).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemShortResponseDto.getDescription());

        assertThat(content).hasJsonPath("$.available");
        assertThat(content).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemShortResponseDto.isAvailable());

        assertThat(content).hasJsonPath("$.ownerId");
        assertThat(content).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);

    }

}
