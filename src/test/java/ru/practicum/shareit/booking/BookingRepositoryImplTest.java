package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.BookingRepositoryImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryImplTest {

    private final BookingRepository bookingRepository;
    private final BookingRepositoryImpl bookingRepositoryImpl;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private final LocalDateTime now = LocalDateTime.now().withNano(0);
    private final User user = new User(1L, "User#1", "user@server.com");
    private final User user2 = new User(2L, "User#2", "user2@server.com");
    private final Item item = new Item(1L, null, user, "Item#1", "Item#1_desc", true);
    private final Booking itemLastBooking =
            new Booking(0, item, user2, now.minusDays(2), now.minusDays(1), BookingStatus.APPROVED);
    private final Booking itemLastBookingRejected =
            new Booking(0, item, user2, now.minusDays(2), now.minusDays(1), BookingStatus.REJECTED);
    private final Booking itemNextBooking =
            new Booking(0, item, user2, now.plusDays(2), now.plusDays(1), BookingStatus.APPROVED);
    private final Booking itemNextBookingRejected =
            new Booking(0, item, user2, now.plusDays(1), now.plusDays(2), BookingStatus.REJECTED);

    @BeforeAll
    void setUp() {
        userRepository.saveAllAndFlush(List.of(user, user2));
        itemRepository.saveAndFlush(item);
        bookingRepository.saveAllAndFlush(List.of(itemLastBooking, itemNextBooking));
    }

    @Test
    void findAllByUserBookingsAndFilterByState() {
        List<Booking> user1Bookings =
                bookingRepositoryImpl.findAllByUserBookingsAndFilterByStateOrderByIdAsc(user.getId(), BookingState.PAST, 0, 99);

        List<Booking> user2Bookings =
                bookingRepositoryImpl.findAllByUserBookingsAndFilterByStateOrderByIdAsc(user2.getId(), BookingState.PAST, 0, 99);

        assertEquals(0, user1Bookings.size());
        assertEquals(1, user2Bookings.size());
        assertEquals(itemLastBooking.getId(), user2Bookings.get(0).getId());
        assertEquals(itemLastBooking.getItem().getId(), user2Bookings.get(0).getItem().getId());
        assertEquals(itemLastBooking.getStatus(), user2Bookings.get(0).getStatus());
        assertEquals(itemLastBooking.getBooker().getId(), user2Bookings.get(0).getBooker().getId());
        assertEquals(itemLastBooking.getStart(), user2Bookings.get(0).getStart());
        assertEquals(itemLastBooking.getEnd(), user2Bookings.get(0).getEnd());
    }

    @Test
    void findAllByUserItemsAndFilterByStateTest() {
        List<Booking> user1ItemsBookings =
                bookingRepositoryImpl.findAllByUserItemsAndFilterByState(user.getId(), BookingState.PAST, 0, 99);

        List<Booking> user2ItemsBookings =
                bookingRepositoryImpl.findAllByUserItemsAndFilterByState(user2.getId(), BookingState.PAST, 0, 99);

        assertEquals(0, user2ItemsBookings.size());
        assertEquals(1, user1ItemsBookings.size());

        assertEquals(itemLastBooking.getId(), user1ItemsBookings.get(0).getId());
        assertEquals(itemLastBooking.getItem().getId(), user1ItemsBookings.get(0).getItem().getId());
        assertEquals(itemLastBooking.getStatus(), user1ItemsBookings.get(0).getStatus());
        assertEquals(itemLastBooking.getBooker().getId(), user1ItemsBookings.get(0).getBooker().getId());
        assertEquals(itemLastBooking.getStart(), user1ItemsBookings.get(0).getStart());
        assertEquals(itemLastBooking.getEnd(), user1ItemsBookings.get(0).getEnd());
    }


}
