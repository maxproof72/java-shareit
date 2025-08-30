package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewRequestData;

import java.time.LocalDateTime;

import static ru.practicum.shareit.ShareItGateway.SHARER_USER_ID_HEADER;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                                    @RequestBody @Valid NewRequestData itemRequest) {

        log.info("POST request to create a item request (userId={}, item={})", userId, itemRequest);
        itemRequest.setCreated(LocalDateTime.now());
        return requestClient.createItemRequest(userId, itemRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItemRequests(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId) {

        log.info("GET request for all user requests (userId={})", userId);
        return requestClient.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsersItemRequests(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId) {
        log.info("GET request for all other users requests (userId={})", userId);
        return requestClient.getOtherUsersItemRequests(userId);

    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@NotNull @RequestHeader(name = SHARER_USER_ID_HEADER) long userId,
                                                 @NotNull @PathVariable long requestId) {

        log.info("GET request for information of item request (requestId={})", requestId);
        return requestClient.getItemRequestById(userId, requestId);
    }
}
