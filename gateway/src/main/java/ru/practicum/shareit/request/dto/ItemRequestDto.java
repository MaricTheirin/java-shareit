package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.service.validation.Create;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    @Length(min = 2, max = 512, message = "Длина описания не входит в разрешённый диапазон", groups = {Create.class})
    @NotBlank(message = "Описание не может быть пустым", groups = {Create.class})
    private String description;

}
