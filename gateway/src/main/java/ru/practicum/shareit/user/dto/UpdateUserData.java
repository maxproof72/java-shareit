package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserData {

    @Pattern(regexp = ".*\\S.*", message = "Имя не может быть пустым")
    @Length(max = 32, message = "Имя должно быть не длиннее 32 символов")
    private String name;

    @Email(message = "Электронная почта должна быть корректной")
    @Length(max = 64, message = "Электронная почта должна быть не длиннее 64 символов")
    private String email;
}
