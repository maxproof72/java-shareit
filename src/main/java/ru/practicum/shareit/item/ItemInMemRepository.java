package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ItemInMemRepository implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private long id;


    @Override
    public Item getItem(long itemId, long userId) {

        Item item = items.get(id);
        if (item == null) {
            final String msg = "Предмет с id=" + id + " не найден";
            log.error(msg);
            throw new NotFoundException("msg");
        }
        if (item.getOwner().getId() != userId) {
            final String msg = "Предмет с id=" + id + " принадлежит другому пользователю";
            log.error(msg);
            throw new NotFoundException("msg");
        }
        log.info("Возвращается информация о предмете {}", item);
        return item;
    }

    @Override
    public Item addItem(Item item) {

        item.setId(++id);
        items.put(item.getId(), item);
        log.debug("Добавлен предмет {}", item);
        return item;
    }

    @Override
    public Item updateItem(Item newItemData) {

        Item existingItem = getItem(newItemData.getId(), newItemData.getOwner().getId());
        if (newItemData.getName() != null) {
            existingItem.setName(newItemData.getName());
        }
        if (newItemData.getDescription() != null) {
            existingItem.setDescription(newItemData.getDescription());
        }
        if (newItemData.getAvailable() != null) {
            existingItem.setAvailable(newItemData.getAvailable());
        }
        return existingItem;
    }

    @Override
    public Collection<Item> getItemsOfUser(User user) {

        Collection<Item> it = items.values().stream()
                .filter(item -> item.getOwner().getId() == user.getId())
                .toList();
        log.info("Запрос предметов пользователя с id={} вернул {} записей", user.getId(), it.size());
        return it;
    }

    @Override
    public Collection<Item> searchItems(User user, String searchPattern) {

        final String pattern = searchPattern.toLowerCase();
        Collection<Item> it = items.values().stream()
                .filter(item -> item.getOwner().getId() == user.getId())
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(pattern) ||
                        item.getDescription().toLowerCase().contains(pattern))
                .toList();
        log.info("Поиск предметов по образцу {} вернул {} записей", searchPattern, it.size());
        return it;
    }
}
