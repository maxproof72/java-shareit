package ru.practicum.shareit.item;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "items")
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User owner;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private ItemRequest request;
}
