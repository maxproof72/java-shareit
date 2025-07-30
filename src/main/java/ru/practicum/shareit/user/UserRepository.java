package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;

public interface UserRepository {

    /**
     * Возвращает пользователя по его <b>id</b>
     * @param id Идентификатор пользователя
     * @return Пользователь с указанным id
     * @throws NotFoundException если пользователь с указанным id не существует
     */
    User getUserById(long id);

    /**
     * Добавляет нового пользователя в хранилище
     * @param user Новый пользователь
     * @return Новый пользователь с установленным <b>id</b>
     * @throws DuplicatedDataException если пользователь с указанным email существует
     */
    User addUser(User user);

    /**
     * Обновляет пользователя
     * @param user Обновление пользователя
     * @return Обновленный пользователь
     * @throws NotFoundException если пользователь с указанным id не найден
     * @throws DuplicatedDataException если обновленное значение email уже используется
     */
    User updateUser(User user);

    /**
     * Возвращает список всех пользователей
     * @return Список пользователей
     */
    Collection<User> getAllUsers();

    /**
     * Удаляет пользователя, если он есть.
     * Если пользователя с заданным id не существует, ничего не происходит
     * @param userId Идентификатор пользователя
     */
    void deleteUser(Long userId);
}
