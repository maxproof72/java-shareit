package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDate;
import ru.practicum.shareit.comments.Comment;
import ru.practicum.shareit.comments.CommentMapper;
import ru.practicum.shareit.comments.CommentRepository;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.NewCommentRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public ItemWithCommentsDto getItem(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(
                        "Не найден предмет id=%d".formatted(itemId)));
        ItemWithCommentsDto dto = ItemMapper.toBookingDto(item);
        if (userId == item.getOwner().getId()) {
            var now = LocalDateTime.now();
            bookingRepository.findLastBookingDate(item, now)
                    .ifPresent(dto::setLastBooking);
            bookingRepository.findNextBookingDate(item, now)
                    .ifPresent(dto::setNextBooking);
        }
        dto.setComments(commentRepository.findAllByItem(item).stream()
                .map(CommentMapper::toDto)
                .toList());
        log.debug("Возвращается информация о предмете {}", dto);
        return dto;
    }

    @Override
    public ItemDto addItem(NewItemRequest newItemRequest) {

        Item item = ItemMapper.toItem(newItemRequest);
        User user = userRepository.findById(newItemRequest.getOwnerId())
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь id=%d не найден".formatted(newItemRequest.getOwnerId())));
        item.setOwner(user);
        item = itemRepository.save(item);
        log.debug("Добавлен предмет (id={}:'{}', owner=({}:'{}')",
                item.getId(), item.getName(), item.getOwner().getId(), item.getOwner().getName());
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto updateItem(UpdateItemRequest updateItemRequest) {
        Item item = itemRepository.findByIdAndOwnerId(updateItemRequest.getId(), updateItemRequest.getOwnerId())
                .orElseThrow(() -> new NotFoundException(
                        "Не найден предмет id=%d пользователя id=%d".formatted(updateItemRequest.getId(), updateItemRequest.getOwnerId())));
        if (updateItemRequest.getName() != null)
            item.setName(updateItemRequest.getName());
        if (updateItemRequest.getDescription() != null)
            item.setDescription(updateItemRequest.getDescription());
        if (updateItemRequest.getAvailable() != null)
            item.setAvailable(updateItemRequest.getAvailable());
        item = itemRepository.save(item);
        log.debug("Обновлен предмет (id={}:'{}', owner=({}:'{}')",
                item.getId(), item.getName(), item.getOwner().getId(), item.getOwner().getName());
        return ItemMapper.toDto(item);
    }

    @Override
    public Collection<ItemWithCommentsDto> getItemsOfUser(long userId) {

        var it = itemRepository.findItemsByOwnerId(userId);
        var now = LocalDateTime.now();
        Map<Long, LocalDateTime> lastBookings = bookingRepository.findLastBookingDateForItems(it, now)
                .stream()
                .collect(Collectors.toUnmodifiableMap(BookingDate::getItem, BookingDate::getDate));
        Map<Long, LocalDateTime> nextBookings = bookingRepository.findNextBookingDateForItems(it, now)
                .stream()
                .collect(Collectors.toUnmodifiableMap(BookingDate::getItem, BookingDate::getDate));
        List<ItemWithCommentsDto> dtoList = it.stream()
                .map(ItemMapper::toBookingDto)
                .peek(dto -> {
                    dto.setLastBooking(lastBookings.get(dto.getId()));
                    dto.setNextBooking(nextBookings.get(dto.getId()));
                }).toList();
        log.debug("Запрос предметов пользователя с id={} вернул {} записей", userId, dtoList.size());
        return dtoList;
    }

    @Override
    public Collection<ItemDto> searchItems(long userId, @NonNull String searchPattern) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь id=%d не найден".formatted(userId)));
        var it = itemRepository.findItemsByOwnerIdAndPattern(user, searchPattern.toLowerCase());
        log.debug("Поиск предметов {} пользователя с id={} вернул {} записей", searchPattern, userId, it.size());
        return it.stream().map(ItemMapper::toDto).toList();
    }

    @Override
    @Transactional
    public CommentDto addComment(@NonNull NewCommentRequest request) {

        // Проверка, что пользователь брал эту вещь в аренду
        if (!bookingRepository.existsBookingByItemIdAndBookerIdAndStatusIsAndEndBefore(
                request.getItemId(),
                request.getUserId(),
                BookingStatus.APPROVED,
                LocalDateTime.now()
        )) {
            throw new IllegalStateException("Недопустимые условия комментария");
        }
        // Далее наличие предмета и пользователя можно не проверять,
        // поскольку найдено подходящее бронирование
        Comment comment = CommentMapper.toComment(request);
        comment.setItem(itemRepository.findById(request.getItemId()).orElse(null));
        comment.setAuthor(userRepository.findById(request.getUserId()).orElse(null));
        comment = commentRepository.save(comment);
        log.debug("Добавлен комментарий (id={}, item=({}:'{}')",
                comment.getId(), comment.getItem().getId(), comment.getItem().getName());
        return CommentMapper.toDto(comment);
    }
}
