package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

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
    UserDto addUser(NewUserDto newUserRequest);

    /**
     * Обновление данных пользователя
     * @param id Id пользователя
     * @param userData Новые данные пользователя
     * @return Обновленный пользователь
     * @throws IllegalStateException при дублировании email
     */
    UserDto updateUser(long id, NewUserDto userData);

    /**
     * Перечень всех пользователей
     *
     * @return Перечень пользователей
     */
    List<UserDto> getUsers();

    /**
     * Удаление пользователя
     * @param id Id пользователя
     */
    void deleteUser(long id);
}
