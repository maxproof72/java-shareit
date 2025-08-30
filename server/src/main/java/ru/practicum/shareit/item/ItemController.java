package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Collection;

import static ru.practicum.shareit.ShareItServer.SHARER_USER_ID_HEADER;


@RestController
@Validated
@Slf4j
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                           @RequestBody NewItemDto itemRequest) {

        log.info("Запрос POST создания нового предмета (userId={}, item={})", userId, itemRequest);
        itemRequest.setOwnerId(userId);
        return itemService.addItem(itemRequest);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                              @RequestBody UpdateItemDto itemRequest,
                              @PathVariable("itemId") long itemId) {

        log.info("Запрос PATCH обновления предмета (userId={}, itemId={}, item={})", userId, itemId, itemRequest);
        itemRequest.setId(itemId);
        itemRequest.setOwnerId(userId);
        return itemService.updateItem(itemRequest);
    }

    @GetMapping("/{itemId}")
    public ItemWithCommentsDto getItem(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                       @PathVariable("itemId") long itemId) {

        log.info("Запрос GET информации о предмете (userId={}, itemId={})", userId, itemId);
        return itemService.getItem(itemId, userId);
    }

    @GetMapping()
    public Collection<ItemWithCommentsDto> getItemsOfUser(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId) {

        log.info("Запрос GET перечня предметов пользователя (userId={})", userId);
        return itemService.getItemsOfUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                           @RequestParam(name = "text") String searchPattern) {

        log.info("Запрос GET поиска предметов (userId={}, search='{}')", userId, searchPattern);
        return itemService.searchItems(userId, searchPattern);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                 @RequestBody NewCommentDto commentRequest,
                                 @PathVariable("itemId") long itemId) {

        log.info("Запрос POST добавления комментария (userId={}, itemId={}, text='{}')",
                userId, itemId, commentRequest.getText().substring(0, Math.min(commentRequest.getText().length(), 20)));
        commentRequest.setUserId(userId);
        commentRequest.setItemId(itemId);
        return itemService.addComment(commentRequest);
    }
}
