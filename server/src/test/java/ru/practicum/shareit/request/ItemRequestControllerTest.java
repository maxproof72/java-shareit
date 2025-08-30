package ru.practicum.shareit.request;

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
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestService requestService;
    @InjectMocks
    private ItemRequestController controller;
    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
    private LocalDateTime now;
    private MockMvc mvc;
    private ItemRequestDto dto;
    private ItemRequestWithAnswersDto dtoWithAnswers;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        now = LocalDateTime.now();
        dto = new ItemRequestDto(1L, "Wanna big smth", now);
        dtoWithAnswers = new ItemRequestWithAnswersDto(1L, "Wanna big smth", now);
        dtoWithAnswers.getItems().add(new ItemRequestWithAnswersDto.ItemAnswer(3L, "Item1", 17L));
        dtoWithAnswers.getItems().add(new ItemRequestWithAnswersDto.ItemAnswer(7L, "Item99", 197L));
    }

    @Test
    void testCreateItemRequest() throws Exception {

        Mockito
                .when(requestService.createItemRequest(Mockito.any(NewItemRequestDto.class)))
                .thenReturn(dto);
        mvc.perform(post("/requests")
                        .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(new NewItemRequestDto(
                                dto.getDescription(), 1L, now)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(dto.getDescription())));
    }

    @Test
    void testGetAllUserItemRequests() throws Exception {

        Mockito.when(requestService.getUserItemRequests(Mockito.anyLong()))
                .thenReturn(List.of(dtoWithAnswers));
        mvc.perform(get("/requests")
                        .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(dtoWithAnswers.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(dtoWithAnswers.getDescription())))
                .andExpect(jsonPath("$.[0].items.[0].id", is(dtoWithAnswers.getItems().getFirst().getId()), Long.class))
                .andExpect(jsonPath("$.[0].items.[1].id", is(dtoWithAnswers.getItems().getLast().getId()), Long.class));
    }

    @Test
    void testGetOtherUsersItemRequests() throws Exception {

        Mockito.when(requestService.getOtherUsersItemRequests(Mockito.anyLong()))
               .thenReturn(List.of(dto));

        mvc.perform(get("/requests/all")
                    .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(dto.getDescription())));
    }

    @Test
    void testGetItemRequest() throws Exception {

        Mockito.when(requestService.getItemRequestById(Mockito.anyLong()))
                .thenReturn(dtoWithAnswers);

        mvc.perform(get("/requests/1")
                        .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dtoWithAnswers.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(dtoWithAnswers.getDescription())))
                .andExpect(jsonPath("$.items.[0].id", is(dtoWithAnswers.getItems().getFirst().getId()), Long.class))
                .andExpect(jsonPath("$.items.[1].id", is(dtoWithAnswers.getItems().getLast().getId()), Long.class));
    }
}
