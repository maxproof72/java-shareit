package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingDto;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(properties = {"spring.datasource.driverClassName=org.h2.Driver",
                              "spring.datasource.url=jdbc:h2:mem:shareit"},
                webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingServiceImplTest {

    private final EntityManager em;
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;


    // region Test helpers

    private Item item;
    private User owner, booker;
    private Booking pastBooking, currentBooking, futureBooking;


    private void makeUsersAndItem() {

        owner = userRepository.save(new User(null, "User1", "user1@mail.com"));
        booker = userRepository.save(new User(null, "User2", "user2@mail.com"));
        item = itemRepository.save(new Item(
                null, "Item1", "This is Item1", true, owner, null));
    }

    private void makeBookings() {

        LocalDateTime now = LocalDateTime.now();
        // Одно бронирование в прошлом
        pastBooking = bookingRepository.save(new Booking(
                null, now.minusDays(10), now.minusDays(8),
                item, booker, BookingStatus.APPROVED));
        // Одно бронирование текущее
        currentBooking = bookingRepository.save(new Booking(
                null, now.minusDays(1), now.plusDays(1),
                item, booker, BookingStatus.APPROVED));
        // Одно бронирование в будущем
        futureBooking = bookingRepository.save(new Booking(
                null, now.plusDays(2), now.plusDays(4),
                item, booker, BookingStatus.WAITING));
    }

    private void assertThatBookingsAreEqual(BookingDto bookingDto, Booking booking) {
        assertThat(bookingDto.getId(), equalTo(booking.getId()));
        assertThat(bookingDto.getItem().getId(), equalTo(item.getId()));
        assertThat(bookingDto.getStart(), equalTo(booking.getStart()));
        assertThat(bookingDto.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingDto.getBooker().getId(), equalTo(booker.getId()));
        assertThat(bookingDto.getStatus(), equalTo(booking.getStatus()));
    }

    // endregion


    @BeforeEach
    public void setUp() {
        item = null;
        owner = null;
        booker = null;
        pastBooking = null;
        currentBooking = null;
        futureBooking = null;
    }

    @Test
    void testAddBooking() {

        makeUsersAndItem();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.plusDays(1);
        LocalDateTime endTime = now.plusDays(2);
        NewBookingDto newBookingDto = new NewBookingDto();
        newBookingDto.setItemId(item.getId());
        newBookingDto.setStart(startTime);
        newBookingDto.setEnd(endTime);

        bookingService.addBooking(booker.getId(), newBookingDto);

        TypedQuery<Booking> query = em.createQuery(
                "SELECT b FROM bookings b WHERE b.item.id = :itemId", Booking.class);
        Booking booking = query.setParameter("itemId", newBookingDto.getItemId()).getSingleResult();

        assertThat(booking, notNullValue());
        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getItem().getId(), equalTo(newBookingDto.getItemId()));
        assertThat(booking.getStart(), equalTo(newBookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(newBookingDto.getEnd()));
        assertThat(booking.getBooker().getId(), equalTo(booker.getId()));
        assertThat(booking.getStatus().name(), equalTo(BookingStatus.WAITING.name()));
    }

    @Test
    void testBookingApprove() {

        makeUsersAndItem();
        makeBookings();

        bookingService.updateBookingStatus(new UpdateBookingDto(
                owner.getId(), futureBooking.getId(), true));

        TypedQuery<Booking> query = em.createQuery(
                "SELECT b FROM bookings b WHERE b.id = :bookingId", Booking.class);
        Booking bookingQuery = query.setParameter("bookingId", futureBooking.getId()).getSingleResult();

        assertThat(bookingQuery, notNullValue());
        assertThat(bookingQuery.getId(), equalTo(futureBooking.getId()));
        assertThat(bookingQuery.getItem().getId(), equalTo(item.getId()));
        assertThat(bookingQuery.getStart(), equalTo(futureBooking.getStart()));
        assertThat(bookingQuery.getEnd(), equalTo(futureBooking.getEnd()));
        assertThat(bookingQuery.getBooker().getId(), equalTo(booker.getId()));
        assertThat(bookingQuery.getStatus(), equalTo(BookingStatus.APPROVED));

        Assertions.assertThrows(ForbiddenException.class, () ->
                bookingService.updateBookingStatus(new UpdateBookingDto(
                        booker.getId(), futureBooking.getId(), false
                )));
    }

    @Test
    void testGetBooking() {

        makeUsersAndItem();
        makeBookings();

        var dto = bookingService.getBooking(booker.getId(), currentBooking.getId());
        assertThat(dto, notNullValue());
        assertThatBookingsAreEqual(dto, currentBooking);
    }

    @Test
    void testGetBookerBookingsOfState() {

        makeUsersAndItem();
        makeBookings();

        // Проверка опции ALL
        var dtoList = bookingService.getBookerBookingsOfState(booker.getId(), BookingRequestState.ALL);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(3));
        assertThatBookingsAreEqual(dtoList.get(0), pastBooking);
        assertThatBookingsAreEqual(dtoList.get(1), currentBooking);
        assertThatBookingsAreEqual(dtoList.get(2), futureBooking);

        // Проверка опции CURRENT
        dtoList = bookingService.getBookerBookingsOfState(booker.getId(), BookingRequestState.CURRENT);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(1));
        assertThatBookingsAreEqual(dtoList.getFirst(), currentBooking);

        // Проверка опции FUTURE
        dtoList = bookingService.getBookerBookingsOfState(booker.getId(), BookingRequestState.FUTURE);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(1));
        assertThatBookingsAreEqual(dtoList.getFirst(), futureBooking);

        // Проверка опции PAST
        dtoList = bookingService.getBookerBookingsOfState(booker.getId(), BookingRequestState.PAST);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(1));
        assertThatBookingsAreEqual(dtoList.getFirst(), pastBooking);

        // Проверка опции REJECTING
        dtoList = bookingService.getBookerBookingsOfState(booker.getId(), BookingRequestState.REJECTED);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(0));

        // Проверка опции WAITING
        dtoList = bookingService.getBookerBookingsOfState(booker.getId(), BookingRequestState.WAITING);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(1));
        assertThatBookingsAreEqual(dtoList.getFirst(), futureBooking);
    }

    @Test
    void testGetOwnerBookingsOfState() {

        makeUsersAndItem();
        makeBookings();

        // Проверка опции ALL
        var dtoList = bookingService.getOwnerBookingsOfState(owner.getId(), BookingRequestState.ALL);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(3));
        assertThatBookingsAreEqual(dtoList.get(0), pastBooking);
        assertThatBookingsAreEqual(dtoList.get(1), currentBooking);
        assertThatBookingsAreEqual(dtoList.get(2), futureBooking);

        // Проверка опции CURRENT
        dtoList = bookingService.getOwnerBookingsOfState(owner.getId(), BookingRequestState.CURRENT);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(1));
        assertThatBookingsAreEqual(dtoList.getFirst(), currentBooking);

        // Проверка опции FUTURE
        dtoList = bookingService.getOwnerBookingsOfState(owner.getId(), BookingRequestState.FUTURE);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(1));
        assertThatBookingsAreEqual(dtoList.getFirst(), futureBooking);

        // Проверка опции PAST
        dtoList = bookingService.getOwnerBookingsOfState(owner.getId(), BookingRequestState.PAST);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(1));
        assertThatBookingsAreEqual(dtoList.getFirst(), pastBooking);

        // Проверка опции REJECTING
        dtoList = bookingService.getOwnerBookingsOfState(owner.getId(), BookingRequestState.REJECTED);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(0));

        // Проверка опции WAITING
        dtoList = bookingService.getOwnerBookingsOfState(owner.getId(), BookingRequestState.WAITING);
        assertThat(dtoList, notNullValue());
        assertThat(dtoList.size(), equalTo(1));
        assertThatBookingsAreEqual(dtoList.getFirst(), futureBooking);
    }
}
