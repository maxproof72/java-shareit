package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserData {

    @NotBlank(message = "Имя не может быть пустым")
    @Length(max = 32, message = "Имя должно быть не длиннее 32 символов")
    private String name;

    @Email(message = "Электронная почта должна быть корректной")
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Length(max = 64, message = "Электронная почта должна быть не длиннее 64 символов")
    private String email;
}
