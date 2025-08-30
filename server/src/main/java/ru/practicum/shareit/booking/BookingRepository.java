package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDate;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    /**
     * Возвращает все бронирования указанного пользователя
     * @param bookerId Id пользователя
     * @return Список бронирований
     */
    List<Booking> findAllByBookerIdOrderByStart(Long bookerId);

    /**
     * Возвращает все текущие бронирования указанного пользователя
     * @param bookerId Id пользователя
     * @param date Текущая дата
     * @return Список бронирований
     */
    @Query("select b from bookings as b " +
            "where b.booker.id = :bookerId and " +
            "b.start <= :date and " +
            "b.end >= :date " +
            "order by b.start asc")
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStart(Long bookerId, LocalDateTime date);

    /**
     * Возвращает все запланированные бронирования указанного пользователя
     * @param bookerId Id пользователя
     * @param date Текущая дата
     * @return Список бронирований
     */
    List<Booking> findAllByBookerIdAndStartAfterOrderByStart(Long bookerId, LocalDateTime date);

    /**
     * Возвращает все завершенные бронирования указанного пользователя
     * @param bookerId Id пользователя
     * @param date Текущая дата
     * @return Список бронирований
     */
    List<Booking> findAllByBookerIdAndEndBeforeOrderByStart(Long bookerId, LocalDateTime date);

    /**
     * Возвращает все бронирования указанного пользователя с заданным статусом
     * @param bookerId Id пользователя
     * @param bookingStatus Статус
     * @return Список бронирований
     */
    List<Booking> findAllByBookerIdAndStatusOrderByStart(Long bookerId, BookingStatus bookingStatus);

    /**
     * Возвращает все бронирования предметов указанного пользователя
     * @param itemOwnerId Id пользователя
     * @return Список бронирований
     */
    List<Booking> findAllByItemOwnerIdOrderByStart(Long itemOwnerId);

    /**
     * Возвращает все текущие бронирования предметов указанного пользователя
     * @param itemOwnerId Id пользователя
     * @param date Текущая дата
     * @return Список бронирований
     */
    @Query("select b from bookings as b " +
            "where b.item.owner.id = :itemOwnerId and " +
            "b.start <= :date and " +
            "b.end >= :date " +
            "order by b.start asc")
    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStart(Long itemOwnerId, LocalDateTime date);

    /**
     * Возвращает все запланированные бронирования предметов указанного пользователя
     * @param itemOwnerId Id пользователя
     * @param date Текущая дата
     * @return Список бронирований
     */
    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStart(Long itemOwnerId, LocalDateTime date);

    /**
     * Возвращает все завершенные бронирования предметов указанного пользователя
     * @param itemOwnerId Id пользователя
     * @param date Текущая дата
     * @return Список бронирований
     */
    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStart(Long itemOwnerId, LocalDateTime date);

    /**
     * Возвращает все бронирования указанного пользователя с заданным статусом
     * @param itemOwnerId Id пользователя
     * @param bookingStatus Статус
     * @return Список бронирований
     */
    List<Booking> findAllByItemOwnerIdAndStatus(Long itemOwnerId, BookingStatus bookingStatus);

    /**
     * Возвращает последнее бронирование указанного предмета
     * @param item Предмет
     * @param date Текущая дата
     * @return Опцион бронирования
     */
    @Query("select max(b.start) from bookings as b where b.item = :item and b.start <= :date")
    Optional<LocalDateTime> findLastBookingDate(Item item, LocalDateTime date);

    /**
     * Возвращает перечень сочетаний <code>booking.item.id</code> и <code>booking.start></code>,
     * где значение сочетания представляет собой дату последнего бронирования,
     * для заданного набора идентификаторов предметов
     * @param list Набор идентификаторов предметов
     * @param date Текущая дата
     * @return Перечень сочетаний
     */
    @Query("""
            select b.item.id as item, max(b.start) as date
            from bookings as b
            where b.item in :list and b.start <= :date
            group by b.item
            """)
    List<BookingDate> findLastBookingDateForItems(Collection<Item> list, LocalDateTime date);

    /**
     * Возвращает ближайшее бронирование указанного предмета
     * @param item Предмет
     * @param date Текущая дата
     * @return Опцион бронирования
     */
    @Query("select min(b.start) from bookings as b where b.item = :item and b.start > :date")
    Optional<LocalDateTime> findNextBookingDate(Item item, LocalDateTime date);

    /**
     * Возвращает перечень сочетаний <code>booking.item.id</code> и <code>booking.start></code>,
     * где значение сочетания представляет собой дату ближайшего бронирования,
     * для заданного набора идентификаторов предметов
     * @param list Набор идентификаторов предметов
     * @param date Текущая дата
     * @return Перечень сочетаний
     */
    @Query("""
            select b.item.id as item, min(b.start) as date
            from bookings as b
            where b.item in :list and b.start > :date
            group by b.item
            """)
    List<BookingDate> findNextBookingDateForItems(Collection<Item> list, LocalDateTime date);

    /**
     * Проверяет, существует ли бронирование указанного предмета,
     * осуществленное заданным пользователем, с заданным статусом
     * и завершенное на указанную дату
     * @param itemId Id предмета
     * @param bookerId Id пользователя
     * @param status Требуемый статус бронирования
     * @param endBefore Дата завершения
     * @return true, если такое бронирование найдено
     */
    boolean existsBookingByItemIdAndBookerIdAndStatusIsAndEndBefore(long itemId, Long bookerId, BookingStatus status, LocalDateTime endBefore);
}
