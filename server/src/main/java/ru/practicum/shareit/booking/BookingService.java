package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

public interface BookingService {

    /**
     * Добавление бронирования
     * @param userId Id пользователя
     * @param request Данные бронирования
     * @return Новое бронирование
     */
    BookingDto addBooking(long userId, NewBookingDto request);

    /**
     * Обновление статуса бронирования
     * @param updateRequest Запрос обновления статуса
     * @return Обновленное бронирование
     */
    BookingDto updateBookingStatus(UpdateBookingDto updateRequest);

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
     * @param bookerId Id пользователя
     * @param state  Тип методики поиска
     * @return Список бронирований
     */
    List<BookingDto> getBookerBookingsOfState(long bookerId, BookingRequestState state);

    /**
     * Возвращает список бронирований предметов указанного пользователя,
     * выполненный по заданной методике
     *
     * @param ownerId Id пользователя
     * @param state  Тип методики поиска
     * @return Список бронирований
     */
    List<BookingDto> getOwnerBookingsOfState(long ownerId, BookingRequestState state);
}
