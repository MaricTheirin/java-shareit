package ru.practicum.shareit.booking;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@DataJpaTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BookingRepositoryTest {


    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private final LocalDateTime now = LocalDateTime.now().withNano(0);
    private final User user = new User(1L, "User#1", "user@server.com");
    private final Item item = new Item(1L, user, "Item#1", "Item#1_desc", true);
    private final Booking itemLastBooking =
            new Booking(0, item, user, now.minusDays(2), now.minusDays(1), BookingStatus.APPROVED);
    private final Booking itemNextBooking =
            new Booking(0, item, user, now.plusDays(1), now.plusDays(2), BookingStatus.APPROVED);

    @BeforeAll
    void setUp() {
        userRepository.saveAndFlush(user);
        itemRepository.saveAndFlush(item);
        bookingRepository.saveAllAndFlush(List.of(itemLastBooking, itemNextBooking));
    }

    @Test
    void getLastBookingsForItemsTest() {
        List<BookingShort> lastBookings =
                bookingRepository.getLastBookingsForItems(Set.of(item.getId()), itemLastBooking.getStatus());

        Assertions.assertEquals(1, lastBookings.size(), "Должно быть не более 1 результата");
        Assertions.assertEquals(1, lastBookings.get(0).getId(), "Должно быть не более 1 результата");
    }

    @Test
    void getNextBookingsForItemsTest() {
        List<BookingShort> nextBookings =
                bookingRepository.getNextBookingsForItems(Set.of(item.getId()), itemNextBooking.getStatus());

        Assertions.assertEquals(1, nextBookings.size(), "Должно быть не более 1 результата");
        Assertions.assertEquals(2, nextBookings.get(0).getId(), "Должно быть не более 1 результата");

    }

    @Test
    void existsBookingByItemIdAndBookerIdAndStatusAndEndIsBeforeTest() {
        Assertions.assertTrue(bookingRepository.existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
                item.getId(),
                user.getId(),
                BookingStatus.APPROVED,
                LocalDateTime.now())
        );
        Assertions.assertFalse(bookingRepository.existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
                Long.MAX_VALUE,
                user.getId(),
                BookingStatus.APPROVED,
                LocalDateTime.now())
        );
        Assertions.assertFalse(bookingRepository.existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
                item.getId(),
                Long.MAX_VALUE,
                BookingStatus.APPROVED,
                LocalDateTime.now())
        );
        Assertions.assertFalse(bookingRepository.existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
                item.getId(),
                user.getId(),
                BookingStatus.WAITING,
                LocalDateTime.now())
        );
    }

}

