package ru.practicum.shareit.item;

import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

public interface ItemService {

    /**
     * Производит поиск предмета
     * @param itemId Id предмета
     * @param userId Id владельца предмета
     * @return Предмет
     * @throws ru.practicum.shareit.exception.NotFoundException если предмет не найден
     */
    ItemWithCommentsDto getItem(long itemId, long userId);

    /**
     * Добавляет новый предмет в репозиторий
     * @param newItemRequest Данные нового предмета
     * @return Новый предмет
     */
    ItemDto addItem(NewItemRequest newItemRequest);

    /**
     * Обновление свойств предмета
     * @param updateItemRequest Обновленные данные предмета
     * @return Обновленный предмет
     */
    ItemDto updateItem(UpdateItemRequest updateItemRequest);

    /**
     * Возвращает перечень предметов указанного пользователя
     * @param userId Id Пользователя
     * @return Перечень предметов
     */
    Collection<ItemWithCommentsDto> getItemsOfUser(long userId);

    /**
     * Производит поиск предметов указанного пользователя по образцу имени или описания
     * @param userId Id пользователя
     * @param searchPattern Образец поиска
     * @return Перечень предметов
     * @apiNote Согласно уточнению наставника (смотри
     * <a href="https://app.pachca.com/chats/15717858?thread_message_id=573496296&message=573496296&sidebar_message=573954448">комментарий</a>),
     * искать нужно только его вещи. Для меня это абсолютно непонятно.
     */
    Collection<ItemDto> searchItems(long userId, String searchPattern);

    /**
     * Добавление комментария к предмету
     * @param request Данные комментария
     * @return Новый комментарий
     */
    CommentDto addComment(NewCommentRequest request);
}
