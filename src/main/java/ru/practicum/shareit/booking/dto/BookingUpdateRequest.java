package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingUpdateRequest {
    long userId;
    long bookingId;
    boolean approved;
}
