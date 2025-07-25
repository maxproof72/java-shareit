package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.interfaces.Marker;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ItemRequestDto {

    @NotBlank(message = "Описание заявки не может быть пустым")
    String description;

    @NotNull(groups = Marker.OnCreate.class, message = "Пустое поле заявителя недопустимо")
    @NotNull(groups = Marker.OnUpdate.class, message = "Пустое поле заявителя недопустимо")
    Long requestor;

    LocalDate created;
}
