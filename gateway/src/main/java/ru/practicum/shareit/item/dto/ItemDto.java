package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.service.validation.Create;
import ru.practicum.shareit.service.validation.Update;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {

    private final long id;

    private final Long requestId;

    @NotBlank(groups = Create.class, message = "Невозможно сохранить предмет с пустым наименованием")
    private final String name;

    @NotBlank(groups = {Create.class, Update.class}, message = "Невозможно сохранить предмет с пустым описанием")
    @Length(groups = {Create.class}, min = 3, message = "Короткое описание")
    private final String description;

    @NotNull(groups = Create.class, message = "Невозможно сохранить предмет без указания его доступности")
    private final Boolean available;

}
