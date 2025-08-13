package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

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
        log.info("Возвращается информация о пользователе {}", user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {

        User user = UserMapper.toUser(newUserRequest);
        if (userDbRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Дублирующий email '%s'".formatted(user.getEmail()));
        }
        user = userDbRepository.save(user);
        log.info("Добавлен пользователь {}", user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(long id, UpdateUserRequest updateUserRequest) {

        var user = userDbRepository.findById(id)
               .orElseThrow(() -> new NotFoundException(
                       "Пользователь id=%d не найден".formatted(id)));
        if (updateUserRequest.getName() != null) {
            user.setName(updateUserRequest.getName());
        }
        final String newEmail = updateUserRequest.getEmail();
        if (newEmail != null && !newEmail.equals(user.getEmail())) {
            if (userDbRepository.existsByEmail(newEmail)) {
                throw new IllegalStateException("Обновление с дублирующим email '%s'".formatted(newEmail));
            }
            user.setEmail(updateUserRequest.getEmail());
        }
        user = userDbRepository.save(user);
        log.info("Обновлен пользователь {}", user);
        return UserMapper.toDto(user);
    }

    @Override
    public Collection<UserDto> getUsers() {
        var us = userDbRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
        log.info("Возвращен список пользователей из {} записей", us.size());
        return us;
    }

    @Override
    public void deleteUser(long id) {
        userDbRepository.deleteById(id);
        log.info("Удален пользователь c id={}", id);
    }
}
