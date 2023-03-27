package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Component
public class CommentDtoMapper {

    private final UserRepository userRepository;

    @Autowired
    public CommentDtoMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Comment mapDtoToComment(CommentDto commentDto, Long authorId) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItemId(),
                authorId,
                commentDto.getCreated()
        );
    }

    public CommentDto mapCommentToDto(Comment comment) {
        String authorName = userRepository.getReferenceById(comment.getAuthorId()).getName();
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItemId(),
                authorName,
                comment.getCreated()
        );
    }


}
