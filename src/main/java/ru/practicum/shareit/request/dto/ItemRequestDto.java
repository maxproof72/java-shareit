package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ItemRequestDto {

    String description;
    Long requestor;
    LocalDate created;
}
