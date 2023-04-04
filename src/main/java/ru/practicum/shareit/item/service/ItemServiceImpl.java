package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.*;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.*;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ItemResponseDto create(Long userId, ItemDto itemDto) {
        log.debug("Для пользователя с id = {} добавляется новый объект: {}", userId, itemDto);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item savedItem = itemRepository.saveAndFlush(ItemDtoMapper.mapDtoToItem(itemDto, user));
        log.trace("Сохранённый предмет: {}", savedItem);
        return ItemDtoMapper.mapItemToResponseDto(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto read(Long userId, Long itemId) {
        log.debug("Пользователь с id = {} запросил объект с id = {}", userId, itemId);

        Item resultItem = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        log.trace("Найден объект {}", resultItem);

        return mapItemToResponseDto(resultItem, userId);
    }

    @Override
    @Transactional
    public ItemResponseDto update(Long userId, Long itemId, ItemDto itemDto) {
        log.debug("Запрошено обновление объекта с id = {} ({}) для пользователя с id = {}", itemId, itemDto, userId);

        Item savedItem =
                itemRepository.getItemByIdEqualsAndOwnerIdEquals(itemId, userId);
        updateItemFields(userId, savedItem, itemDto);
        log.trace("Сохранённый предмет: {}", savedItem);
        return mapItemToResponseDto(savedItem, userId);
    }

    @Override
    @Transactional
    public ItemResponseDto delete(Long userId, Long itemId) {
        log.debug("Для пользователя с id = {} удаляется предмет с id = {}", userId, itemId);

        Item itemToDelete = itemRepository
                .getItemByIdEqualsAndOwnerIdEquals(itemId, userId);
        itemRepository.delete(itemToDelete);
        log.trace("Выполнено удаление предмета: {}", itemToDelete);
        return mapItemToResponseDto(itemToDelete, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> findAvailableItemsBySearchQuery(String searchQuery) {
        log.debug("Запрошен поиск всех доступных вещей, содержащих '{}'", searchQuery);
        if (searchQuery.isBlank()) {
            return Collections.emptyList();
        }

        List<Item> foundItems = itemRepository.findAllAvailableAndContainingQueryIgnoreCase(searchQuery);
        log.trace("Найденные вещи: {}", foundItems);
        return foundItems.stream().map(item -> mapItemToResponseDto(item, 0)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> findAll(Long userId) {
        log.debug("Для пользователя с id = {} запрошен список всех предметов", userId);

        List<Item> allUserItems = itemRepository.findAllByOwnerIdOrderByIdAsc(userId);
        log.trace("Получен массив предметов: {}", allUserItems);
        return new ArrayList<>(mapItemsToResponseDto(allUserItems).values());

    }

    @Override
    @Transactional
    public CommentResponseDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        log.debug(
                "Пользователь с id = {} запросил добавление к предмету с id = {} комментария {}",
                userId, itemId, commentDto
        );
        checkIfItemWasBookedByUser(userId, itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        User commentAuthor = userRepository.getReferenceById(userId);

        Comment savedComment = commentRepository.saveAndFlush(
                CommentDtoMapper.mapDtoToComment(commentDto, commentAuthor, item)
        );
        log.trace("Результат сохранения комментария: {}", savedComment);
        return CommentDtoMapper.mapCommentToResponseDto(savedComment);
    }

    private void checkIfItemWasBookedByUser(Long userId, Long itemId) {
        if (!bookingRepository.existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now())
        ) {
            throw new ItemNotAvailableException("Оставить комментарий можно только успешной аренды");
        }
    }

    private void updateItemFields(Long ownerUserId, Item item, ItemDto updatedItemDto) {
        log.debug("Обновление вещи {} данными из DTO: {}", item, updatedItemDto);

        if (updatedItemDto.getName() != null && !updatedItemDto.getName().isBlank()) {
            log.debug(
                    "У вещи {} изменёно наименование. Старое значение - {}, новое значение - {}",
                    updatedItemDto,
                    item.getName(),
                    updatedItemDto.getName()
            );
            item.setName(updatedItemDto.getName());
        }

        if (updatedItemDto.getDescription() != null && !updatedItemDto.getDescription().isBlank()) {
            log.debug(
                    "У вещи {} изменёно описание. Старое значение - {}, новое значение - {}",
                    item,
                    item.getDescription(),
                    updatedItemDto.getDescription()
            );
            item.setDescription(updatedItemDto.getDescription());
        }

        if (updatedItemDto.getAvailable() != null) {
            log.debug(
                    "У вещи {} изменена доступность. Старое значение - {}, новое значение - {}",
                    item,
                    item.isAvailable(),
                    updatedItemDto.getAvailable()

            );
            item.setAvailable(updatedItemDto.getAvailable());
        }
        log.trace("Обновлённая вещь: {}", item);
    }

    private ItemResponseDto mapItemToResponseDto(Item item, long requestedUserId) {
        ItemResponseDto responseDto = ItemDtoMapper.mapItemToResponseDto(item);
        if (requestedUserId == item.getOwner().getId()) {
            addLastAndNextBookingForItem(responseDto);
        }

        addCommentsForItem(responseDto);
        return responseDto;
    }

    private Map<Long, ItemResponseDto> mapItemsToResponseDto(List<Item> items) {
        Map<Long, ItemResponseDto> mappedItems =  items.stream()
                .map(ItemDtoMapper::mapItemToResponseDto)
                .collect(Collectors.toMap(ItemResponseDto::getId, Function.identity()));

        addLastAndNextBookingForItems(mappedItems);
        addCommentsForItems(mappedItems);
        return mappedItems;
    }

    private void addLastAndNextBookingForItem(ItemResponseDto item) {
        addLastAndNextBookingForItems(Map.of(item.getId(), item));
    }

    private void addCommentsForItem(ItemResponseDto item) {
        addCommentsForItems(Map.of(item.getId(), item));
    }

    private void addLastAndNextBookingForItems(Map<Long, ItemResponseDto> items) {
        BookingStatus statusToInclude = BookingStatus.APPROVED;

        Map<Long, BookingShort> lastBookings = bookingRepository
                .getLastBookingsForItems(items.keySet(), statusToInclude)
                .stream()
                .collect(Collectors.toMap(BookingShort::getItemId, Function.identity(), (o, n) -> o));

        Map<Long, BookingShort> nextBookings = bookingRepository
                .getNextBookingsForItems(items.keySet(), statusToInclude)
                .stream()
                .collect(Collectors.toMap(BookingShort::getItemId, Function.identity(), (o, n) -> o));

        items.values().forEach(item -> {
            if (lastBookings.containsKey(item.getId())) {
                item.setLastBooking(BookingDtoMapper.mapBookingShortToDto(lastBookings.get(item.getId())));
            }
            if (nextBookings.containsKey(item.getId())) {
                item.setNextBooking(BookingDtoMapper.mapBookingShortToDto(nextBookings.get(item.getId())));
            }
        });

    }

    private void addCommentsForItems(Map<Long, ItemResponseDto> items) {
        Map<Long, List<CommentResponseDto>> comments = commentRepository
                .findAllByItemIdIn(items.keySet())
                .stream()
                .map(CommentDtoMapper::mapCommentToResponseDto)
                .collect(Collectors.groupingBy(CommentResponseDto::getItemId, Collectors.toList()));

        items.values().forEach(item -> item.setComments(comments.getOrDefault(item.getId(), Collections.emptyList())));
    }

}
