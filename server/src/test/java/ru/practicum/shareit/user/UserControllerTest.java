package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController controller;
    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
    private MockMvc mvc;
    private UserDto userDto, userDto2;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        userDto = new UserDto(1L, "John Smith", "john.smith@mail.com");
        userDto2 = new UserDto(2L, "Jane Smith", "jane.smith@mail.com");
    }

    @Test
    void testGetAllUsers() throws Exception {

        Mockito
                .when(userService.getUsers())
                .thenReturn(List.of(userDto, userDto2));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.[1].id", is(userDto2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(userDto2.getName())))
                .andExpect(jsonPath("$.[1].email", is(userDto2.getEmail())));
    }

    @Test
    void testGetUser() throws Exception {

        Mockito
                .when(userService.getUser(Mockito.anyLong()))
                .thenReturn(userDto);

        mvc.perform(get("/users/{id}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void testAddUser() throws Exception {

        Mockito
                .when(userService.addUser(Mockito.any()))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                    .content(mapper.writeValueAsString(new NewUserDto(userDto.getName(), userDto.getEmail())))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void testUpdateUser() throws Exception {

        Mockito
                .when(userService.updateUser(Mockito.anyLong(), Mockito.any()))
                .thenReturn(userDto);

        mvc.perform(patch("/users/{id}", 1L)
                        .content(mapper.writeValueAsString(new NewUserDto(userDto.getName(), userDto.getEmail())))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void testDeleteUser() throws Exception {

        mvc.perform(delete("/users/{id}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
