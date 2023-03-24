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
    public ItemDto create(Long userId, ItemDto itemDto) {
        log.debug("Для пользователя с id = {} добавляется новый объект: {}", userId, itemDto);
        checkBeforeSave(userId, itemDto);

        Item savedItem = itemRepository.save(itemDtoMapper.mapDtoToItem(userId, itemDto));
        log.trace("Сохранённый предмет: {}", savedItem);
        return itemDtoMapper.mapItemToDto(savedItem);
    }

    @Override
    public ItemDto read(Long userId, Long itemId) {
        log.debug("Пользователь с id = {} запросил объект с id = {}", userId, itemId);
        Item resultItem = itemRepository.get(itemId);
        log.trace("Найден объект {}", resultItem);
        return itemDtoMapper.mapItemToDto(resultItem);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        log.debug("Запрошено обновление объекта с id = {} ({}) для пользователя с id = {}", itemId, itemDto, userId);
        checkBeforeUpdate(userId, itemId);

        Item savedItem = itemRepository.get(itemId);
        updateItemFields(userId, savedItem, itemDto);
        log.trace("Сохранённый предмет: {}", savedItem);
        return itemDtoMapper.mapItemToDto(savedItem);
    }

    @Override
    public ItemDto delete(Long userId, Long itemId) {
        log.debug("Для пользователя с id = {} удаляется предмет с id = {}", userId, itemId);
        checkIfUserExist(userId);
        checkIfItemExist(userId, itemId);

        Item deletedItem = itemRepository.delete(userId, itemId);
        log.trace("Выполнено удаление предмета: {}", deletedItem);
        return itemDtoMapper.mapItemToDto(deletedItem);
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        log.debug("Для пользователя с id = {} запрошен список всех предметов", userId);
        checkIfUserExist(userId);

        List<Item> allUserItems = itemRepository.findAll(userId);
        log.trace("Получен массив предметов: {}", allUserItems);
        return allUserItems.stream().map(itemDtoMapper::mapItemToDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAvailable(String searchQuery) {
        log.debug("Запрошен поиск всех доступных вещей, содержащих '{}'", searchQuery);

        List<Item> foundItems = itemRepository.findAvailable(searchQuery);
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

    private void updateItemFields(Long ownerUserId, Item item, ItemDto updatedItemDto) {
        log.debug("Обновление вещи {} данными из DTO: {}", item, updatedItemDto);

        if (updatedItemDto.getName() != null && !updatedItemDto.getName().isBlank()) {
            item.setName(updatedItemDto.getName());
            log.debug(
                    "У вещи {} изменёно наименование. Старое значение - {}, новое значение - {}",
                    updatedItemDto,
                    item.getName(),
                    updatedItemDto.getName()
            );
        }

        if (updatedItemDto.getDescription() != null && !updatedItemDto.getDescription().isBlank()) {
            item.setDescription(updatedItemDto.getDescription());
            log.debug(
                    "У вещи {} изменёно описание. Старое значение - {}, новое значение - {}",
                    item,
                    item.getDescription(),
                    updatedItemDto.getDescription()
            );
        }

        if (updatedItemDto.getAvailable() != null) {
            item.setAvailable(updatedItemDto.getAvailable());
            log.debug(
                    "У вещи {} изменена доступность. Старое значение - {}, новое значение - {}",
                    item,
                    item.isAvailable(),
                    updatedItemDto.getAvailable()

            );
        }
        log.trace("Обновлённая вещь: {}", item);
    }

}
