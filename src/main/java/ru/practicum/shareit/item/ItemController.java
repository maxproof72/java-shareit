package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    @Qualifier("ItemDbService")
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@NotNull @RequestHeader(name = "X-Sharer-User-Id") long userId,
                           @Valid @RequestBody NewItemRequest itemRequest) {

        itemRequest.setOwnerId(userId);
        return itemService.addItem(itemRequest);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@NotNull @RequestHeader(name = "X-Sharer-User-Id") long userId,
                              @Valid @RequestBody UpdateItemRequest itemRequest,
                              @NotNull @PathVariable("itemId") long itemId) {

        itemRequest.setId(itemId);
        itemRequest.setOwnerId(userId);
        return itemService.updateItem(itemRequest);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@NotNull @RequestHeader(name = "X-Sharer-User-Id") long userId,
                              @NotNull @PathVariable("itemId") long itemId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping()
    public Collection<ItemDto> getItemsOfUser(@NotNull @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return itemService.getItemsOfUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@NotNull @RequestHeader(name = "X-Sharer-User-Id") long userId,
                                           @RequestParam(name = "text") String searchPattern) {
        return itemService.searchItems(userId, searchPattern);
    }
}
