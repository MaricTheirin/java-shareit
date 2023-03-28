package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserResponseDto {

    private long id;

    private String name;

    private String email;

}
