package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final LocalDateTime now = LocalDateTime.now().withNano(0);
    private User user;
    private User user2;
    private Item item;
    private Item item2;
    private Item item3;

    @BeforeAll
    void setUp() {
        user = new User(1L, "User#1--", "user@server.com");
        user2 = new User(2L, "User#2", "user2@server.com");
        item = new Item(1L, null, user, "Item#1", "Item#1_OfUser#1", true);
        item2 = new Item(2L, null, user, "Item#2", "Item#2_OfUser#1", false);
        item3 = new Item(3L, null, user2, "Item#3", "Item#3_OfUser#2", true);
        userRepository.saveAllAndFlush(List.of(user, user2));
        itemRepository.saveAllAndFlush(List.of(item, item2, item3));
    }

    @Test
    void findAllByOwnerIdOrderByIdAscTest() {
        List<Item> itemsFoundByOwner = itemRepository.findAllByOwnerIdOrderByIdAsc(user.getId());
        assertEquals(2, itemsFoundByOwner.size(), "Пользователю должно принадлежать 2 предмета");
        assertEquals(1L, itemsFoundByOwner.get(0).getId(), "Массив предметов не отсортирован по ID");
    }

    @Test
    void findAllAvailableAndContainingQueryIgnoreCaseTest() {
        List<Item> foundItems = itemRepository.findAllAvailableAndContainingQueryIgnoreCase("ofuser");
        assertEquals(2, foundItems.size(), "Условию поиска должно соответствовать 2 предмета");
    }

    @Test
    void existsItemByIdAndAvailableIsTrueTest() {
        boolean itemExistAndAvailable = itemRepository.existsItemByIdAndAvailableIsTrue(item.getId());
        assertTrue(itemExistAndAvailable, "Предмет должен существовать и быть доступным");

        itemExistAndAvailable = itemRepository.existsItemByIdAndAvailableIsTrue(item2.getId());
        assertFalse(itemExistAndAvailable, "Предмет недоступен и не должен выводиться");
    }

    @Test
    void existsItemByIdAndOwnerIdTest() {
        boolean itemExistAndBelongsToUser = itemRepository.existsItemByIdAndOwnerId(item.getId(), user.getId());
        assertTrue(itemExistAndBelongsToUser, "Предмет должен существовать и быть доступным");

        itemExistAndBelongsToUser = itemRepository.existsItemByIdAndOwnerId(item.getId(), user2.getId());
        assertFalse(itemExistAndBelongsToUser, "Предмет не принадлежит пользователю и не должен выводиться");
    }


}
