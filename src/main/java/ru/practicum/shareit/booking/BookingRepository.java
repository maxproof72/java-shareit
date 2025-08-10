package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findAllByBookerId(Long bookerId);

    @Query("select b from bookings as b " +
            "where b.booker = :bookerId and " +
            "b.start <= :date and " +
            "b.end >= :date " +
            "order by b.start asc")
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStart(Long bookerId, LocalDateTime date);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStart(Long bookerId, LocalDateTime date);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStart(Long bookerId, LocalDateTime date);

    List<Booking> findAllByBookerIdAndStatusOrderByStart(Long itemOwnerId, BookingStatus bookingStatus);

    List<Booking> findAllByItemOwnerIdOrderByStart(Long itemOwnerId);

    @Query("select b from bookings as b " +
            "where b.item.owner = :itemOwnerId and " +
            "b.start <= :date and " +
            "b.end >= :date " +
            "order by b.start asc")
    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStart(Long itemOwnerId, LocalDateTime date);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStart(Long itemOwnerId, LocalDateTime date);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStart(Long itemOwnerId, LocalDateTime date);

    List<Booking> findAllByItemOwnerIdAndStatus(Long itemOwnerId, BookingStatus bookingStatus);
}
