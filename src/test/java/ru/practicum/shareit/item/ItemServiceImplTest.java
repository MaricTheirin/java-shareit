package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.mapper.CommentDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @InjectMocks
    ItemServiceImpl itemService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    BookingRepository bookingRepository;

    private final User user1 = new User(1, "User#1", "User1@server.com");

    private final ItemDto itemDto1 = new ItemDto(
            1L,
            null,
            "Item#1_name",
            "Item#1_desc",
            true
    );

    private final ItemDto itemDto1Updated = new ItemDto(
            1L,
            null,
            "Item#1_name_updated",
            "Item#1_desc_updated",
            false
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

    private final CommentDto commentDto = new CommentDto(
            1L,
            "Comment#1_Text"
    );

    private final Comment comment = CommentDtoMapper.mapDtoToComment(commentDto, user1, item1);

    private final CommentResponseDto commentResponseDto = CommentDtoMapper.mapCommentToResponseDto(comment);

    @Test
    void createTest() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(item1);

        ItemResponseDto createdItem = itemService.create(1L, itemDto1);
        assertNotNull(createdItem);
        assertEquals(itemDto1.getId(), createdItem.getId());
        assertEquals(itemDto1.getName(), createdItem.getName());
        assertEquals(itemDto1.getDescription(), createdItem.getDescription());
        assertEquals(itemDto1.getAvailable(), createdItem.getAvailable());
        assertEquals(itemDto1.getRequestId(), createdItem.getRequestId());
    }

    @Test
    void readTest() {
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));

        ItemResponseDto createdItem = itemService.read(1L, 1L);
        assertNotNull(createdItem);
        assertEquals(itemDto1.getId(), createdItem.getId());
        assertEquals(itemDto1.getName(), createdItem.getName());
        assertEquals(itemDto1.getDescription(), createdItem.getDescription());
        assertEquals(itemDto1.getAvailable(), createdItem.getAvailable());
        assertEquals(itemDto1.getRequestId(), createdItem.getRequestId());
    }

    @Test
    void updateTest() {
        Mockito.when(itemRepository.getItemByIdEqualsAndOwnerIdEquals(anyLong(), anyLong()))
                .thenReturn(Optional.of(item1));

        ItemResponseDto updatedItem = itemService.update(1L, 1L, itemDto1Updated);
        assertNotNull(updatedItem);
        assertEquals(itemDto1Updated.getId(), updatedItem.getId());
        assertEquals(itemDto1Updated.getName(), updatedItem.getName());
        assertEquals(itemDto1Updated.getDescription(), updatedItem.getDescription());
        assertEquals(itemDto1Updated.getAvailable(), updatedItem.getAvailable());
        assertEquals(itemDto1Updated.getRequestId(), updatedItem.getRequestId());
    }

    @Test
    void deleteTest() {
        Mockito.when(itemRepository.getItemByIdEqualsAndOwnerIdEquals(anyLong(), anyLong()))
                .thenReturn(Optional.of(item1));

        ItemResponseDto deletedItem = itemService.delete(1L, 1L);
        assertNotNull(deletedItem);
        assertEquals(itemDto1.getId(), deletedItem.getId());
        assertEquals(itemDto1.getName(), deletedItem.getName());
        assertEquals(itemDto1.getDescription(), deletedItem.getDescription());
        assertEquals(itemDto1.getAvailable(), deletedItem.getAvailable());
        assertEquals(itemDto1.getRequestId(), deletedItem.getRequestId());
    }

    @Test
    void findAvailableItemsBySearchQueryTest() {
        List<ItemResponseDto> resultList;

        resultList = itemService.findAvailableItemsBySearchQuery("");
        assertEquals(0, resultList.size());

        Mockito.when(itemRepository.findAllAvailableAndContainingQueryIgnoreCase(anyString()))
                .thenReturn(List.of(item1));

        resultList = itemService.findAvailableItemsBySearchQuery("query");
        assertEquals(1, resultList.size());
        assertNull(resultList.get(0).getLastBooking());
        assertNull(resultList.get(0).getNextBooking());
        assertEquals(itemDto1.getId(), resultList.get(0).getId());
        assertEquals(itemDto1.getName(), resultList.get(0).getName());
        assertEquals(itemDto1.getDescription(), resultList.get(0).getDescription());
        assertEquals(itemDto1.getAvailable(), resultList.get(0).getAvailable());
        assertEquals(itemDto1.getRequestId(), resultList.get(0).getRequestId());
    }

    @Test
    void findAllTest() {
        Mockito.when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong()))
                .thenReturn(Optional.of(List.of(item1)));

        List<ItemResponseDto> resultList = itemService.findAll(1L);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(itemDto1.getId(), resultList.get(0).getId());
        assertEquals(itemDto1.getName(), resultList.get(0).getName());
        assertEquals(itemDto1.getDescription(), resultList.get(0).getDescription());
        assertEquals(itemDto1.getAvailable(), resultList.get(0).getAvailable());
        assertEquals(itemDto1.getRequestId(), resultList.get(0).getRequestId());
    }

    @Test
    void createCommentTest() {
        Mockito.when(bookingRepository.existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
                    eq(99L), anyLong(), eq(BookingStatus.APPROVED), any(LocalDateTime.class)
        )).thenReturn(false);

        assertThrows(ItemNotAvailableException.class, () -> itemService.createComment(1L, 99L, commentDto));

        Mockito.when(bookingRepository.existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
                eq(1L), anyLong(), eq(BookingStatus.APPROVED), any(LocalDateTime.class)
        )).thenReturn(true);

        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(userRepository.getReferenceById(anyLong())).thenReturn(user1);
        Mockito.when(commentRepository.saveAndFlush(any(Comment.class))).thenReturn(comment);

        CommentResponseDto responseDto = itemService.createComment(1L, 1L, commentDto);
        assertEquals(commentResponseDto.getId(), responseDto.getId());
        assertEquals(commentResponseDto.getItemId(), responseDto.getItemId());
        assertEquals(commentResponseDto.getCreated(), responseDto.getCreated());
        assertEquals(commentResponseDto.getText(), responseDto.getText());
        assertEquals(commentResponseDto.getAuthorName(), responseDto.getAuthorName());

    }

}
