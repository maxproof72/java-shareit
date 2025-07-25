package ru.practicum.shareit.user;

import jakarta.validation.constraints.Positive;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto getUser(long id);

    UserDto addUser(NewUserRequest newUserRequest);

    UserDto updateUser(long id, UpdateUserRequest updateUserRequest);

    Collection<UserDto> getUsers();

    void deleteUser(@Positive Long userId);
}
