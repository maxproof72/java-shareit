package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestState;
import ru.practicum.shareit.booking.dto.BookingUpdateRequest;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.util.Collection;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    @PostMapping()
    public BookingDto addBooking(@NotNull @RequestHeader(name = "X-Sharer-User-Id") long userId,
                                 @Valid @RequestBody NewBookingRequest bookingRequest) {
        return bookingService.addBooking(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@NotNull @RequestHeader(name = "X-Sharer-User-Id") long userId,
                                          @PathVariable("bookingId") long bookingId,
                                          @RequestParam boolean approved) {
        var updateRequest = new BookingUpdateRequest(userId, bookingId, approved);
        return bookingService.updateBookingStatus(updateRequest);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@NotNull @RequestHeader(name = "X-Sharer-User-Id") long userId,
                                 @PathVariable("bookingId") long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping()
    public Collection<BookingDto> getUserBookingsOfState(@NotNull @RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                         @RequestParam(required = false, defaultValue = "ALL") BookingRequestState state) {
        return bookingService.getUserBookingsOfState(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getItemBookingsOfState(@NotNull @RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                   @RequestParam(required = false, defaultValue = "ALL") BookingRequestState state) {
        return bookingService.getItemBookingsOfState(userId, state);
    }
}
