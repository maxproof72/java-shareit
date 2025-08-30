package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.NewUserData;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController controller;
    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
    private MockMvc mvc;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetUser() throws Exception {

        // Некорректный путь
        mvc.perform(get("/users/{id}", "XY")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    private void testAddUserFor(NewUserData user) throws Exception {

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testAddUser() throws Exception {

        // Пустое или длинное имя
        testAddUserFor(new NewUserData(null, "xyz@email.com"));
        testAddUserFor(new NewUserData("", "xyz@email.com"));
        testAddUserFor(new NewUserData("x".repeat(33), "xyz@email.com"));

        // Пустая или некорректная почта
        testAddUserFor(new NewUserData("user", null));
        testAddUserFor(new NewUserData("user", ""));
        testAddUserFor(new NewUserData("user", "z".repeat(60) + "@email.com"));
        testAddUserFor(new NewUserData("user", "xyz.at.email.com"));
    }

    private void testUpdateUserFor(NewUserData user) throws Exception {

        mvc.perform(patch("/users/{id}", 1L)
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testUpdateUser() throws Exception {

        // Пустое или длинное имя
        testUpdateUserFor(new NewUserData("", "xyz@email.com"));
        testUpdateUserFor(new NewUserData("x".repeat(33), "xyz@email.com"));

        // Пустая или некорректная почта
        testUpdateUserFor(new NewUserData("user", "z".repeat(60) + "@email.com"));
        testUpdateUserFor(new NewUserData("user", "xyz.at.email.com"));

        // Некорректный путь
        mvc.perform(patch("/users/{id}", "XY")
                        .content(mapper.writeValueAsString(
                                new NewUserData("user", "user@mail.com")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeleteUser() throws Exception {

        // Некорректный путь
        mvc.perform(delete("/users/{id}", "XY")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
