package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Поиск предметов заданного пользователя
     * @param id Идентификатор пользователя - хозяина предметов
     * @return Список предметов
     */
    List<Item> findItemsByOwnerId(long id);

    /**
     * Выборка всех предметов пользователя <ref>owner_if</ref> с именем или описанием,
     * содержащим заданный текст
     * @param owner Пользователь
     * @param value Текст поиска (!в нижнем регистре)
     * @return Список предметов
     * @apiNote Согласно уточнению наставника (смотри
     * <a href="https://app.pachca.com/chats/15717858?thread_message_id=573496296&message=573496296&sidebar_message=573954448">комментарий</a>),
     * искать нужно только его вещи. Для меня это абсолютно непонятно.
     */
    @Query("select it from items as it " +
            "where it.owner = :owner and " +
            "it.available and " +
            "  (lower(it.name) like %:value% or" +
            "   lower(it.description) like %:value%)")
    List<Item> findItemsByOwnerIdAndPattern(User owner, String value);

    /**
     * Производит поиск предмета с заданным id пользователя id
     * @param id Идентификатор предмета
     * @param ownerId Идентификатор пользователя
     * @return Опцион предмета
     */
    Optional<Item> findByIdAndOwnerId(Long id, Long ownerId);

    /**
     * Возвращает список предметов, добавленных в ответ на перечень заявок
     * @param requestIds Перечень Id заявок
     * @return Список предметов
     */
    List<Item> findItemsByRequestIdIn(Collection<Long> requestIds);

    /**
     * Возвращает список предметов, добавленных в ответ на указанную заявку
     * @param requestId Id заявки
     * @return Связанный перечень предметов
     */
    List<Item> findItemsByRequestId(Long requestId);
}
