package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;
import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT new ru.practicum.shareit.item.model.Comment(c.id, c.text, c.item, c.author, c.created) " +
    "FROM Comment AS c " +
    "WHERE c.item.id in (:itemIds) " +
    "ORDER BY c.created ASC")
    List<Comment> findAllByItemIdIn(Set<Long> itemIds);

}
