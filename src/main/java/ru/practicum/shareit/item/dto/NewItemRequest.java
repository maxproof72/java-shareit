package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemRequest {

    @NotBlank(message = "Название предмета не может быть пустым")
    String name;

    @NotBlank(message = "Описание предмета не может быть пустым")
    String description;

    @NotNull(message = "Не указана доступность предмета")
    Boolean available;

    Long ownerId;
}
