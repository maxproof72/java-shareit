package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.practicum.shareit.ShareItServer.SHARER_USER_ID_HEADER;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                            @RequestBody NewItemRequestDto requestDto) {

        log.info("Запрос POST создания заявки (userId={}, item={})", userId, requestDto);
        requestDto.setRequestorId(userId);
        requestDto.setCreated(LocalDateTime.now());
        return requestService.createItemRequest(requestDto);
    }

    @GetMapping
    public Collection<ItemRequestWithAnswersDto> getAllUserItemRequests(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId) {

        log.info("Запрос GET перечня заявок от пользователя (userId={})", userId);
        return requestService.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getOtherUsersItemRequests(@RequestHeader(name = SHARER_USER_ID_HEADER) long userId) {

        log.info("Запрос GET перечня заявок от других пользователей (userId={})", userId);
        return requestService.getOtherUsersItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithAnswersDto getItemRequest(@PathVariable long requestId) {

        log.info("Запрос GET подробной информации о заявке (requestId={})", requestId);
        return requestService.getItemRequestById(requestId);
    }
}
