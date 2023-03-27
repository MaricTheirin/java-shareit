package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemDtoMapper itemDtoMapper;
    private final CommentDtoMapper commentDtoMapper;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(
            ItemDtoMapper itemDtoMapper,
            CommentDtoMapper commentDtoMapper,
            ItemRepository itemRepository,
            CommentRepository commentRepository,
            UserRepository userRepository,
            BookingRepository bookingRepository
    ) {
        this.itemDtoMapper = itemDtoMapper;
        this.commentDtoMapper = commentDtoMapper;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        log.debug("Для пользователя с id = {} добавляется новый объект: {}", userId, itemDto);
        checkBeforeSave(userId, itemDto);

        Item savedItem = itemRepository.saveAndFlush(itemDtoMapper.mapDtoToItem(userId, itemDto));
        log.trace("Сохранённый предмет: {}", savedItem);
        return itemDtoMapper.mapItemToDto(savedItem);
    }

    @Override
    public ItemDto read(Long userId, Long itemId) {
        log.debug("Пользователь с id = {} запросил объект с id = {}", userId, itemId);
        checkIfItemExist(itemId);

        Item resultItem = itemRepository.getReferenceById(itemId);
        log.trace("Найден объект {}", resultItem);
        return itemDtoMapper.mapItemToDto(resultItem, userId.equals(resultItem.getOwnerId()));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        log.debug("Запрошено обновление объекта с id = {} ({}) для пользователя с id = {}", itemId, itemDto, userId);
        checkBeforeUpdate(userId, itemId);

        Item savedItem = itemRepository.getReferenceById(itemId);
        updateItemFields(userId, savedItem, itemDto);
        itemRepository.flush();
        log.trace("Сохранённый предмет: {}", savedItem);
        return itemDtoMapper.mapItemToDto(savedItem);
    }

    @Override
    public ItemDto delete(Long userId, Long itemId) {
        log.debug("Для пользователя с id = {} удаляется предмет с id = {}", userId, itemId);

        checkIfUserExist(userId);
        checkIfItemExistAndBelongsToUser(userId, itemId);
        Item itemToDelete = itemRepository.getReferenceById(itemId);
        itemRepository.delete(itemToDelete);
        log.trace("Выполнено удаление предмета: {}", itemToDelete);
        return itemDtoMapper.mapItemToDto(itemToDelete);
    }

    @Override
    public List<ItemDto> findAvailableItemsBySearchQuery(String searchQuery) {
        log.debug("Запрошен поиск всех доступных вещей, содержащих '{}'", searchQuery);
        if (searchQuery.length() == 0) {
            return new ArrayList<>();
        }

        List<Item> foundItems = itemRepository.findAllAvailableAndContainingQueryIgnoreCase(searchQuery);
        log.trace("Найденные вещи: {}", foundItems);
        return foundItems.stream().map(itemDtoMapper::mapItemToDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        log.debug("Для пользователя с id = {} запрошен список всех предметов", userId);
        checkIfUserExist(userId);

        List<Item> allUserItems = itemRepository.findAllByOwnerIdOrderByIdAsc(userId);
        log.trace("Получен массив предметов: {}", allUserItems);
        return allUserItems.stream().map(item -> itemDtoMapper.mapItemToDto(item, true)).collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        log.debug(
                "Пользователь с id = {} запросил добавление к предмету с id = {} комментария {}",
                userId, itemId, commentDto
        );
        commentDto.setItemId(itemId);
        commentDto.setCreated(LocalDateTime.now());
        checkBeforeCommentSave(userId, itemId, commentDto);

        Comment savedComment = commentRepository.saveAndFlush(commentDtoMapper.mapDtoToComment(commentDto, userId));
        log.trace("Результат сохранения комментария: {}", savedComment);
        return commentDtoMapper.mapCommentToDto(savedComment);
    }

    private void checkBeforeCommentSave(Long userId, Long itemId, CommentDto commentDto) {
        checkIfUserExist(userId);
        checkIfItemExist(itemId);
        if (!bookingRepository.existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now())
        ) {
            throw new ItemNotAvailableException("Оставить комментарий можно только успешной аренды");
        }
    }

    private void checkBeforeSave(Long userId, ItemDto itemDto) {
        checkIfUserExist(userId);
    }

    private void checkBeforeUpdate(Long userId, Long itemId) {
        checkIfUserExist(userId);
        checkIfItemExistAndBelongsToUser(userId, itemId);
    }

    private void checkIfUserExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Владелец с id = {} не существует", userId);
            throw new UserNotFoundException("Ошибка при сохранении вещи - владелец не обнаружен");
        }
    }

    private void checkIfItemExist(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            log.warn("Предмет с id = {} не существует", itemId);
            throw new ItemNotFoundException("Ошибка при обновлении вещи - объект не был добавлен ранее");
        }
    }

    private void checkIfItemExistAndBelongsToUser(Long userId, Long itemId) {
        checkIfItemExist(itemId);
        Item item = itemRepository.getReferenceById(itemId);
        if (item.getOwnerId() != userId) {
            log.warn("Предмет с id = {} не принадлежит пользователю с id = {}", itemId, userId);
            throw new ItemNotFoundException("Ошибка при обновлении вещи - объект не принадлежит пользователю");
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

}
