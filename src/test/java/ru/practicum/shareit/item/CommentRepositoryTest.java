package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentRepositoryTest {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User user;
    private Item item;
    private Item item2;
    private Comment comment;
    private Comment comment2;

    @BeforeAll
    void setUp() {
        user = new User(1L, "User#1", "user@server.com");
        item = new Item(1L, null, user, "Item#1", "Item#1_OfUser#1", true);
        item2 = new Item(2L, null, user, "Item#2", "Item#2_OfUser#1", true);
        comment = new Comment(1L, "Comment#1", item, user, null);
        comment2 = new Comment(2L, "Comment#2", item2, user, null);

        userRepository.saveAndFlush(user);
        itemRepository.saveAllAndFlush(List.of(item, item2));
        commentRepository.saveAllAndFlush(List.of(comment, comment2));
    }

    @Test
    void findAllByItemIdIn() {
        List<Comment> comments = commentRepository.findAllByItemIdIn(Set.of(item2.getId()));
        assertEquals(1, comments.size());
        assertEquals(2, comments.get(0).getId());
    }

}
