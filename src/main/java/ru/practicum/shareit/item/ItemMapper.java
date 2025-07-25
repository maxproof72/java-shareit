package ru.practicum.shareit.item;

import lombok.NonNull;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(@NonNull Item item) {
        return new ItemDto(
            item.getName(),
            item.getDescription(),
            item.getAvailable(),
            (item.getRequest() != null) ? item.getRequest().getId() : null);
    }
}
