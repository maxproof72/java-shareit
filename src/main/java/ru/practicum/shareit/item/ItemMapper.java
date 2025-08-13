package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.dto.NewItemRequest;

@UtilityClass
public class ItemMapper {

    public static ItemDto toDto(@NonNull Item item) {
        return new ItemDto(
             item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
            (item.getRequest() != null) ? item.getRequest().getId() : null);
    }

    public static ItemWithCommentsDto toBookingDto(@NonNull Item item) {
        var itemWithComments = new ItemWithCommentsDto();
        itemWithComments.setId(item.getId());
        itemWithComments.setName(item.getName());
        itemWithComments.setDescription(item.getDescription());
        itemWithComments.setAvailable(item.isAvailable());
        if (item.getRequest() != null)
            itemWithComments.setRequest(item.getRequest().getId());
        return itemWithComments;
    }

    public static Item toItem(@NonNull NewItemRequest itemRequest) {
        Item item = new Item();
        item.setName(itemRequest.getName());
        item.setDescription(itemRequest.getDescription());
        item.setAvailable(itemRequest.getAvailable());
        return item;
    }
}
