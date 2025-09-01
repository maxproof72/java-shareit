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
import ru.practicum.shareit.comments.dto.NewCommentDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
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
    private final ItemRequestRepository itemRequestRepository;

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
    public ItemDto addItem(NewItemDto newItemDto) {

        Item item = ItemMapper.toItem(newItemDto);

        // Определение пользователя
        User user = userRepository.findById(newItemDto.getOwnerId())
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь id=%d не найден".formatted(newItemDto.getOwnerId())));
        item.setOwner(user);

        // Определение заявки
        if (newItemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(newItemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Заявка с id=%d не найдена"
                            .formatted(newItemDto.getRequestId())));
            item.setRequest(itemRequest);
        }

        // Сохранение предмета
        item = itemRepository.save(item);
        log.debug("Добавлен предмет (id={}:'{}', owner=({}:'{}')",
                item.getId(), item.getName(), item.getOwner().getId(), item.getOwner().getName());
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto updateItem(UpdateItemDto updateItemDto) {
        Item item = itemRepository.findByIdAndOwnerId(updateItemDto.getId(), updateItemDto.getOwnerId())
                .orElseThrow(() -> new NotFoundException(
                        "Не найден предмет id=%d пользователя id=%d".formatted(updateItemDto.getId(), updateItemDto.getOwnerId())));
        if (updateItemDto.getName() != null)
            item.setName(updateItemDto.getName());
        if (updateItemDto.getDescription() != null)
            item.setDescription(updateItemDto.getDescription());
        if (updateItemDto.getAvailable() != null)
            item.setAvailable(updateItemDto.getAvailable());
        item = itemRepository.save(item);
        log.debug("Обновлен предмет (id={}:'{}', owner=({}:'{}')",
                item.getId(), item.getName(), item.getOwner().getId(), item.getOwner().getName());
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemWithCommentsDto> getItemsOfUser(long userId) {

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
    public List<ItemDto> searchItems(long userId, @NonNull String searchPattern) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь id=%d не найден".formatted(userId)));
        var it = itemRepository.findItemsByOwnerIdAndPattern(user, searchPattern.toLowerCase());
        log.debug("Поиск предметов {} пользователя с id={} вернул {} записей", searchPattern, userId, it.size());
        return it.stream().map(ItemMapper::toDto).toList();
    }

    @Override
    @Transactional
    public CommentDto addComment(@NonNull NewCommentDto request) {

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
