package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ShareItServer.FORMATTER;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController controller;
    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
    private MockMvc mvc;
    private BookingDto dto;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        LocalDateTime now = LocalDateTime.now();
        dto = new BookingDto(1L, now.plusDays(1), now.plusDays(3),
                new ItemDto(2L, "Small thing", "Tiny small thing", true, null),
              new UserDto(3L, "User1", "user1@mail.com"),
        BookingStatus.APPROVED);
    }

    @Test
    void testAddBooking() throws Exception {

        Mockito.when(bookingService.addBooking(Mockito.anyLong(), Mockito.any()))
                .thenReturn(dto);

        mvc.perform(post("/bookings")
                .header(ShareItServer.SHARER_USER_ID_HEADER,3L)
                .content(mapper.writeValueAsString(new NewBookingDto(
                        null, dto.getStart(), dto.getEnd(), 2L)))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
            .andExpect(jsonPath("$.start", is(FORMATTER.format(dto.getStart()))))
            .andExpect(jsonPath("$.end", is(FORMATTER.format(dto.getEnd()))))
            .andExpect(jsonPath("$.item.id", is(dto.getItem().getId()), Long.class))
            .andExpect(jsonPath("$.booker.id", is(dto.getBooker().getId()), Long.class));
    }

    @Test
    void testUpdateBookingStatus() throws Exception {

        Mockito.when(bookingService.updateBookingStatus(Mockito.any()))
                .thenReturn(dto);
        mvc.perform(patch("/bookings/{id}?approved=true", 1L)
                        .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(new UpdateBookingDto(
                                3L, 1L, true)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(dto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(dto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.toString())));
    }

    @Test
    void testGetBooking() throws Exception {

        Mockito.when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong()))
               .thenReturn(dto);

        mvc.perform(get("/bookings/{id}", 1L)
                    .header(ShareItServer.SHARER_USER_ID_HEADER,3L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(dto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(dto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.toString())));
    }

    @Test
    void testGetUserBookingOfState() throws Exception {
        Mockito.when(bookingService.getBookerBookingsOfState(Mockito.anyLong(), Mockito.any(BookingRequestState.class)))
                .thenReturn(List.of(dto));

        mvc.perform(get("/bookings?state=ALL")
                        .header(ShareItServer.SHARER_USER_ID_HEADER,3L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].item.id", is(dto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.id", is(dto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(BookingStatus.APPROVED.toString())));
    }

    @Test
    void testGetItemBookingOfState() throws Exception {
        Mockito.when(bookingService.getOwnerBookingsOfState(Mockito.anyLong(), Mockito.any(BookingRequestState.class)))
                .thenReturn(List.of(dto));
        mvc.perform(get("/bookings/owner?state=ALL")
                        .header(ShareItServer.SHARER_USER_ID_HEADER,3L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].item.id", is(dto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.id", is(dto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(BookingStatus.APPROVED.toString())));
    }
}
