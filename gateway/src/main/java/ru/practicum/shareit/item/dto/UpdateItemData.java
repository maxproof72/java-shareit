package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemData {

    @Pattern(regexp = ".*\\S.*", message = "Название предмета не может быть пустым")
    @Length(max = 64, message = "Название предмета ограничено длиной в 64 символа")
    private String name;

    @Pattern(regexp = ".*\\S.*", message = "Описание предмета не может быть пустым")
    @Length(max = 512, message = "Описание предмета ограничено длиной в 512 символов")
    private String description;

    private Boolean available;
}
