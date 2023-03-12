package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemDtoMapper itemDtoMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemDtoMapper itemDtoMapper, ItemRepository itemRepository, UserRepository userRepository) {
        this.itemDtoMapper = itemDtoMapper;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        log.debug("Для пользователя с id = {} добавляется новый объект: {}", userId, itemDto);
        checkBeforeSave(userId, itemDto);

        Item savedItem = itemRepository.saveItem(itemDtoMapper.mapDtoToItem(userId, itemDto));
        log.trace("Сохранённый предмет: {}", savedItem);
        return itemDtoMapper.mapItemToDto(savedItem);
    }

    @Override
    public ItemDto readItem(Long userId, Long itemId) {
        log.debug("Для пользователя с id = {} запрошен объект с id = {}", userId, itemId);
        Item resultItem = itemRepository.getItem(userId, itemId);
        log.trace("Найден объект {}", resultItem);
        return itemDtoMapper.mapItemToDto(resultItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        log.debug("Для пользователя с id = {} добавляется новый объект: {}", userId, itemDto);
        checkBeforeUpdate(userId, itemDto);

        Item oldItem = itemRepository.getItem(userId, itemId);
        log.trace("Старый объект: {}", oldItem);
        Item updatedItem = itemDtoMapper.mapDtoToItem(userId, itemDto);
        updatedItem.setShareCounter(oldItem.getShareCounter());
        log.trace("Новый объект: {}",updatedItem);
        itemRepository.updateItem(updatedItem);
        log.trace("Обновление завершено");
        return itemDtoMapper.mapItemToDto(updatedItem);
    }

    @Override
    public ItemDto deleteItem(Long userId, Long itemId) {
        log.debug("Для пользователя с id = {} удаляется предмет с id = {}", userId, itemId);
        checkIfUserExist(userId);
        checkIfItemExist(itemId);

        Item deletedItem = itemRepository.deleteItem(userId, itemId);
        log.trace("Выполнено удаление предмета: {}", deletedItem);
        return itemDtoMapper.mapItemToDto(deletedItem);
    }

    @Override
    public ItemDto findAvailableItems(String text) {
        return null;
    }

    private void checkBeforeSave(Long userId, ItemDto itemDto) {
        checkIfUserExist(userId);
    }

    private void checkBeforeUpdate(Long userId, ItemDto itemDto) {
        checkIfUserExist(userId);
        checkIfItemExist(itemDto.getId());
    }

    private void checkIfUserExist(Long userId) {
        if (!userRepository.isExist(userId)) {
            log.warn("Владелец с id = {} не существует", userId);
            throw new UserNotFoundException("Ошибка при сохранении вещи - владелец не обнаружен");
        }
    }

    private void checkIfItemExist(Long itemId) {
        if (!itemRepository.isExist(itemId)) {
            log.warn("Предмет с id = {} не существует", itemId);
            throw new ItemNotFoundException("Ошибка при обновлении вещи - объект не был добавлен ранее");
        }
    }

}
