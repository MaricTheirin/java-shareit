package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.ResolvableType;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserSerializationTest {

    private final ObjectMapper objectMapper;
    private final UserResponseDto userResponseDto = new UserResponseDto(
            1L,
            "User#1",
            "user1@server.com"
    );

    @Test
    void userResponseDtoSerializationTest() throws IOException {
        JacksonTester<UserResponseDto> tester = new JacksonTester<>(
                UserResponseDto.class,
                ResolvableType.forClass(UserResponseDto.class),
                objectMapper
        );
        JsonContent<UserResponseDto> content = tester.write(userResponseDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);

        assertThat(content).hasJsonPath("$.name");
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo(userResponseDto.getName());

        assertThat(content).hasJsonPath("$.email");
        assertThat(content).extractingJsonPathStringValue("$.email").isEqualTo(userResponseDto.getEmail());

    }

}
