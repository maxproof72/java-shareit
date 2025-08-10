package ru.practicum.shareit.user;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

@UtilityClass
public class UserMapper {

    public static UserDto toDto(@NonNull User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUser(@NonNull NewUserRequest newUserRequest) {
        return new User(
                null,
                newUserRequest.getName(),
                newUserRequest.getEmail()
        );
    }
}
