package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.Collection;

import static ru.practicum.shareit.ShareItServer.SHARER_USER_ID_HEADER;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;


    @PostMapping()
    public BookingDto addBooking(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                 @RequestBody NewBookingDto bookingRequest) {

        log.info("Запрос POST создания нового бронирования (userId={}, booking={})", userId, bookingRequest);
        return bookingService.addBooking(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                          @PathVariable("bookingId") long bookingId,
                                          @RequestParam boolean approved) {

        log.info("Запрос PATCH обновления бронирования (userId={}, booking={}, approved={})", userId, bookingId, approved);
        var updateRequest = new UpdateBookingDto(userId, bookingId, approved);
        return bookingService.updateBookingStatus(updateRequest);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                 @PathVariable("bookingId") long bookingId) {

        log.info("Запрос GET получения информации о бронировании (userId={}, bookingId={})", userId, bookingId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping()
    public Collection<BookingDto> getUserBookingsOfState(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                                         @RequestParam(required = false, defaultValue = "ALL") BookingRequestState state) {

        log.info("Запрос GET перечня бронирований пользователя (userId={}, state={})", userId, state);
        return bookingService.getBookerBookingsOfState(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getItemBookingsOfState(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                                   @RequestParam(required = false, defaultValue = "ALL") BookingRequestState state) {

        log.info("Запрос GET перечня бронирований предметов пользователя (userId={}, state={})", userId, state);
        return bookingService.getOwnerBookingsOfState(userId, state);
    }
}
