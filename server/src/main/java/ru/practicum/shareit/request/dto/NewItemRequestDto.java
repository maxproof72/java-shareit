package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.utils.CustomDateTimeDeserializer;
import ru.practicum.shareit.utils.CustomDateTimeSerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewItemRequestDto {

    private String description;
    private Long requestorId;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime created;
}
