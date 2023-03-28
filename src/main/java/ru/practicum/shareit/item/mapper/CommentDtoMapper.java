package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;

@Slf4j
@Component
public class CommentDtoMapper {

    public Comment mapDtoToComment(CommentDto commentDto, User author, Item item) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                item,
                author,
                LocalDateTime.now()
        );
    }

    public CommentResponseDto mapCommentToResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }


}
