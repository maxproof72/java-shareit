package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

public interface ItemService {

    ItemDto getItem(long itemId, long userId);

    ItemDto addItem(NewItemRequest newItemRequest);

    ItemDto updateItem(UpdateItemRequest updateItemRequest);

    Collection<ItemDto> getItemsOfUser(long userId);

    Collection<ItemDto> searchItems(long userId, String searchPattern);
}
