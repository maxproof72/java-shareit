package ru.practicum.shareit.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentRequest {

    @Null
    private Long userId;
    @Null
    private Long itemId;
    @NotNull @NotBlank
    private String text;
}
