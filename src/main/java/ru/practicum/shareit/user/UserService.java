package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    /**
     * Поиск пользователя по его Id
     * @param id Id пользователя
     * @return Пользователь
     * @throws ru.practicum.shareit.exception.NotFoundException если пользователь не найден
     */
    UserDto getUser(long id);

    /**
     * Добавление нового пользователя
     * @param newUserRequest Данные нового пользователя
     * @return Новый пользователь
     * @throws IllegalStateException при дублировании email
     */
    UserDto addUser(NewUserRequest newUserRequest);

    /**
     * Обновление данных пользователя
     * @param id Id пользователя
     * @param updateUserRequest Новые данные пользователя
     * @return Обновленный пользователь
     * @throws IllegalStateException при дублировании email
     */
    UserDto updateUser(long id, UpdateUserRequest updateUserRequest);

    /**
     * Перечень всех пользователей
     * @return Перечень пользователей
     */
    Collection<UserDto> getUsers();

    /**
     * Удаление пользователя
     * @param id Id пользователя
     */
    void deleteUser(long id);
}
