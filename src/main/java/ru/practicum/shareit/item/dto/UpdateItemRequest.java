package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemRequest {

    private Long id;

    @Pattern(regexp = ".*\\S.*", message = "Название предмета не может быть пустым")
    private String name;

    @Pattern(regexp = ".*\\S.*", message = "Описание предмета не может быть пустым")
    private String description;

    private Boolean available;
    private Long ownerId;
}
