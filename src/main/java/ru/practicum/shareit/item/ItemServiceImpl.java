package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    public ItemDto getItem(long itemId, long userId) {
        log.info("Запрос на информацию о предмете {} пользователя {}", itemId, userId);
        return ItemMapper.toDto(itemRepository.getItem(itemId, userId));
    }

    @Override
    public ItemDto addItem(NewItemRequest newItemRequest) {

        log.info("Запрос на добавление предмета {}", newItemRequest);
        User user = userRepository.getUserById(newItemRequest.getOwnerId());
        Item newItem = ItemMapper.toItem(newItemRequest);
        newItem.setOwner(user);
        newItem = itemRepository.addItem(newItem);
        return ItemMapper.toDto(newItem);
    }

    @Override
    public ItemDto updateItem(UpdateItemRequest updateItemRequest) {

        log.info("Запрос на обновление предмета {}", updateItemRequest);
        User user = userRepository.getUserById(updateItemRequest.getOwnerId());
        Item newItemData = ItemMapper.toItem(updateItemRequest);
        newItemData.setOwner(user);
        newItemData = itemRepository.updateItem(newItemData);
        return ItemMapper.toDto(newItemData);
    }

    @Override
    public Collection<ItemDto> getItemsOfUser(long userId) {

        log.info("Запрос на список предметов пользователя {}", userId);
        User user = userRepository.getUserById(userId);
        return itemRepository.getItemsOfUser(user).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> searchItems(long userId, String searchPattern) {

        log.info("Запрос на поиск предметов пользователя {} по образцу {}", userId, searchPattern);
        User user = userRepository.getUserById(userId);
        if (searchPattern.isEmpty()) {
            return List.of();
        }
        return itemRepository.searchItems(user, searchPattern).stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}
