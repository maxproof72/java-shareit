package ru.practicum.shareit.user;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;

@UtilityClass
public class UserMapper {

    public UserDto toDto(@NonNull User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User toUser(@NonNull NewUserDto userData) {
        return new User(
                null,
                userData.getName(),
                userData.getEmail()
        );
    }
}
