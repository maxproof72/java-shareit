package ru.practicum.shareit.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentRequest {

    @Null
    private Long userId;

    @Null
    private Long itemId;

    @NotNull @NotBlank
    @Length(max = 512, message = "Длина комментария не может превышать 512 символов")
    private String text;
}
