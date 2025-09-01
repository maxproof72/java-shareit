package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userDbRepository;

    @Override
    public UserDto getUser(long id) {

        var user = userDbRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь id=%d не найден".formatted(id)));
        log.debug("Возвращается информация о пользователе {}", user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto addUser(NewUserDto userData) {

        User user = UserMapper.toUser(userData);
        if (userDbRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Дублирующий email '%s'".formatted(user.getEmail()));
        }
        user = userDbRepository.save(user);
        log.debug("Добавлен пользователь {}", user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(long id, NewUserDto userData) {

        var user = userDbRepository.findById(id)
               .orElseThrow(() -> new NotFoundException(
                       "Пользователь id=%d не найден".formatted(id)));
        if (userData.getName() != null) {
            user.setName(userData.getName());
        }
        final String newEmail = userData.getEmail();
        if (newEmail != null && !newEmail.equals(user.getEmail())) {
            if (userDbRepository.existsByEmail(newEmail)) {
                throw new IllegalStateException("Обновление с дублирующим email '%s'".formatted(newEmail));
            }
            user.setEmail(userData.getEmail());
        }
        user = userDbRepository.save(user);
        log.debug("Обновлен пользователь {}", user);
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUsers() {

        var us = userDbRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
        log.debug("Возвращен список пользователей из {} записей", us.size());
        return us;
    }

    @Override
    public void deleteUser(long id) {

        userDbRepository.deleteById(id);
        log.debug("Удален пользователь c id={}", id);
    }
}
