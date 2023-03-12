package ru.practicum.shareit.user.dto;

import lombok.Data;
import javax.validation.constraints.Email;

@Data
public class UserDto {

    private final Long id;
    private final String name;
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private final String email;

}
