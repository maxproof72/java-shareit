package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface ItemRepository {

    /**
     * Возвращает предмет по его <b>id</b>
     * @param itemId Идентификатор предмета
     * @param userId Идентификатор пользователя - хозяина предмета
     * @return Предмет с указанным id
     * @throws NotFoundException если предмет с указанным id не существует
     * или принадлежит другому пользователю
     */
    Item getItem(long itemId, long userId) throws NotFoundException;

    /**
     * Добавляет новый предмет в хранилище
     * @param item Новый предмет
     * @return Новый предмет с установленным <b>id</b>
     */
    Item addItem(Item item);

    /**
     * Обновляет предмет
     * @param newItemData Обновление данных предмета
     * @return Обновленный предмет
     * @throws NotFoundException если предмет с указанным id не найден
     * или предмет принадлежит другому пользователю
     */
    Item updateItem(Item newItemData) throws NotFoundException;

    /**
     * Возвращает перечень предметов, принадлежащих указанному пользователю
     * @param user Пользователь
     * @return Перечень предметов
     */
    Collection<Item> getItemsOfUser(User user);

    /**
     * Производит поиск предметов пользователя user по образцу searchPattern в имени или описании
     * @param user Пользователь
     * @param searchPattern Образец поиска
     * @return Перечень предметов
     */
    Collection<Item> searchItems(User user, String searchPattern);
}
