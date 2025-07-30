package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserObject(long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public UserDto getUser(long id) {
        log.info("Запрос данных пользователя {}", id);
        return UserMapper.toDto(userRepository.getUserById(id));
    }

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        log.info("Запрос на добавление пользователя {}", newUserRequest);
        User user = UserMapper.toUser(newUserRequest);
        user = userRepository.addUser(user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(long id, UpdateUserRequest updateUserRequest) {
        log.info("Запрос на обновление данных пользователя с id={}: {}", id, updateUserRequest);
        User user = UserMapper.toUser(updateUserRequest);
        user.setId(id);
        user = userRepository.updateUser(user);
        return UserMapper.toDto(user);
    }

    @Override
    public Collection<UserDto> getUsers() {
        log.info("Запрос на получение списка всех пользователей");
        return userRepository.getAllUsers().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long id) {
        log.info("Запрос на удаление пользователя с id={}", id);
        userRepository.deleteUser(id);
    }
}
