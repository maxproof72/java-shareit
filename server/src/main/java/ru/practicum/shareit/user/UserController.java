package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
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
    public UserDto addUser(@RequestBody NewUserDto userRequest) {

        log.info("Запрос POST добавления пользователя (data={})", userRequest);
        return userService.addUser(userRequest);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") Long userId,
                              @RequestBody NewUserDto userData) {

        log.info("Запрос PATCH обновления пользователя (userId={}, data={})", userId, userData);
        return userService.updateUser(userId, userData);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long userId) {

        log.info("Запрос DELETE удаления пользователя (userId={}) ", userId);
        userService.deleteUser(userId);
    }
}

