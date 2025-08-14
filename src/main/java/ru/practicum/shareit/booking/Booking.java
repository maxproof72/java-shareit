package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Entity(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "booking_start")
    LocalDateTime start;

    @Column(name = "booking_end")
    LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    User booker;

    @Enumerated(EnumType.STRING)
    BookingStatus status;
}
