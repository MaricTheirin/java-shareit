package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.service.validation.Create;
import ru.practicum.shareit.service.validation.Update;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {

    private final Long id;

    @NotBlank(groups = Create.class, message = "Невозможно сохранить пользователя с пустым именем")
    private final String name;

    @NotEmpty(groups = Create.class, message = "Невозможно сохранить пользователя с пустым email")
    @Email(
            regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            groups = {Create.class, Update.class},
            message = "Email задан некорректно"
    )
    private final String email;

}
