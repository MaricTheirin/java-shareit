package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.exception.BookingException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.service.exception.AccessException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    private final User user1 = new User(1, "User#1", "User1@server.com");

    private final ItemDto itemDto1 = new ItemDto(
            1L,
            null,
            "Item#1_name",
            "Item#1_desc",
            true
    );

    private final Item item1 = new Item(
            1L,
            null,
            user1,
            "Item#1_name",
            "Item#1_desc",
            true
    );

    private final ItemResponseDto itemResponseDto1 = new ItemResponseDto(
            1L,
            null,
            "Item#1_name",
            "Item#1_desc",
            true,
            new BookingShortResponseDto(1L, 1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(1)),
            new BookingShortResponseDto(1L, 1L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(4)),
            Collections.emptyList()
    );

    private final BookingDto bookingDto = new BookingDto(
            1L,
            1L,
            LocalDateTime.now().plusDays(1).withNano(0),
            LocalDateTime.now().plusDays(2).withNano(0)
    );

    private final BookingDto bookingDtoIncorrectItemId = new BookingDto(
            99L,
            99L,
            null,
            null
    );

    private final BookingDto bookingDtoIncorrectStart = new BookingDto(
            97L,
            97L,
            LocalDateTime.now(),
            LocalDateTime.now().minusDays(1)
    );

    private final Booking booking = BookingDtoMapper.mapDtoToBooking(bookingDto, item1, user1);

    private final Booking bookingInRejectedStatus = new Booking(
            1L,
            item1,
            user1,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(1),
            BookingStatus.REJECTED
    );

    private final BookingResponseDto bookingResponseDto = BookingDtoMapper.mapBookingToResponseDto(booking);


    @Test
    void createTest() {
        Mockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item1));

        Mockito.when(itemRepository.existsItemByIdAndOwnerId(eq(99L), eq(99L))).thenReturn(true);
        assertThrows(BookingNotFoundException.class, () -> bookingService.create(99L, bookingDtoIncorrectItemId));

        Mockito.when(itemRepository.existsItemByIdAndOwnerId(eq(99L), eq(98L))).thenReturn(false);
        Mockito.when(itemRepository.existsItemByIdAndAvailableIsTrue(eq(99L))).thenReturn(false);
        assertThrows(ItemNotAvailableException.class, () -> bookingService.create(98L, bookingDtoIncorrectItemId));

        Mockito.when(itemRepository.existsItemByIdAndOwnerId(eq(97L), eq(97L))).thenReturn(false);
        Mockito.when(itemRepository.existsItemByIdAndAvailableIsTrue(eq(97L))).thenReturn(true);
        assertThrows(BookingException.class, () -> bookingService.create(97L, bookingDtoIncorrectStart));

        Mockito.when(itemRepository.existsItemByIdAndOwnerId(eq(1L), eq(1L))).thenReturn(false);
        Mockito.when(itemRepository.existsItemByIdAndAvailableIsTrue(eq(1L))).thenReturn(true);
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(booking);
        BookingResponseDto createdResponseDto = bookingService.create(1L, bookingDto);
        assertEquals(bookingResponseDto.getId(), createdResponseDto.getId());
        assertEquals(bookingResponseDto.getItem(), createdResponseDto.getItem());
        assertEquals(bookingResponseDto.getBooker().getId(), createdResponseDto.getBooker().getId());
        assertEquals(bookingResponseDto.getStart(), createdResponseDto.getStart());
        assertEquals(bookingResponseDto.getEnd(), createdResponseDto.getEnd());
        assertEquals(bookingResponseDto.getStatus(), createdResponseDto.getStatus());
    }

    @Test
    void readTest() {
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        assertThrows(AccessException.class, () -> bookingService.read(99L, 1L));

        BookingResponseDto createdResponseDto = bookingService.read(1L, 1L);
        assertEquals(bookingResponseDto.getId(), createdResponseDto.getId());
        assertEquals(bookingResponseDto.getItem().getId(), createdResponseDto.getItem().getId());
        assertEquals(bookingResponseDto.getStatus(), createdResponseDto.getStatus());
        assertEquals(bookingResponseDto.getBooker().getId(), createdResponseDto.getBooker().getId());
        assertEquals(bookingResponseDto.getStart(), createdResponseDto.getStart());
        assertEquals(bookingResponseDto.getEnd(), createdResponseDto.getEnd());
    }

    @Test
    void reviewTest() {
        Mockito.when(bookingRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class, () -> bookingService.review(1L, 99L, true));

        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingInRejectedStatus));
        assertThrows(BookingException.class, () -> bookingService.review(1L, 1L, true));

        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Mockito.when(userRepository.existsById(99L)).thenReturn(true);
        assertThrows(BookingNotFoundException.class, () -> bookingService.review(99L, 1L, true));

        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        BookingResponseDto createdResponseDto = bookingService.review(1L, 1L, true);
        assertEquals(booking.getId(), createdResponseDto.getId());
        assertEquals(booking.getBooker().getId(), createdResponseDto.getBooker().getId());
        assertEquals(booking.getStart(), createdResponseDto.getStart());
        assertEquals(booking.getEnd(), createdResponseDto.getEnd());
        assertEquals(BookingStatus.APPROVED, createdResponseDto.getStatus());
    }

    @Test
    void findOwnBookings() {
        Mockito.when(userRepository.existsById(user1.getId())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByUserBookingsAndFilterByStateOrderByIdAsc(
                    eq(user1.getId()), eq(BookingState.WAITING), anyLong(), anyLong())
                ).thenReturn(List.of(booking));

        List<BookingResponseDto> createdBookings =
                bookingService.findOwnBookings(user1.getId(), BookingState.WAITING, 0, 20);
        assertEquals(1, createdBookings.size());
        assertEquals(booking.getId(), createdBookings.get(0).getId());
        assertEquals(booking.getItem().getId(), createdBookings.get(0).getItem().getId());
        assertEquals(booking.getStatus(), createdBookings.get(0).getStatus());
        assertEquals(booking.getStart(), createdBookings.get(0).getStart());
        assertEquals(booking.getEnd(), createdBookings.get(0).getEnd());

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.findOwnBookings(user1.getId(), BookingState.WAITING, -1L, 20L));
        assertThrows(IllegalArgumentException.class, () ->
                bookingService.findOwnBookings(user1.getId(), BookingState.WAITING, 1L, 0L));
    }

    @Test
    void findOwnItemsBookingsTest() {
        Mockito.when(userRepository.existsById(user1.getId())).thenReturn(true);
        Mockito.when(bookingRepository.findAllByUserItemsAndFilterByState(
                eq(user1.getId()), eq(BookingState.WAITING), anyLong(), anyLong())
        ).thenReturn(List.of(booking));

        List<BookingResponseDto> createdBookings =
                bookingService.findOwnItemsBookings(user1.getId(), BookingState.WAITING, 0, 20);
        assertEquals(1, createdBookings.size());
        assertEquals(booking.getId(), createdBookings.get(0).getId());
        assertEquals(booking.getItem().getId(), createdBookings.get(0).getItem().getId());
        assertEquals(booking.getStatus(), createdBookings.get(0).getStatus());
        assertEquals(booking.getStart(), createdBookings.get(0).getStart());
        assertEquals(booking.getEnd(), createdBookings.get(0).getEnd());

        Mockito.when(userRepository.existsById(eq(Long.MAX_VALUE))).thenReturn(false);
        assertThrows(UserNotFoundException.class, () ->
                bookingService.findOwnItemsBookings(Long.MAX_VALUE, BookingState.WAITING, 0L, 0L));

    }

}
