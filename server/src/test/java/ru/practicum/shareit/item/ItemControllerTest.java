package ru.practicum.shareit.item;

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
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController controller;
    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
    private MockMvc mvc;
    private ItemDto itemDto, itemDto2;
    private ItemWithCommentsDto itemWithCommentsDto;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        itemDto = new ItemDto(1L, "Big thing", "Real big thing", true, null);
        itemDto2 = new ItemDto(2L, "Small thing", "Tiny small thing", true, null);
        itemWithCommentsDto = new ItemWithCommentsDto(1L, "Big thing", "Real big thing", true,
                null, null, null,
                List.of(new CommentDto(1L, "aaa", "User1", LocalDateTime.now())));
    }

    @Test
    void testAddItem() throws Exception {

        Mockito.when(itemService.addItem(Mockito.any()))
                .thenReturn(itemDto);
        mvc.perform(post("/items")
                        .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(new NewItemDto(
                                itemDto.getName(),
                                itemDto.getDescription(),
                                itemDto.getAvailable(),
                                1L,
                                null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto2.getAvailable())))
                .andExpect(jsonPath("$.request", nullValue()));
    }

    @Test
    void testUpdateItem() throws Exception {

        Mockito.when(itemService.updateItem(Mockito.any()))
                .thenReturn(itemDto);
        mvc.perform(patch("/items/{id}", 1L)
                        .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(new UpdateItemDto(
                                1L,
                                itemDto.getName(),
                                itemDto.getDescription(),
                                itemDto.getAvailable(),
                                1L)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto2.getAvailable())))
                .andExpect(jsonPath("$.request", nullValue()));
    }

    @Test
    void testGetItem() throws Exception {

        Mockito.when(itemService.getItem(Mockito.anyLong(), Mockito.anyLong()))
               .thenReturn(itemWithCommentsDto);

        mvc.perform(get("/items/{id}", 1L)
                    .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithCommentsDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithCommentsDto.getName())))
                .andExpect(jsonPath("$.description", is(itemWithCommentsDto.getDescription())))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.request", nullValue()))
                .andExpect(jsonPath("$.comments.[0].id", is(itemWithCommentsDto.getComments().getFirst().getId()), Long.class))
                .andExpect(jsonPath("$.comments.[0].text", is(itemWithCommentsDto.getComments().getFirst().getText())))
                .andExpect(jsonPath("$.comments.[0].authorName", is(itemWithCommentsDto.getComments().getFirst().getAuthorName())));
    }

    @Test
    void testGetItemsOfUser() throws Exception {
        Mockito.when(itemService.getItemsOfUser(Mockito.anyLong()))
                .thenReturn(List.of(itemWithCommentsDto));

        mvc.perform(get("/items")
                        .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemWithCommentsDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemWithCommentsDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemWithCommentsDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(true)))
                .andExpect(jsonPath("$.[0].request", nullValue()))
                .andExpect(jsonPath("$.[0].comments.[0].id", is(itemWithCommentsDto.getComments().getFirst().getId()), Long.class))
                .andExpect(jsonPath("$.[0].comments.[0].text", is(itemWithCommentsDto.getComments().getFirst().getText())))
                .andExpect(jsonPath("$.[0].comments.[0].authorName", is(itemWithCommentsDto.getComments().getFirst().getAuthorName())));
    }

    @Test
    void testSearchItems() throws Exception {
        Mockito.when(itemService.searchItems(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search?text={text}", "abc")
                        .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemWithCommentsDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemWithCommentsDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemWithCommentsDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(true)))
                .andExpect(jsonPath("$.[0].request", nullValue()));
    }

    @Test
    void testAddComment() throws Exception {

        var commentDto = new CommentDto(1L, "abc", "User1", LocalDateTime.now());
        Mockito.when(itemService.addComment(Mockito.any()))
                .thenReturn(commentDto);
        mvc.perform(post("/items/{id}/comment", 1L)
                        .header(ShareItServer.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(new NewCommentDto(1L, 2L, "abc")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }
}
