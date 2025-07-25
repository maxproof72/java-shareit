package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
public class ItemRequest {
    Long id;
    String description;
    User requestor;
    LocalDate created;
}
