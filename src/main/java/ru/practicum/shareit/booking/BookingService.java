package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.constant.BookingRequestState;
import ru.practicum.shareit.booking.dto.BookingUpdateRequest;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.util.Collection;

public interface BookingService {

    /**
     * Добавление бронирования
     * @param userId Id пользователя
     * @param request Данные бронирования
     * @return Новое бронирование
     */
    BookingDto addBooking(long userId, NewBookingRequest request);

    /**
     * Обновление статуса бронирования
     * @param updateRequest Запрос обновления статуса
     * @return Обновленное бронирование
     */
    BookingDto updateBookingStatus(BookingUpdateRequest updateRequest);

    /**
     * Поиск бронирования по предмету и пользователю
     * @param userId Id предмета
     * @param bookingId Id пользователя
     * @return Найденное бронирование
     * @throws ru.practicum.shareit.exception.NotFoundException, если бронирование не найдено
     */
    BookingDto getBooking(long userId, long bookingId);

    /**
     * Возвращает список бронирований указанного пользователя,
     * выполненный по заданной методике
     *
     * @param userId Id пользователя
     * @param state  Тип методики поиска
     * @return Список бронирований
     */
    Collection<BookingDto> getUserBookingsOfState(long userId, BookingRequestState state);

    /**
     * Возвращает список бронирований предметов указанного пользователя,
     * выполненный по заданной методике
     *
     * @param userId Id пользователя
     * @param state  Тип методики поиска
     * @return Список бронирований
     */
    Collection<BookingDto> getItemBookingsOfState(long userId, BookingRequestState state);
}
