package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.NewBookingData;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getBookingsOfUser(long userId, BookingRequestState state) {
        return get("?state={state}", userId, Map.of("state", state.name()));
    }

    public ResponseEntity<Object> getBookingsOfUserItems(long userId, BookingRequestState state) {
        return get("/owner?state={state}", userId,
                   Map.of("state", state.name()));
    }

    public ResponseEntity<Object> bookItem(long userId, NewBookingData requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> updateBookingStatus(long userId, long bookingId, boolean state) {
        return patch("/" + bookingId + "?approved={approved}", userId,
                     Map.of("approved", state), null);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }
}
