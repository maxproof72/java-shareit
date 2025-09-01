package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.CustomDateTimeDeserializer;
import ru.practicum.shareit.utils.CustomDateTimeSerializer;
import ru.practicum.shareit.comments.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithCommentsDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime lastBooking;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime nextBooking;

    private Long request;
    private List<CommentDto> comments;
}
