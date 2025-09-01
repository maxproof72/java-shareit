package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.NewBookingData;

import static ru.practicum.shareit.ShareItGateway.SHARER_USER_ID_HEADER;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;


    @PostMapping()
    public ResponseEntity<Object> addBooking(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                             @Valid @RequestBody NewBookingData bookingRequest) {

        log.info("POST request to create a new booking (userId={}, booking={})", userId, bookingRequest);
        return bookingClient.bookItem(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                                      @PathVariable("bookingId") long bookingId,
                                                      @RequestParam boolean approved) {

        log.info("PATCH request for booking status update (userId={}, booking={}, approved={})", userId, bookingId, approved);
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                             @PathVariable("bookingId") long bookingId) {

        log.info("GET request for information about booking (userId={}, bookingId={})", userId, bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getUserBookingsOfState(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                                         @RequestParam(required = false, defaultValue = "ALL") String stateParam) {

        BookingRequestState state = BookingRequestState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("GET request of user booking list (userId={}, state={})", userId, state);
        return bookingClient.getBookingsOfUser(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getItemBookingsOfState(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                                         @RequestParam(required = false, defaultValue = "ALL") String stateParam) {

        BookingRequestState state = BookingRequestState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("GET request of user items booking list (userId={}, state={})", userId, state);
        return bookingClient.getBookingsOfUserItems(userId, state);
    }
}
