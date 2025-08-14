package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.constant.BookingRequestState;
import ru.practicum.shareit.booking.dto.BookingUpdateRequest;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.util.Collection;

import static ru.practicum.shareit.ShareItApp.SHARER_USER_ID_HEADER;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;


    @PostMapping()
    public BookingDto addBooking(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                 @Valid @RequestBody NewBookingRequest bookingRequest) {

        log.info("Запрос POST создания нового бронирования (userId={}, booking={})", userId, bookingRequest);
        return bookingService.addBooking(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                          @PathVariable("bookingId") long bookingId,
                                          @RequestParam boolean approved) {

        log.info("Запрос PATCH обновления бронирования (userId={}, booking={}, approved={})", userId, bookingId, approved);
        var updateRequest = new BookingUpdateRequest(userId, bookingId, approved);
        return bookingService.updateBookingStatus(updateRequest);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                 @PathVariable("bookingId") long bookingId) {

        log.info("Запрос GET получения информации о бронировании (userId={}, bookingId={})", userId, bookingId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping()
    public Collection<BookingDto> getUserBookingsOfState(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                                         @RequestParam(required = false, defaultValue = "ALL") BookingRequestState state) {

        log.info("Запрос GET перечня бронирований пользователя (userId={}, state={})", userId, state);
        return bookingService.getUserBookingsOfState(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getItemBookingsOfState(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                                   @RequestParam(required = false, defaultValue = "ALL") BookingRequestState state) {

        log.info("Запрос GET перечня бронирований предметов пользователя (userId={}, state={})", userId, state);
        return bookingService.getItemBookingsOfState(userId, state);
    }
}
