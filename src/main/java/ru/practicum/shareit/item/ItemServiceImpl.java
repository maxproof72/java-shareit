package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;

@Slf4j
@Qualifier("ItemDbService")
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto getItem(long itemId, long userId) {
        Item item = itemRepository.findByIdAndOwnerId(itemId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Не найден предмет id=%d пользователя id=%d".formatted(itemId, userId)));
        log.info("Возвращается информация о предмете {}", item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto addItem(NewItemRequest newItemRequest) {

        Item item = ItemMapper.toItem(newItemRequest);
        User user = userRepository.findById(newItemRequest.getOwnerId())
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь id=%d не найден".formatted(newItemRequest.getOwnerId())));
        item.setOwner(user);
        item = itemRepository.save(item);
        log.info("Добавлен предмет {}", item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto updateItem(UpdateItemRequest updateItemRequest) {
        Item item = itemRepository.findByIdAndOwnerId(updateItemRequest.getId(), updateItemRequest.getOwnerId())
                .orElseThrow(() -> new NotFoundException(
                        "Не найден предмет id=%d пользователя id=%d".formatted(updateItemRequest.getId(), updateItemRequest.getOwnerId())));
        if (updateItemRequest.getName() != null)
            item.setName(updateItemRequest.getName());
        if (updateItemRequest.getDescription() != null)
            item.setDescription(updateItemRequest.getDescription());
        if (updateItemRequest.getAvailable() != null)
            item.setAvailable(updateItemRequest.getAvailable());
        item = itemRepository.save(item);
        log.info("Обновлен предмет {}", item);
        return ItemMapper.toDto(item);
    }

    @Override
    public Collection<ItemDto> getItemsOfUser(long userId) {
        var it = itemRepository.findItemsByOwnerId(userId);
        log.info("Запрос предметов пользователя с id={} вернул {} записей", userId, it.size());
        return it.stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public Collection<ItemDto> searchItems(long userId, @NonNull String searchPattern) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь id=%d не найден".formatted(userId)));
        var it = itemRepository.findItemsByOwnerIdAndPattern(user, searchPattern.toLowerCase());
        log.info("Поиск предметов {} пользователя с id={} вернул {} записей", searchPattern, userId, it.size());
        return it.stream().map(ItemMapper::toDto).toList();
    }
}
