package ru.practicum.shareit.user;

import lombok.NonNull;
import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {

    public static UserDto mapToDto(@NonNull User user) {
        return new UserDto(user.getName(), user.getEmail());
    }
}
