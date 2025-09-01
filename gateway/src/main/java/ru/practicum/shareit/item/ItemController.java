package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.NewCommentData;
import ru.practicum.shareit.item.dto.NewItemData;
import ru.practicum.shareit.item.dto.UpdateItemData;

import static ru.practicum.shareit.ShareItGateway.SHARER_USER_ID_HEADER;


@RestController
@Validated
@Slf4j
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                          @Valid @RequestBody NewItemData itemRequest) {

        log.info("POST request to create new item (userId={}, item={})", userId, itemRequest);
        return itemClient.addItem(userId, itemRequest);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                             @Valid @RequestBody UpdateItemData itemRequest,
                                             @NotNull @PathVariable("itemId") long itemId) {

        log.info("PATCH request for item update (userId={}, itemId={}, item={})", userId, itemId, itemRequest);
        return itemClient.updateItem(userId, itemId, itemRequest);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                          @NotNull @PathVariable("itemId") long itemId) {

        log.info("GET request for information of item (userId={}, itemId={})", userId, itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping()
    public ResponseEntity<Object> getItemsOfUser(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId) {

        log.info("GET request of user items list (userId={})", userId);
        return itemClient.getItemsOfUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                              @RequestParam(name = "text") String searchPattern) {

        log.info("GET request of search for items (userId={}, search='{}')", userId, searchPattern);
        return itemClient.searchItems(userId, searchPattern);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                             @Valid @RequestBody NewCommentData commentRequest,
                                             @NotNull @PathVariable("itemId") long itemId) {

        log.info("POST request to create a comment (userId={}, itemId={}, text='{}')",
                userId, itemId, commentRequest.getText().substring(0, Math.min(commentRequest.getText().length(), 20)));
        return itemClient.addComment(userId, itemId, commentRequest);
    }
}
