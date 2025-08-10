package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestState;
import ru.practicum.shareit.booking.dto.BookingUpdateRequest;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, NewBookingRequest request);

    BookingDto updateBookingStatus(BookingUpdateRequest updateRequest);

    BookingDto getBooking(long userId, long bookingId);

    List<BookingDto> getUserBookingsOfState(long userId, BookingRequestState state);

    List<BookingDto> getItemBookingsOfState(long userId, BookingRequestState state);
}
