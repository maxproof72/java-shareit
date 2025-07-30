package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserInMemRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private long id;

    private void checkDuplicatedEmail(String email, long ownId) {
        long id = users.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .mapToLong(User::getId)
                .findAny()
                .orElse(0);
        if (id != 0 && id != ownId)
            throw new DuplicatedDataException("Email " + email + " уже используется");
    }

    @Override
    public User getUserById(long id) {
        User user = users.get(id);
        if (user == null)
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        log.info("Возвращается информация о пользователе {}", user);
        return user;
    }

    @Override
    public User addUser(User user) {

        checkDuplicatedEmail(user.getEmail(), 0);
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("Добавлен пользователь {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {

        User existingUser = getUserById(user.getId());
        if (user.getName() != null)
            existingUser.setName(user.getName());
        if (user.getEmail() != null) {
            checkDuplicatedEmail(user.getEmail(), existingUser.getId());
            existingUser.setEmail(user.getEmail());
        }
        log.debug("Обновлен пользователь {}", user);
        return existingUser;
    }

    @Override
    public Collection<User> getAllUsers() {
        var us = users.values().stream()
                .sorted(Comparator.comparingLong(User::getId))
                .toList();
        log.info("Возвращен список пользователей из {} записей", us.size());
        return us;
    }

    @Override
    public void deleteUser(Long userId) {
        var u = users.remove(userId);
        if (u != null)
            log.info("Удален пользователь {}", u);
        else
            log.info("Пользователя с id={} не существует", id);
    }
}
