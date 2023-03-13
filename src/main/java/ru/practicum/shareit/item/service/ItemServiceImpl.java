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
import java.util.List;
import java.util.stream.Collectors;

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
        log.debug("Пользователь с id = {} запросил объект с id = {}", userId, itemId);
        Item resultItem = itemRepository.getItem(itemId);
        log.trace("Найден объект {}", resultItem);
        return itemDtoMapper.mapItemToDto(resultItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        log.debug("Запрошено обновление объекта с id = {} ({}) для пользователя с id = {}", itemId, itemDto, userId);
        checkBeforeUpdate(userId, itemId);

        Item updatedItem = getItemWithUpdatedFields(userId, itemId, itemDto);
        Item savedItem = itemRepository.updateItem(updatedItem);
        log.trace("Сохранённый предмет: {}", savedItem);
        return itemDtoMapper.mapItemToDto(savedItem);
    }

    @Override
    public ItemDto deleteItem(Long userId, Long itemId) {
        log.debug("Для пользователя с id = {} удаляется предмет с id = {}", userId, itemId);
        checkIfUserExist(userId);
        checkIfItemExist(userId, itemId);

        Item deletedItem = itemRepository.deleteItem(userId, itemId);
        log.trace("Выполнено удаление предмета: {}", deletedItem);
        return itemDtoMapper.mapItemToDto(deletedItem);
    }

    @Override
    public List<ItemDto> readAllItems(Long userId) {
        log.debug("Для пользователя с id = {} запрошен список всех предметов", userId);
        checkIfUserExist(userId);

        List<Item> allUserItems = itemRepository.getAllItems(userId);
        log.trace("Получен массив предметов: {}", allUserItems);
        return allUserItems.stream().map(itemDtoMapper::mapItemToDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAvailableItems(String searchQuery) {
        log.debug("Запрошен поиск всех доступных вещей, содержащих '{}'", searchQuery);

        List<Item> foundItems = itemRepository.getAvailableItems(searchQuery);
        log.trace("Найденные вещи: {}", foundItems);
        return foundItems.stream().map(itemDtoMapper::mapItemToDto).collect(Collectors.toList());
    }

    private void checkBeforeSave(Long userId, ItemDto itemDto) {
        checkIfUserExist(userId);
    }

    private void checkBeforeUpdate(Long userId, Long itemId) {
        checkIfUserExist(userId);
        checkIfItemExist(userId, itemId);
    }

    private void checkIfUserExist(Long userId) {
        if (!userRepository.isExist(userId)) {
            log.warn("Владелец с id = {} не существует", userId);
            throw new UserNotFoundException("Ошибка при сохранении вещи - владелец не обнаружен");
        }
    }

    private void checkIfItemExist(Long userId, Long itemId) {
        if (!itemRepository.isExistAndBelongsToUser(userId, itemId)) {
            log.warn("Предмет с id = {} не существует или не принадлежит пользователю с id = {}", itemId, userId);
            throw new ItemNotFoundException("Ошибка при обновлении вещи - объект не был добавлен ранее");
        }
    }

    private Item getItemWithUpdatedFields(Long ownerUserId, Long savedItemId, ItemDto updatedItemDto) {
        log.debug("Обновление вещи с id {} данными из DTO: {}", savedItemId, updatedItemDto);

        Item savedItem = itemRepository.getItem(savedItemId);
        Item.ItemBuilder builder = Item.builder().id(savedItemId).ownerId(ownerUserId);

        if (updatedItemDto.getName() != null) {
            builder.name(updatedItemDto.getName());
            log.debug(
                    "У вещи {} изменёно наименование. Старое значение - {}, новое значение - {}",
                    updatedItemDto,
                    savedItem.getName(),
                    updatedItemDto.getName()
            );
        } else {
            builder.name(savedItem.getName());
        }

        if (updatedItemDto.getDescription() != null) {
            builder.description(updatedItemDto.getDescription());
            log.debug(
                    "У вещи {} изменёно описание. Старое значение - {}, новое значение - {}",
                    savedItem,
                    savedItem.getDescription(),
                    updatedItemDto.getDescription()
            );
        } else {
            builder.description(savedItem.getDescription());
        }

        if (updatedItemDto.getAvailable() != null) {
            builder.available(updatedItemDto.getAvailable());
            log.debug(
                    "У вещи {} изменена доступность. Старое значение - {}, новое значение - {}",
                    savedItem,
                    savedItem.isAvailable(),
                    updatedItemDto.getAvailable()

            );
        } else {
            builder.available(savedItem.isAvailable());
        }
        Item updatedItem = builder.build();
        log.trace("Обновлённая вещь: {}", updatedItem);
        return updatedItem;
    }

}
