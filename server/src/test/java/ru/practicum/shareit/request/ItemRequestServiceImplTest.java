package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(properties = {"spring.datasource.driverClassName=org.h2.Driver",
                              "spring.datasource.url=jdbc:h2:mem:shareit"},
                webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemRequestServiceImplTest {

    private final EntityManager em;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    @Autowired
    private ItemRequestService itemRequestService;


    private User requester, provider;
    private ItemRequest request;
    private Item item;

    private void makeUsersItemsAndRequest() {

        requester = userRepository.save(new User(null, "Requester", "req@mail.com"));
        request = itemRequestRepository.save(new ItemRequest(null, "I wanna get a Big item", requester, LocalDateTime.now().minusHours(2)));
        provider = userRepository.save(new User(null, "Provider", "pro@mail.com"));
        item = itemRepository.save(new Item(null, "item", "item desc", true, provider, request));
    }

    @BeforeEach
    public void setup() {
        requester = null;
        provider = null;
        request = null;
        item = null;
    }

    @Test
    void testCreateItemRequest() {

        User user = userRepository.save(new User(
                null, "User1", "user1@mail.com"));

        var now = LocalDateTime.now();
        var dto = new NewItemRequestDto("I wanna get a Big item", user.getId(), now);
        itemRequestService.createItemRequest(dto);

        TypedQuery<ItemRequest> query = em.createQuery(
                "SELECT r FROM requests r WHERE r.requestor.id = :requestorId", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("requestorId", user.getId()).getSingleResult();

        assertThat(itemRequest, notNullValue());
        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getDescription(), equalTo(dto.getDescription()));
        assertThat(itemRequest.getCreated(), equalTo(now));
        assertThat(itemRequest.getRequestor().getId(), equalTo(user.getId()));
        assertThat(itemRequest.getRequestor().getName(), equalTo(user.getName()));
        assertThat(itemRequest.getRequestor().getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void testGetUserItemRequests() {

        makeUsersItemsAndRequest();

        List<ItemRequestWithAnswersDto> dtos = itemRequestService.getUserItemRequests(requester.getId());
        assertThat(dtos.size(), equalTo(1));
        ItemRequestWithAnswersDto dto = dtos.getFirst();
        assertThat(dto.getId(), equalTo(request.getId()));
        assertThat(dto.getDescription(), equalTo(request.getDescription()));
        assertThat(dto.getCreated(), equalTo(request.getCreated()));
        assertThat(dto.getItems().size(), equalTo(1));
        assertThat(dto.getItems().getFirst().getId(), equalTo(item.getId()));
        assertThat(dto.getItems().getFirst().getName(), equalTo(item.getName()));
        assertThat(dto.getItems().getFirst().getOwnerId(), equalTo(provider.getId()));
    }

    @Test
    void testGetOtherUsersItemRequests() {

        makeUsersItemsAndRequest();

        List<ItemRequestDto> dtos = itemRequestService.getOtherUsersItemRequests(provider.getId());
        assertThat(dtos.size(), equalTo(1));
        ItemRequestDto dto = dtos.getFirst();
        assertThat(dto.getId(), equalTo(request.getId()));
        assertThat(dto.getDescription(), equalTo(request.getDescription()));
        assertThat(dto.getCreated(), equalTo(request.getCreated()));
    }

    @Test
    void testGetItemRequestById() {

        makeUsersItemsAndRequest();

        ItemRequestWithAnswersDto dto = itemRequestService.getItemRequestById(request.getId());
        assertThat(dto.getId(), equalTo(request.getId()));
        assertThat(dto.getDescription(), equalTo(request.getDescription()));
        assertThat(dto.getCreated(), equalTo(request.getCreated()));
        assertThat(dto.getItems().size(), equalTo(1));
        assertThat(dto.getItems().getFirst().getId(), equalTo(item.getId()));
        assertThat(dto.getItems().getFirst().getName(), equalTo(item.getName()));
        assertThat(dto.getItems().getFirst().getOwnerId(), equalTo(provider.getId()));
    }
}
