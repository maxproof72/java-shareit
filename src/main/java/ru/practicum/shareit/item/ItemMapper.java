package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

@UtilityClass
public class ItemMapper {

    public static ItemDto toDto(@NonNull Item item) {
        return new ItemDto(
             item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
            (item.getRequest() != null) ? item.getRequest().getId() : null);
    }

    public static Item toItem(@NonNull NewItemRequest itemRequest) {
        Item item = new Item();
        item.setName(itemRequest.getName());
        item.setDescription(itemRequest.getDescription());
        item.setAvailable(itemRequest.getAvailable());
        return item;
    }

    public static Item toItem(@NonNull UpdateItemRequest itemRequest) {
        Item item = new Item();
        item.setId(itemRequest.getId());
        item.setName(itemRequest.getName());
        item.setDescription(itemRequest.getDescription());
        item.setAvailable(itemRequest.getAvailable());
        return item;
    }
}
