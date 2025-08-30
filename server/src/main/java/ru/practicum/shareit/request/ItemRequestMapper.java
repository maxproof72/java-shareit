package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

@UtilityClass
public class ItemRequestMapper {

    public ItemRequest toItemRequest(NewItemRequestDto requestDto) {

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(requestDto.getDescription());
        itemRequest.setCreated(requestDto.getCreated());
        return itemRequest;
    }

    public ItemRequestDto toDto(ItemRequest itemRequest) {

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public ItemRequestWithAnswersDto toDtoWithAnswers(ItemRequest itemRequest) {

        return new ItemRequestWithAnswersDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }
}
