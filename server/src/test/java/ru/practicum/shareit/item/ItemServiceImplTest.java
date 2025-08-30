package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.comments.Comment;
import ru.practicum.shareit.comments.CommentRepository;
import ru.practicum.shareit.comments.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SuppressWarnings("SequencedCollectionMethodCanBeUsed")
@Transactional
@SpringBootTest(properties = {"spring.datasource.driverClassName=org.h2.Driver",
                              "spring.datasource.url=jdbc:h2:mem:shareit"},
                webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Test
    void testGetItem() {

        User user1 = userRepository.save(new User(null, "Rich user", "ricci@mail.com"));
        User user2 = userRepository.save(new User(null, "Poor user", "poory@mail.com"));
        User user3 = userRepository.save(new User(null, "Side user", "user@mail.com"));
        Item item = itemRepository.save(new Item(null, "Big Thing", "The Big thing item", true, user1, null));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastMoment = now.minusDays(10);
        bookingRepository.save(new Booking(
                null, pastMoment, pastMoment.plusDays(2),
                item, user2, BookingStatus.APPROVED));
        Booking bookingByUser3 = bookingRepository.save(new Booking(
                null, pastMoment.plusDays(3), pastMoment.plusDays(5),
                item, user3, BookingStatus.APPROVED));
        Comment commentByUser2 = commentRepository.save(new Comment(
                null, "Great big thing", item, user2, now.minusHours(2)));
        Comment commentByUser3 = commentRepository.save(new Comment(
                null, "Not as big thing as I expected", item, user3, now.minusHours(1)));

        ItemWithCommentsDto itemWithComments = itemService.getItem(item.getId(), user1.getId());

        assertThat(itemWithComments, notNullValue());
        assertThat(itemWithComments.getId(), equalTo(item.getId()));
        assertThat(itemWithComments.getName(), equalTo(item.getName()));
        assertThat(itemWithComments.getDescription(), equalTo(item.getDescription()));
        assertThat(itemWithComments.getAvailable(), equalTo(true));
        assertThat(itemWithComments.getLastBooking().truncatedTo(ChronoUnit.MILLIS),
                equalTo(bookingByUser3.getStart().truncatedTo(ChronoUnit.MILLIS)));
        assertThat(itemWithComments.getNextBooking(), nullValue());
        assertThat(itemWithComments.getComments().size(), equalTo(2));

        assertThat(itemWithComments.getComments().getFirst().getId(), equalTo(commentByUser2.getId()));
        assertThat(itemWithComments.getComments().getLast().getId(), equalTo(commentByUser3.getId()));
    }

    @Test
    void testAddItem() {

        User user = new User(null, "User1", "user1@mail.com");
        user = userRepository.save(user);

        NewItemDto newItemDto = new NewItemDto("Item1", "This is Item1", true, user.getId(), null);
        itemService.addItem(newItemDto);

        TypedQuery<Item> query = em.createQuery(
                "SELECT i FROM items i WHERE i.name = :itemName", Item.class);
        Item item = query.setParameter("itemName", newItemDto.getName()).getSingleResult();

        assertThat(item, notNullValue());
        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(newItemDto.getName()));
        assertThat(item.getDescription(), equalTo(newItemDto.getDescription()));
        assertThat(item.getOwner().getId(), equalTo(user.getId()));
    }

    @Test
    void testUpdateItem() {

        User user = userRepository.save(new User(null, "User1", "user1@mail.com"));
        Item item = itemRepository.save(new Item(null, "Item1", "This is Item1", false, user, null));

        UpdateItemDto updateItemDto = new UpdateItemDto(item.getId(), "Updated name", "Updated description", true, user.getId());
        itemService.updateItem(updateItemDto);

        TypedQuery<Item> query = em.createQuery(
                "SELECT i FROM items i WHERE i.id = :itemId", Item.class);
        item = query.setParameter("itemId", updateItemDto.getId()).getSingleResult();

        assertThat(item, notNullValue());
        assertThat(item.getId(), notNullValue());
        assertThat(item.getId(), equalTo(updateItemDto.getId()));
        assertThat(item.getName(), equalTo(updateItemDto.getName()));
        assertThat(item.getDescription(), equalTo(updateItemDto.getDescription()));
        assertThat(item.isAvailable(), equalTo(updateItemDto.getAvailable()));
        assertThat(item.getOwner().getId(), equalTo(user.getId()));
    }

    @Test
    void testGetItemsOfUser() {

        User user1 = userRepository.save(new User(null, "Rich user", "ricci@mail.com"));
        User user2 = userRepository.save(new User(null, "Poor user", "poory@mail.com"));
        Item item1 = itemRepository.save(new Item(null, "Big Thing", "The Big thing item", true, user1, null));
        Item item2 = itemRepository.save(new Item(null, "Small Thing", "The Small thing item", true, user1, null));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastMoment = now.minusDays(10);
        LocalDateTime futureMoment = now.plusDays(10);
        Booking pastBooking1 = bookingRepository.save(new Booking(
                null, pastMoment, pastMoment.plusDays(2),
                item1, user2, BookingStatus.APPROVED));
        Booking pastBooking2 = bookingRepository.save(new Booking(
                null, pastMoment, pastMoment.plusDays(2),
                item2, user2, BookingStatus.APPROVED));
        Booking futureBooking1 = bookingRepository.save(new Booking(
                null, futureMoment, futureMoment.plusDays(2),
                item1, user2, BookingStatus.APPROVED));
        Booking futureBooking2 = bookingRepository.save(new Booking(
                null, futureMoment, futureMoment.plusDays(2),
                item2, user2, BookingStatus.APPROVED));

        List<ItemWithCommentsDto> items = itemService.getItemsOfUser(user1.getId())
                .stream()
                .sorted(Comparator.comparing(ItemWithCommentsDto::getId))
                .toList();

        assertThat(items, notNullValue());
        assertThat(items.size(), equalTo(2));

        var it = items.getFirst();
        assertThat(it.getId(), equalTo(item1.getId()));
        assertThat(it.getName(), equalTo(item1.getName()));
        assertThat(it.getDescription(), equalTo(item1.getDescription()));
        assertThat(it.getAvailable(), equalTo(true));
        assertThat(it.getLastBooking().truncatedTo(ChronoUnit.MILLIS),
                equalTo(pastBooking1.getStart().truncatedTo(ChronoUnit.MILLIS)));
        assertThat(it.getNextBooking().truncatedTo(ChronoUnit.MILLIS),
                equalTo(futureBooking1.getStart().truncatedTo(ChronoUnit.MILLIS)));

        it = items.getLast();
        assertThat(it.getId(), equalTo(item2.getId()));
        assertThat(it.getName(), equalTo(item2.getName()));
        assertThat(it.getDescription(), equalTo(item2.getDescription()));
        assertThat(it.getAvailable(), equalTo(true));
        assertThat(it.getLastBooking().truncatedTo(ChronoUnit.MILLIS),
                equalTo(pastBooking2.getStart().truncatedTo(ChronoUnit.MILLIS)));
        assertThat(it.getNextBooking().truncatedTo(ChronoUnit.MILLIS),
                equalTo(futureBooking2.getStart().truncatedTo(ChronoUnit.MILLIS)));
    }

    @Test
    void testSearchItems() {

        User user = userRepository.save(new User(null, "User1", "user1@mail.com"));
        List<Item> items = new ArrayList<>();
        items.add(itemRepository.save(new Item(null, "Raakuta", "Dinner table", true, user, null)));
        items.add(itemRepository.save(new Item(null, "Syterale", "Syterale as is", true, user, null)));
        items.add(itemRepository.save(new Item(null, "Polygome", "Solid RAA desk", true, user, null)));

        List<ItemDto> dtos = itemService.searchItems(user.getId(), "raa")
                        .stream()
                        .sorted(Comparator.comparing(ItemDto::getId))
                        .toList();
        assertThat(dtos.size(), equalTo(2));
        assertThat(dtos.get(0).getId(), equalTo(items.get(0).getId()));
        assertThat(dtos.get(0).getName(), equalTo(items.get(0).getName()));
        assertThat(dtos.get(0).getDescription(), equalTo(items.get(0).getDescription()));
        assertThat(dtos.get(1).getId(), equalTo(items.get(2).getId()));
        assertThat(dtos.get(1).getName(), equalTo(items.get(2).getName()));
        assertThat(dtos.get(1).getDescription(), equalTo(items.get(2).getDescription()));
    }

    @Test
    void testAddComment() {

        User user1 = userRepository.save(new User(null, "Rich user", "ricci@mail.com"));
        User user2 = userRepository.save(new User(null, "Poor user", "poory@mail.com"));
        Item item1 = itemRepository.save(new Item(null, "Big Thing", "The Big thing item", true, user1, null));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastMoment = now.minusDays(10);
        bookingRepository.save(new Booking(
                null, pastMoment, pastMoment.plusDays(2),
                item1, user2, BookingStatus.APPROVED));

        var commentDto = new NewCommentDto(user2.getId(), item1.getId(), "Really Big thing");
        itemService.addComment(commentDto);

        TypedQuery<Comment> query = em.createQuery(
                "SELECT c FROM comments c WHERE c.author.id = :userId", Comment.class);
        Comment comment = query.setParameter("userId", user2.getId()).getSingleResult();

        assertThat(comment, notNullValue());
        assertThat(comment.getId(), notNullValue());
        assertThat(comment.getText(), equalTo(commentDto.getText()));
        assertThat(comment.getItem().getId(), equalTo(item1.getId()));
        assertThat(comment.getAuthor().getId(), equalTo(user2.getId()));
    }
}
