package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity(name = "requests")
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User requestor;

    private LocalDateTime created;
}
