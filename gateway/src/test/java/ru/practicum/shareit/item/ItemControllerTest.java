package ru.practicum.shareit.item;

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
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.item.dto.NewCommentData;
import ru.practicum.shareit.item.dto.NewItemData;
import ru.practicum.shareit.item.dto.UpdateItemData;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController controller;
    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private void testAddItemFor(NewItemData newItemData) throws Exception {

        mvc.perform(post("/items")
                        .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(newItemData))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testAddItem() throws Exception {

        // Пустое название
        testAddItemFor(new NewItemData("", "abc", true, null));

        // Длинное название
        testAddItemFor(new NewItemData("a".repeat(65), "abc", true, null));

        // Пустое описание
        testAddItemFor(new NewItemData("aaa", "", true, null));

        // Длинное описание
        testAddItemFor(new NewItemData("aaa", "x".repeat(513), true, null));

        // Пустое поле available
        testAddItemFor(new NewItemData("aaa", "abc", null, null));
    }

    private void testUpdateItemFor(UpdateItemData updateItemData) throws Exception {

        mvc.perform(patch("/items/{id}", 1L)
                        .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(updateItemData))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testUpdateItem() throws Exception {

        // Пустое название
        testUpdateItemFor(new UpdateItemData("", "abc", true));

        // Длинное название
        testUpdateItemFor(new UpdateItemData("a".repeat(65), "abc", true));

        // Пустое описание
        testUpdateItemFor(new UpdateItemData("aaa", "", true));

        // Длинное описание
        testUpdateItemFor(new UpdateItemData("aaa", "x".repeat(513), true));

        // Нет хедера
        mvc.perform(patch("/items/{id}", 1L)
                        .content(mapper.writeValueAsString(
                                new UpdateItemData("abc", "def", true)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Кривой id предмета
        mvc.perform(patch("/items/{id}", "XY")
                        .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(
                                new UpdateItemData("abc", "def", true)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetItem() throws Exception {

        // Нет хедера
        mvc.perform(get("/items/{id}", 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Некорректный id предмета
        mvc.perform(get("/items/{id}", "XY")
                    .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetItemsOfUser() throws Exception {

        // Нет хедера
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testSearchItems() throws Exception {

        // Нет хедера
        mvc.perform(get("/items/search?text={text}", "abc")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Нет параметра поиска
        mvc.perform(get("/items/search")
                        .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testAddComment() throws Exception {

        // Нет хедера
        mvc.perform(post("/items/{id}/comment", 1L)
                        .content(mapper.writeValueAsString(new NewCommentData("abc")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Некорректный id
        mvc.perform(post("/items/{id}/comment", "XY")
                        .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(new NewCommentData("abc")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Нулевой комментарий
        mvc.perform(post("/items/{id}/comment", 1L)
                        .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(new NewCommentData(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Пустой комментарий
        mvc.perform(post("/items/{id}/comment", 1L)
                        .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(new NewCommentData("")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Длинный комментарий
        mvc.perform(post("/items/{id}/comment", 1L)
                        .header(ShareItGateway.SHARER_USER_ID_HEADER,1L)
                        .content(mapper.writeValueAsString(new NewCommentData("c".repeat(513))))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
