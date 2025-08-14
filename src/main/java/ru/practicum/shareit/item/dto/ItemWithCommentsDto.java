package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comments.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemWithCommentsDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private Long request;
    private List<CommentDto> comments;
}
