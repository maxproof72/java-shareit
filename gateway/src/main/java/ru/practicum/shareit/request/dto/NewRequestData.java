package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.utils.CustomDateTimeDeserializer;
import ru.practicum.shareit.utils.CustomDateTimeSerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewRequestData {

    @NotNull @NotBlank @Length(max = 512, message = "Текст запроса ограничен длиной 512 символов")
    String description;

    @Null(message = "Недопустимые данные времени создания запроса")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    LocalDateTime created;
}
