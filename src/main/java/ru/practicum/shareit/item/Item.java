package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
@NoArgsConstructor
public class Item {
    long id;
    String name;
    String description;
    Boolean available;
    User owner;
    ItemRequest request;
}
