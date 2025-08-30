package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDtoJsonTest {

    private final JacksonTester<UserDto> jsonDto;
    private final JacksonTester<NewUserDto> jsonNewDto;

    @Test
    void testUserDtoJson() throws Exception {

        var dto = new UserDto(
                1L,
                "User1",
                "user1@mail.com"
        );

        var jsonContent = jsonDto.write(dto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("User1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("user1@mail.com");
    }

    @Test
    void testNewUserDtoJson() throws Exception {

        var dto = new NewUserDto("User2", "user2@mail.com");

        var jsonContent = jsonNewDto.write(dto);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("User2");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("user2@mail.com");
    }
}
