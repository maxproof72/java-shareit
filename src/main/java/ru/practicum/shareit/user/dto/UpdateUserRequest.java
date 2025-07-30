package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Pattern(regexp = ".*\\S.*", message = "Имя не может быть пустым")
    private String name;

    @Email(message = "Электронная почта должна быть корректной")
    private String email;
}
