package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestDtoJsonTest {

    private final JacksonTester<ItemRequestDto> jsonDto;
    private final JacksonTester<ItemRequestWithAnswersDto> jsonAnswersDto;
    private final JacksonTester<NewItemRequestDto> jsonNewDto;

    @Test
    void testItemDtoJson() throws Exception {

        var dto = new ItemRequestDto(
                1L,
                "Need item",
                LocalDateTime.of(2025, Month.JULY, 15, 17, 45, 19)
        );

        var jsonContent = jsonDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Need item");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo("2025-07-15T17:45:19");
    }

    @Test
    void testItemRequestWithAnswersDtoJson() throws Exception {

        var dto = new ItemRequestWithAnswersDto(
                1L,
                "Need item",
                LocalDateTime.of(2025, Month.JULY, 15, 17, 45, 19));
        dto.getItems().add(new ItemRequestWithAnswersDto.ItemAnswer(12L, "Name12", 13L));
        dto.getItems().add(new ItemRequestWithAnswersDto.ItemAnswer(14L, "Name14", 15L));

        var jsonContent = jsonAnswersDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Need item");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo("2025-07-15T17:45:19");
        assertThat(jsonContent).extractingJsonPathArrayValue("$.items").hasSize(2);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.items.[0].id").isEqualTo(12);
        assertThat(jsonContent).extractingJsonPathStringValue("$.items.[0].name").isEqualTo("Name12");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.items.[0].ownerId").isEqualTo(13);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.items.[1].id").isEqualTo(14);
        assertThat(jsonContent).extractingJsonPathStringValue("$.items.[1].name").isEqualTo("Name14");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.items.[1].ownerId").isEqualTo(15);
    }

    @Test
    void testNewItemRequestDtoJson() throws Exception {

        var dto = new NewItemRequestDto(
                "desc1",
                17L,
                LocalDateTime.of(2025, Month.JULY, 15, 17, 45, 19));

        var jsonContent = jsonNewDto.write(dto);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("desc1");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestorId").isEqualTo(17);
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo("2025-07-15T17:45:19");
    }
}
