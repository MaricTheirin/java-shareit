package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Slf4j
@Component
public class CommentDtoMapper {

    private static final String OBJECT_MAPPED_MESSAGE = "Выполнено преобразование объекта из {} в {}";

    public Comment mapDtoToComment(CommentDto commentDto, User author, Item item) {
        Comment mappedComment = new Comment(
                commentDto.getId(),
                commentDto.getText(),
                item,
                author,
                LocalDateTime.now()
        );
        log.trace(OBJECT_MAPPED_MESSAGE, item, mappedComment);
        return mappedComment;
    }

    public CommentResponseDto mapCommentToResponseDto(Comment comment) {
        CommentResponseDto mappedResponseDto = new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
        log.trace(OBJECT_MAPPED_MESSAGE, comment, mappedResponseDto);
        return mappedResponseDto;
    }


}
