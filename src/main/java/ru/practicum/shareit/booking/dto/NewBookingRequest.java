package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBookingRequest {

    @Null
    Long id;

    @NotNull
    @Future
    LocalDateTime start;

    @NotNull
    @Future
    LocalDateTime end;

    @NotNull
    Long itemId;
}
