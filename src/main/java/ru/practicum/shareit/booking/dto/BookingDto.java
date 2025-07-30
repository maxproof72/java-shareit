package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class BookingDto {

    LocalDate start;
    LocalDate end;
    Long item;
    Long booker;
    BookingStatus status;
}
