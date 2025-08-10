package ru.practicum.shareit.item;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;


@Data
@NoArgsConstructor
@Entity(name = "items")
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @OneToOne(fetch = FetchType.LAZY)
    private ItemRequest request;
}
