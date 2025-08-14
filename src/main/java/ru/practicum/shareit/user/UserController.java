package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers() {

        log.info("Запрос GET получения списка пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long userId) {

        log.info("Запрос GET информации о пользователе (id={})", userId);
        return userService.getUser(userId);
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody NewUserRequest userRequest) {

        log.info("Запрос POST добавления пользователя (data={})", userRequest);
        return userService.addUser(userRequest);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") Long userId,
                              @Valid @RequestBody UpdateUserRequest userRequest) {

        log.info("Запрос PATCH обновления пользователя (userId={}, data={})", userId, userRequest);
        return userService.updateUser(userId, userRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long userId) {

        log.info("Запрос DELETE удаления пользователя (userId={}) ", userId);
        userService.deleteUser(userId);
    }
}

