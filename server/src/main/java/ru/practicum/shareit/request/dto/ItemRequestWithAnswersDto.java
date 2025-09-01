package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.utils.CustomDateTimeDeserializer;
import ru.practicum.shareit.utils.CustomDateTimeSerializer;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestWithAnswersDto {

    @Data
    @AllArgsConstructor
    public static class ItemAnswer {
        long id;
        String name;
        long ownerId;

        public static ItemAnswer fromItem(Item item) {
            return new ItemAnswer(item.getId(), item.getName(), item.getOwner().getId());
        }
    }

    private long id;
    private String description;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime created;

    private final List<ItemAnswer> items = new ArrayList<>();
}
