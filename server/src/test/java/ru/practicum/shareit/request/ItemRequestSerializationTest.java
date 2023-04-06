package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.ResolvableType;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestShortResponseDto;
import ru.practicum.shareit.user.model.User;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestSerializationTest {

    private final ObjectMapper objectMapper;
    private final LocalDateTime creationTime = LocalDateTime.now().withNano(0);
    private final User owner = new User(1L, "User#1", "user1@server.com");
    private final Item item = new Item(1L, 1L, owner, "Item#1", "Item#1_OfUser#1", true);
    private final ItemShortResponseDto itemShortResponseDto = new ItemShortResponseDto(
            item.getId(),
            item.getRequestId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            item.getOwner().getId()
    );

    private final ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto(
            1L,
            "ItemRequest#1_Description",
            creationTime,
            List.of(itemShortResponseDto)
    );

    private final ItemRequestShortResponseDto itemRequestShortResponseDto = new ItemRequestShortResponseDto(
            itemRequestResponseDto.getId(),
            itemRequestResponseDto.getDescription(),
            itemRequestResponseDto.getCreated()
    );

    @Test
    void itemRequestResponseDtoSerializationTest() throws IOException {
        JacksonTester<ItemRequestResponseDto> tester = new JacksonTester<>(
                ItemRequestResponseDto.class,
                ResolvableType.forClass(ItemRequestResponseDto.class),
                objectMapper
        );
        JsonContent<ItemRequestResponseDto> content = tester.write(itemRequestResponseDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);

        assertThat(content).hasJsonPath("$.description");
        assertThat(content).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestResponseDto.getDescription());

        assertThat(content).hasJsonPath("$.created");
        assertThat(content).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequestResponseDto.getCreated().toString());

        assertThat(content).hasJsonPath("$.items");
        assertThat(content).extractingJsonPathArrayValue("$.items").asList().size()
                .isEqualTo(itemRequestResponseDto.getItems().size());

    }

    @Test
    void itemRequestShortResponseDtoSerializationTest() throws IOException {
        JacksonTester<ItemRequestShortResponseDto> tester = new JacksonTester<>(
                ItemRequestShortResponseDto.class,
                ResolvableType.forClass(ItemRequestShortResponseDto.class),
                objectMapper
        );
        JsonContent<ItemRequestShortResponseDto> content = tester.write(itemRequestShortResponseDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);

        assertThat(content).hasJsonPath("$.description");
        assertThat(content).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestResponseDto.getDescription());

        assertThat(content).hasJsonPath("$.created");
        assertThat(content).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequestResponseDto.getCreated().toString());

    }

}
