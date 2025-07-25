package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.interfaces.Marker;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class BookingDto {

    @FutureOrPresent(message = "Заявка задним числом недопустима")
    LocalDate start;

    @Future(message = "Заявка задним числом недопустима")
    LocalDate end;

    @NotNull(groups = {Marker.OnCreate.class, Marker.OnUpdate.class},
             message = "Пустая ссылка на сущность недопустима")
    Long item;

    @NotNull(groups = {Marker.OnCreate.class, Marker.OnUpdate.class},
            message = "Пустая ссылка на заявителя недопустима")
    Long booker;

    BookingStatus status;
}
