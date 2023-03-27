package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.service.validation.Create;
import ru.practicum.shareit.service.validation.Update;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {

    @NotNull(groups = Update.class, message = "Для обновления комментария требуется его ID")
    private Long id;

    @Length(min = 2, groups = {Create.class, Update.class}, message = "Длина комментария должна превышать 2 символа")
    private String text;

    @Null(groups = {Create.class, Update.class})
    private Long itemId;

    @Null(groups = {Create.class, Update.class})
    private String authorName;

    @Null(groups = {Create.class, Update.class})
    private LocalDateTime created;

}
