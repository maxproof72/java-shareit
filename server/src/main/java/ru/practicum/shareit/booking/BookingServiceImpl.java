package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto addBooking(long userId, NewBookingDto request) {

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

        BookingDto dto = BookingMapper.toDto(booking);
        log.debug("Добавлено бронирование (id={}, item=({}:{}), booker={}:{}",
                dto.getId(), dto.getItem().getId(), dto.getItem().getName(),
                dto.getBooker().getId(), dto.getBooker().getName());
        return dto;
    }

    @Override
    public BookingDto updateBookingStatus(UpdateBookingDto updateRequest) {

        Booking booking = bookingRepository.findById(updateRequest.getBookingId())
                .orElseThrow(() -> new NotFoundException(
                        "Бронирование с id=%d не найдено".formatted(updateRequest.getBookingId())));
        var ownerId = booking.getItem().getOwner().getId();
        if (ownerId != updateRequest.getUserId())
            throw new ForbiddenException("Предмет id=%d бронирования не принадлежит пользователю с id=%d"
                            .formatted(updateRequest.getBookingId(), ownerId));
        booking.setStatus(updateRequest.isApproved() ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        booking = bookingRepository.save(booking);

        BookingDto dto = BookingMapper.toDto(booking);
        log.debug("Успешное обновление статуса бронирования (id={}, status={})", dto.getId(), dto.getStatus());
        return dto;
    }

    @Override
    public BookingDto getBooking(long userId, long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(
                        "Бронирование с id=%d не найдено".formatted(bookingId)));
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId)
            throw new ForbiddenException("Пользователю id=%d запрещен доступ к бронированию id=%d"
                    .formatted(userId, bookingId));

        BookingDto dto = BookingMapper.toDto(booking);
        log.debug("Успешный поиск бронирования (id={}, item={}:{}, booker={}:{})",
                dto.getId(), dto.getItem().getId(), dto.getItem().getName(),
                dto.getBooker().getId(), dto.getBooker().getName());
        return dto;
    }

    @Override
    public List<BookingDto> getBookerBookingsOfState(long bookerId, BookingRequestState state) {

        if (!userRepository.existsById(bookerId))
                throw new NotFoundException("Пользователь id=%d не найден".formatted(bookerId));
        List<Booking> bs =
            switch (state) {
                case ALL -> bookingRepository.findAllByBookerIdOrderByStart(bookerId);
                case CURRENT -> bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStart(bookerId, LocalDateTime.now());
                case FUTURE -> bookingRepository.findAllByBookerIdAndStartAfterOrderByStart(bookerId, LocalDateTime.now());
                case PAST -> bookingRepository.findAllByBookerIdAndEndBeforeOrderByStart(bookerId, LocalDateTime.now());
                case REJECTED ->
                        bookingRepository.findAllByBookerIdAndStatusOrderByStart(bookerId, BookingStatus.REJECTED);
                case WAITING ->
                        bookingRepository.findAllByBookerIdAndStatusOrderByStart(bookerId, BookingStatus.WAITING);
            };
        log.debug("Запрос {} бронирований вернул {} записей", state, bs.size());
        return bs.stream().map(BookingMapper::toDto).toList();
    }

    @Override
    public List<BookingDto> getOwnerBookingsOfState(long ownerId, BookingRequestState state) {
        if (!userRepository.existsById(ownerId))
            throw new NotFoundException("Пользователь id=%d не найден".formatted(ownerId));
        List<Booking> bs =
                switch (state) {
                    case ALL -> bookingRepository.findAllByItemOwnerIdOrderByStart(ownerId);
                    case CURRENT -> bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStart(
                            ownerId, LocalDateTime.now());
                    case FUTURE -> bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStart(
                            ownerId, LocalDateTime.now());
                    case PAST -> bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStart(ownerId,
                            LocalDateTime.now());
                    case REJECTED ->
                            bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.REJECTED);
                    case WAITING ->
                            bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.WAITING);
                };
        log.debug("Запрос {} бронирований вещей пользователя {} вернул {} записей", state, ownerId, bs.size());
        return bs.stream().map(BookingMapper::toDto).toList();
    }
}
