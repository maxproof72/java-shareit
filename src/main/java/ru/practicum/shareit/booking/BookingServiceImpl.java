package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestState;
import ru.practicum.shareit.booking.dto.BookingUpdateRequest;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto addBooking(long userId, NewBookingRequest request) {

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException(
                        "Предмет id=%d не найден".formatted(request.getItemId())));
        if (!item.isAvailable()) {
            throw new IllegalStateException("Предмет id=%d недоступен для бронирования".formatted(request.getItemId()));
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь id=%d не найден".formatted(userId)));
        Booking booking = BookingMapper.toBooking(request);
        booking.setItem(item);
        booking.setBooker(user);
        booking = bookingRepository.save(booking);
        log.info("Добавлено бронирование {}", booking);
        return BookingMapper.toDto(booking);
    }

    @Override
    public BookingDto updateBookingStatus(BookingUpdateRequest updateRequest) {

        Booking booking = bookingRepository.findById(updateRequest.getBookingId())
                .orElseThrow(() -> new NotFoundException(
                        "Бронирование с id=%d не найдено".formatted(updateRequest.getBookingId())));
        var ownerId = booking.getItem().getOwner().getId();
        if (ownerId != updateRequest.getUserId())
            throw new ForbiddenException("Предмет id=%d бронирования не принадлежит пользователю с id=%d"
                            .formatted(updateRequest.getBookingId(), ownerId));
        booking.setStatus(updateRequest.isApproved() ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        booking = bookingRepository.save(booking);
        log.info("Успешное обновление статуса бронирования {}", booking);
        return BookingMapper.toDto(booking);
    }

    @Override
    public BookingDto getBooking(long userId, long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(
                        "Бронирование с id=%d не найдено".formatted(bookingId)));
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId)
            throw new ForbiddenException("Пользователю id=%d запрещен доступ к бронированию id=%d"
                    .formatted(userId, bookingId));
        log.info("Успешный поиск бронирования {}", booking);
        return BookingMapper.toDto(booking);
    }

    @Override
    public Collection<BookingDto> getUserBookingsOfState(long userId, BookingRequestState state) {

        if (!userRepository.existsById(userId))
                throw new NotFoundException("Пользователь id=%d не найден".formatted(userId));
        List<Booking> bs =
            switch (state) {
                case ALL -> bookingRepository.findAllByBookerId(userId);
                case CURRENT -> bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStart(userId, LocalDateTime.now());
                case FUTURE -> bookingRepository.findAllByBookerIdAndStartAfterOrderByStart(userId, LocalDateTime.now());
                case PAST -> bookingRepository.findAllByBookerIdAndEndBeforeOrderByStart(userId, LocalDateTime.now());
                case REJECTED ->
                        bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED);
                case WAITING ->
                        bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.WAITING);
            };
        log.info("Запрос {} бронирований вернул {} записей", state, bs.size());
        return bs.stream().map(BookingMapper::toDto).toList();
    }

    @Override
    public Collection<BookingDto> getItemBookingsOfState(long userId, BookingRequestState state) {
        if (!userRepository.existsById(userId))
            throw new NotFoundException("Пользователь id=%d не найден".formatted(userId));
        List<Booking> bs =
                switch (state) {
                    case ALL -> bookingRepository.findAllByItemOwnerIdOrderByStart(userId);
                    case CURRENT -> bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStart(
                            userId, LocalDateTime.now());
                    case FUTURE -> bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStart(
                            userId, LocalDateTime.now());
                    case PAST -> bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStart(userId,
                            LocalDateTime.now());
                    case REJECTED ->
                            bookingRepository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED);
                    case WAITING ->
                            bookingRepository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.WAITING);
                };
        log.info("Запрос {} бронирований вещей пользователя {} вернул {} записей", state, userId, bs.size());
        return bs.stream().map(BookingMapper::toDto).toList();
    }
}
