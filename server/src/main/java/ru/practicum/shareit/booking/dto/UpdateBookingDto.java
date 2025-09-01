package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateBookingDto {

    private long userId;
    private long bookingId;
    private boolean approved;
}
