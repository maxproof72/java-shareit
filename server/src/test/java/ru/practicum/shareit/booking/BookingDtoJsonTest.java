package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingDtoJsonTest {

    private final JacksonTester<BookingDto> jsonDto;
    private final JacksonTester<NewBookingDto> jsonNewDto;
    private final JacksonTester<UpdateBookingDto> jsonUpdateDto;

    @Test
    void testBookingDtoJson() throws Exception {

        BookingDto dto = new BookingDto(
                1L,
                LocalDateTime.of(2025, Month.JULY, 15, 17, 45, 19),
                LocalDateTime.of(2025, Month.SEPTEMBER, 23, 17, 45, 19),
                new ItemDto(2L, "Item2", "Item 2", true, null),
                new UserDto(3L, "User3", "user3@mail.com"),
                BookingStatus.APPROVED
        );

        var jsonContent = jsonDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo("2025-07-15T17:45:19");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo("2025-09-23T17:45:19");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.id").isEqualTo(2);
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.name").isEqualTo("Item2");
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.description").isEqualTo("Item 2");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.booker.id").isEqualTo(3);
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.name").isEqualTo("User3");
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.email").isEqualTo("user3@mail.com");
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo(BookingStatus.APPROVED.name());
    }

    @Test
    void testNewBookingDtoJson() throws Exception {

        NewBookingDto dto = new NewBookingDto(
                1L,
                LocalDateTime.of(2025, Month.JULY, 15, 17, 45, 19),
                LocalDateTime.of(2025, Month.SEPTEMBER, 23, 17, 45, 19),
                2L
        );

        var jsonContent = jsonNewDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo("2025-07-15T17:45:19");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo("2025-09-23T17:45:19");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);
    }

    @Test
    void testUpdateBookingDtoJson() throws Exception {

        UpdateBookingDto dto = new UpdateBookingDto(1L, 2L, true);

        var jsonContent = jsonUpdateDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.userId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.bookingId").isEqualTo(2);
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.approved").isEqualTo(true);
    }
}
