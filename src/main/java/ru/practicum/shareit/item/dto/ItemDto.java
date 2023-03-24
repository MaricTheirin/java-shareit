package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.service.validation.Create;
import ru.practicum.shareit.service.validation.Update;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {

    private long id;

    @NotBlank(groups = Create.class, message = "Невозможно сохранить предмет с пустым наименованием")
    private String name;

    @NotBlank(groups = {Create.class, Update.class}, message = "Невозможно сохранить предмет с пустым описанием")
    private String description;

    @NotNull(groups = Create.class, message = "Невозможно сохранить предмет без указания его доступности")
    private Boolean available;

}
