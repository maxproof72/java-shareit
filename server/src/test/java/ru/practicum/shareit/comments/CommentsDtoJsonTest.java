package ru.practicum.shareit.comments;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.NewCommentDto;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentsDtoJsonTest {

    private final JacksonTester<CommentDto> jsonDto;
    private final JacksonTester<NewCommentDto> jsonNewDto;

    @Test
    void testCommentDtoJson() throws Exception {

        var dto = new CommentDto(
                1L,
                "Great device!",
                "John Smith",
                LocalDateTime.of(2025, Month.JULY, 15, 17, 45, 19)
        );

        var jsonContent = jsonDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.text").isEqualTo("Great device!");
        assertThat(jsonContent).extractingJsonPathStringValue("$.authorName").isEqualTo("John Smith");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo("2025-07-15T17:45:19");
    }

    @Test
    void testNewCommentDtoJson() throws Exception {

        var dto = new NewCommentDto(15L, 99L, "Great device!");

        var jsonContent = jsonNewDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.userId").isEqualTo(15);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId").isEqualTo(99);
        assertThat(jsonContent).extractingJsonPathStringValue("$.text").isEqualTo("Great device!");
    }
}
