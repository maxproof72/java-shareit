package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemDtoJsonTest {

    private final JacksonTester<ItemDto> jsonDto;
    private final JacksonTester<ItemWithCommentsDto> jsonCommentsDto;
    private final JacksonTester<NewItemDto> jsonNewDto;
    private final JacksonTester<UpdateItemDto> jsonUpdateDto;

    @Test
    void testItemDtoJson() throws Exception {

        var dto = new ItemDto(
                1L,
                "MyItem",
                "Great thing",
                true,
                7L
        );

        var jsonContent = jsonDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("MyItem");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Great thing");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.request").isEqualTo(7);
    }

    @Test
    void testItemWithCommentsDtoJson() throws Exception {

        var dto = new ItemWithCommentsDto(
                1L,
                "MyItem",
                "Great thing",
                true,
                LocalDateTime.of(2025, Month.JULY, 15, 17, 45, 19),
                LocalDateTime.of(2025, Month.SEPTEMBER, 23, 17, 45, 19),
                7L,
                List.of(
                        new CommentDto(5L, "text5", "myself", LocalDateTime.of(2025, Month.AUGUST, 8, 15, 45, 12)),
                        new CommentDto(9L, "text9", "user99", LocalDateTime.of(2025, Month.AUGUST, 9, 11, 15, 32))
                )
        );

        var jsonContent = jsonCommentsDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("MyItem");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Great thing");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.request").isEqualTo(7);
        assertThat(jsonContent).extractingJsonPathStringValue("$.lastBooking").isEqualTo("2025-07-15T17:45:19");
        assertThat(jsonContent).extractingJsonPathStringValue("$.nextBooking").isEqualTo("2025-09-23T17:45:19");
        assertThat(jsonContent).extractingJsonPathArrayValue("$.comments").hasSize(2);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.comments.[0].id").isEqualTo(5);
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments.[0].text").isEqualTo("text5");
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments.[0].authorName").isEqualTo("myself");
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments.[0].created").isEqualTo("2025-08-08T15:45:12");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.comments.[1].id").isEqualTo(9);
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments.[1].text").isEqualTo("text9");
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments.[1].authorName").isEqualTo("user99");
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments.[1].created").isEqualTo("2025-08-09T11:15:32");
    }

    @Test
    void testNewItemDtoJson() throws Exception {

        var dto = new NewItemDto("item1", "My item 1", true, 17L, 99L);

        var jsonContent = jsonNewDto.write(dto);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("item1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("My item 1");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.ownerId").isEqualTo(17);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(99);
    }

    @Test
    void testUpdateItemDtoJson() throws Exception {

        var dto = new UpdateItemDto(5L, "item1", "My item 1", true, 17L);

        var jsonContent = jsonUpdateDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(5);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("item1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("My item 1");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.ownerId").isEqualTo(17);
    }
}
