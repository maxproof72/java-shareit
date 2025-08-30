package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.NewCommentData;
import ru.practicum.shareit.item.dto.NewItemData;
import ru.practicum.shareit.item.dto.UpdateItemData;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(long userId, NewItemData itemRequest) {
        return post("", userId, itemRequest);
    }

    public ResponseEntity<Object> updateItem(long userId, long itemId, UpdateItemData itemRequest) {
        return patch("/" + itemId, userId, itemRequest);
    }

    public ResponseEntity<Object> getItem(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsOfUser(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItems(long userId, String searchPattern) {
        return get("/search?text={text}", userId,
                   Map.of("text", searchPattern));
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, NewCommentData commentRequest) {
        return post("/" + itemId + "/comment", userId, commentRequest);
    }
}
