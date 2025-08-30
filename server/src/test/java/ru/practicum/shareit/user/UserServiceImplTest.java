package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(properties = {"spring.datasource.driverClassName=org.h2.Driver",
                              "spring.datasource.url=jdbc:h2:mem:shareit"},
                webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImplTest {

    private final EntityManager em;
    private final UserService userService;
    private final UserRepository userRepository;


    @Test
    void testSaveUser() {

        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setName("John Dow");
        newUserDto.setEmail("john_dow@gmail.com");

        userService.addUser(newUserDto);

        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM users u WHERE u.email = :email", User.class);
        User user = query.setParameter("email", newUserDto.getEmail()).getSingleResult();

        assertThat(user, notNullValue());
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(newUserDto.getName()));
        assertThat(user.getEmail(), equalTo(newUserDto.getEmail()));
    }

    @Test
    void testGetUser() {

        User user = userRepository.save(new User(null, "user1", "user1@email.com"));
        UserDto dto = userService.getUser(user.getId());
        assertThat(dto, notNullValue());
        assertThat(dto.getId(), equalTo(user.getId()));
        assertThat(dto.getName(), equalTo(user.getName()));
        assertThat(dto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void testUpdateUser() {

        User user = userRepository.save(new User(null, "user1", "user1@email.com"));
        NewUserDto newUserDto = new NewUserDto("user11", "user11@email.com");
        UserDto dto = userService.updateUser(user.getId(), newUserDto);
        assertThat(dto, notNullValue());
        assertThat(dto.getId(), equalTo(user.getId()));
        assertThat(dto.getName(), equalTo(newUserDto.getName()));
        assertThat(dto.getEmail(), equalTo(newUserDto.getEmail()));
    }

    @Test
    void testGetUsers() {

        User user1 = userRepository.save(new User(null, "user1", "user1@email.com"));
        User user2 = userRepository.save(new User(null, "user2", "user2@email.com"));
        List<UserDto> dtos = userService.getUsers().stream()
                .sorted(Comparator.comparing(UserDto::getName))
                .toList();
        assertThat(dtos, notNullValue());
        assertThat(dtos.size(), equalTo(2));
        assertThat(dtos.getFirst().getId(), equalTo(user1.getId()));
        assertThat(dtos.getFirst().getName(), equalTo(user1.getName()));
        assertThat(dtos.getFirst().getEmail(), equalTo(user1.getEmail()));
        assertThat(dtos.getLast().getId(), equalTo(user2.getId()));
        assertThat(dtos.getLast().getName(), equalTo(user2.getName()));
        assertThat(dtos.getLast().getEmail(), equalTo(user2.getEmail()));
    }

    @Test
    void testDeleteUser() {

        long userId = userRepository.save(new User(null, "user1", "user1@email.com")).getId();
        userService.deleteUser(userId);
        TypedQuery<User> query = em.createQuery("SELECT u FROM users u WHERE u.id = :id", User.class);
        try {
            User user = query.setParameter("id", userId).getSingleResult();
            assertThat(user, nullValue());
        } catch (NoResultException e) {
            assertThat(e, instanceOf(NoResultException.class));
        }
    }
}
