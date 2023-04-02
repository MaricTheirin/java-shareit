package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class ItemRequestServiceImplTest {

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Mock
    ItemRequestRepository itemRequestRepository;

    @Mock
    UserRepository userRepository;

    private final UserDto userDto = new UserDto(1L, "User#1", "user1@server.com");
    private final UserDto updatedUserDto =
            new UserDto(1L, "upd_".concat(userDto.getName()), "upd_".concat(userDto.getEmail()));
    private final User user = UserDtoMapper.mapDtoToUser(userDto);
    private final User updatedUser = UserDtoMapper.mapDtoToUser(updatedUserDto);
    private final UserResponseDto userResponseDto = UserDtoMapper.mapUserToResponseDto(user);
    private final UserResponseDto updatedUserResponseDto = UserDtoMapper.mapUserToResponseDto(updatedUser);
    private final ItemRequestDto itemRequestDto = new ItemRequestDto("Request#1_description");
    private final ItemRequest itemRequest = ItemRequestDtoMapper.mapDtoToItemRequest(user, itemRequestDto);
    private final ItemRequestResponseDto itemRequestResponseDto =
            ItemRequestDtoMapper.mapItemRequestToResponseDto(itemRequest);
    private final Pageable pageable = PageRequest.of(0, 20);

    @BeforeEach
    void setUp() {
        itemRequest.setId(1L);
        itemRequestResponseDto.setId(itemRequest.getId());
        user.setId(1L);
    }

    @Test
    void createTest() {
        Mockito.when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.of(user));
        Mockito.when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestResponseDto createdItemRequestResponseDto = itemRequestService.create(user.getId(), itemRequestDto);
        assertEquals(itemRequestResponseDto.getId(), createdItemRequestResponseDto.getId());
        assertEquals(itemRequestResponseDto.getItems().size(), createdItemRequestResponseDto.getItems().size());
        assertEquals(itemRequestResponseDto.getCreated(), createdItemRequestResponseDto.getCreated());
        assertEquals(itemRequestResponseDto.getDescription(), createdItemRequestResponseDto.getDescription());
    }

    @Test
    void readTest() {
        Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);
        Mockito.when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));

        ItemRequestResponseDto foundItemRequestResponseDto = itemRequestService.read(1L, 1L);
        assertEquals(itemRequestResponseDto.getId(), foundItemRequestResponseDto.getId());
        assertEquals(itemRequestResponseDto.getItems().size(), foundItemRequestResponseDto.getItems().size());
        assertEquals(itemRequestResponseDto.getCreated(), foundItemRequestResponseDto.getCreated());
        assertEquals(itemRequestResponseDto.getDescription(), foundItemRequestResponseDto.getDescription());

        Mockito.when(userRepository.existsById(eq(user.getId()))).thenReturn(true);
        Mockito.when(itemRequestRepository.findById(eq(Long.MAX_VALUE))).thenReturn(Optional.empty());
        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.read(user.getId(), Long.MAX_VALUE));

        Mockito.when(userRepository.existsById(eq(Long.MAX_VALUE))).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> itemRequestService.read(Long.MAX_VALUE, itemRequest.getId()));
    }

    @Test
    void readUserRequestsTest() {
        Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);
        Mockito.when(itemRequestRepository.findAllByUserIdOrderByCreatedDesc(itemRequest.getId()))
                .thenReturn(Optional.of(List.of(itemRequest)));

        List<ItemRequestResponseDto> foundItemRequests = itemRequestService.readUserRequests(user.getId());
        assertEquals(1, foundItemRequests.size());
        assertEquals(itemRequestResponseDto.getId(), foundItemRequests.get(0).getId());
        assertEquals(itemRequestResponseDto.getItems().size(), foundItemRequests.get(0).getItems().size());
        assertEquals(itemRequestResponseDto.getDescription(), foundItemRequests.get(0).getDescription());
        assertEquals(itemRequestResponseDto.getCreated(), foundItemRequests.get(0).getCreated());

        Mockito.when(userRepository.existsById(eq(Long.MAX_VALUE))).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> itemRequestService.readUserRequests(Long.MAX_VALUE));
    }

    @Test
    void readAllUsersRequestsTest() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(itemRequestRepository.findAllByUserIdNotOrderByCreatedDesc(anyLong(), eq(pageable)))
                .thenReturn(Optional.of(List.of(itemRequest)));

        List<ItemRequestResponseDto> foundItemRequests = itemRequestService.readAllUsersRequests(user.getId() + 1, 0, 20);
        assertEquals(1, foundItemRequests.size());
        assertEquals(itemRequestResponseDto.getId(), foundItemRequests.get(0).getId());
        assertEquals(itemRequestResponseDto.getItems().size(), foundItemRequests.get(0).getItems().size());
        assertEquals(itemRequestResponseDto.getDescription(), foundItemRequests.get(0).getDescription());
        assertEquals(itemRequestResponseDto.getCreated(), foundItemRequests.get(0).getCreated());

        Mockito.when(userRepository.existsById(eq(Long.MAX_VALUE))).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> itemRequestService.readUserRequests(Long.MAX_VALUE));
    }


}

