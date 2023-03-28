package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;

@Slf4j
@Component
public class CommentDtoMapper {

    public Comment mapDtoToComment(CommentDto commentDto, Long authorId, Long itemId) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                itemId,
                authorId,
                LocalDateTime.now()
        );
    }

    public CommentDto mapCommentToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText()
        );
    }

    public CommentResponseDto mapCommentToResponseDto(Comment comment) {
        return null;
    }


}
