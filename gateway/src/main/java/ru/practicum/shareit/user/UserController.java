package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserData;
import ru.practicum.shareit.user.dto.UpdateUserData;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {

        log.info("GET request of users list");
        return userClient.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Long userId) {

        log.info("GET request of user information (id={})", userId);
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody NewUserData userRequest) {

        log.info("POST request to create a user (data={})", userRequest);
        return userClient.addUser(userRequest);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long userId,
                                             @Valid @RequestBody UpdateUserData userRequest) {

        log.info("PATCH request to update user data (userId={}, data={})", userId, userRequest);
        return userClient.updateUser(userId, userRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long userId) {

        log.info("DELETE request to delete user (userId={}) ", userId);
        userClient.deleteUser(userId);
    }
}

