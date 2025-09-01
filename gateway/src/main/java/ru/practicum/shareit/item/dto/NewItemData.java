package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemData {

    @NotBlank(message = "Название предмета не может быть пустым")
    @Length(max = 64, message = "Название предмета ограничено длиной в 64 символа")
    private String name;

    @NotBlank(message = "Описание предмета не может быть пустым")
    @Length(max = 512, message = "Описание предмета ограничено длиной в 512 символов")
    private String description;

    @NotNull(message = "Не указана доступность предмета")
    private Boolean available;

    private Long requestId;
}
