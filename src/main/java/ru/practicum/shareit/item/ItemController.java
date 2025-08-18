package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

import static ru.practicum.shareit.ShareItApp.SHARER_USER_ID_HEADER;


@RestController
@Validated
@Slf4j
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                           @Valid @RequestBody NewItemRequest itemRequest) {

        log.info("Запрос POST создания нового предмета (userId={}, item={})", userId, itemRequest);
        itemRequest.setOwnerId(userId);
        return itemService.addItem(itemRequest);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                              @Valid @RequestBody UpdateItemRequest itemRequest,
                              @NotNull @PathVariable("itemId") long itemId) {

        log.info("Запрос PATCH обновления предмета (userId={}, itemId={}, item={})", userId, itemId, itemRequest);
        itemRequest.setId(itemId);
        itemRequest.setOwnerId(userId);
        return itemService.updateItem(itemRequest);
    }

    @GetMapping("/{itemId}")
    public ItemWithCommentsDto getItem(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                       @NotNull @PathVariable("itemId") long itemId) {

        log.info("Запрос GET информации о предмете (userId={}, itemId={})", userId, itemId);
        return itemService.getItem(itemId, userId);
    }

    @GetMapping()
    public Collection<ItemWithCommentsDto> getItemsOfUser(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId) {

        log.info("Запрос GET перечня предметов пользователя (userId={})", userId);
        return itemService.getItemsOfUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                           @RequestParam(name = "text") String searchPattern) {

        log.info("Запрос GET поиска предметов (userId={}, search='{}')", userId, searchPattern);
        return itemService.searchItems(userId, searchPattern);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                 @Valid @RequestBody NewCommentRequest commentRequest,
                                 @NotNull @PathVariable("itemId") long itemId) {

        log.info("Запрос POST добавления комментария (userId={}, itemId={}, text='{}')",
                userId, itemId, commentRequest.getText().substring(0, Math.min(commentRequest.getText().length(), 20)));
        commentRequest.setUserId(userId);
        commentRequest.setItemId(itemId);
        return itemService.addComment(commentRequest);
    }
}
