package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.booking.dto.NewBookingData;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @InjectMocks
    private BookingController controller;
    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
    private MockMvc mvc;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testAddBooking() throws Exception {

//        Mockito.when(bookingClient.bookItem(Mockito.anyLong(), Mockito.any(NewBookingData.class)))
//                        .thenThrow(AssertionError.class);

        // Прошедшее время старта
        mvc.perform(post("/bookings")
                .header(ShareItGateway.SHARER_USER_ID_HEADER,3L)
                .content(mapper.writeValueAsString(new NewBookingData(
                        null,
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        2L)))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());

        // Прошедшее время окончания
        mvc.perform(post("/bookings")
                .header(ShareItGateway.SHARER_USER_ID_HEADER,3L)
                .content(mapper.writeValueAsString(new NewBookingData(
                        null,
                        LocalDateTime.now().plusDays(2),
                        LocalDateTime.now().minusDays(1),
                        2L)))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());

        // Не указан предмет
        mvc.perform(post("/bookings")
                .header(ShareItGateway.SHARER_USER_ID_HEADER,3L)
                .content(mapper.writeValueAsString(new NewBookingData(
                        null,
                        LocalDateTime.now().plusDays(2),
                        LocalDateTime.now().plusDays(5),
                        null)))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());

        // Не указан пользователь
        mvc.perform(post("/bookings")
                .content(mapper.writeValueAsString(new NewBookingData(
                        null,
                        LocalDateTime.now().plusDays(2),
                        LocalDateTime.now().plusDays(5),
                        null)))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }


    @Test
    void testUpdateBookingStatus() throws Exception {

        // Нет approved
        mvc.perform(patch("/bookings/{id}", 1L)
                        .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Плохой approved
        mvc.perform(patch("/bookings/{id}?approved=123", 1L)
                        .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Нет хедера
        mvc.perform(patch("/bookings/{id}?approved=true", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Плохой id бронирования
        mvc.perform(patch("/bookings/{id}?approved=true", "XY")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetBooking() throws Exception {

        // Нет хедера
        mvc.perform(get("/bookings/{id}", 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Плохой id бронирования
        mvc.perform(get("/bookings/{id}", "XY")
                    .header(ShareItGateway.SHARER_USER_ID_HEADER,3L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetUserBookingOfState() throws Exception {

        // Нет хедера
        mvc.perform(get("/bookings?state=ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetItemBookingOfState() throws Exception {

        // Нет хедера
        mvc.perform(get("/bookings/owner?state=ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
