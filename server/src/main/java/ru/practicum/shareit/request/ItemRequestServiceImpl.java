package ru.practicum.shareit.request;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public ItemRequestDto createItemRequest(NewItemRequestDto newItemRequestDto) {

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(newItemRequestDto);
        User user = userRepository.findById(newItemRequestDto.getRequestorId())
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь id=%d не найден".formatted(newItemRequestDto.getRequestorId())));
        itemRequest.setRequestor(user);
        itemRequest = itemRequestRepository.save(itemRequest);
        if (log.isDebugEnabled()) {
            String limitedDescription = itemRequest.getDescription();
            limitedDescription = limitedDescription.substring(0, Math.min(limitedDescription.length(), 20));
            log.debug("Добавлена заявка id={}, desc='{}', owner_id={}",
                    itemRequest.getId(), limitedDescription, itemRequest.getRequestor().getId());
        }
        return ItemRequestMapper.toDto(itemRequest);
    }

    @Override
    public List<ItemRequestWithAnswersDto> getUserItemRequests(long userId) {

        List<ItemRequestWithAnswersDto> requests =
                itemRequestRepository.findAllItemsRequestsByRequestorIdOrderByCreatedDesc(userId)
                        .stream()
                        .map(ItemRequestMapper::toDtoWithAnswers)
                        .toList();

        Map<Long, ItemRequestWithAnswersDto> dtoMap = requests.stream()
                .collect(Collectors.toUnmodifiableMap(
                        ItemRequestWithAnswersDto::getId,
                        Function.identity()));

        itemRepository.findItemsByRequestIdIn(dtoMap.keySet())
                .forEach(item -> dtoMap.get(item.getRequest().getId())
                    .getItems().add(ItemRequestWithAnswersDto.ItemAnswer.fromItem(item)));

        log.debug("Запрос заявок пользователя с id={} вернул {} записей", userId, requests.size());
        return requests;
    }

    @Override
    public List<ItemRequestDto> getOtherUsersItemRequests(long userId) {

        var requests = itemRequestRepository.findItemRequestsByRequestorIdNot(userId);
        log.debug("Запрос заявок от пользователей кроме id={} вернул {} записей", userId, requests.size());
        return requests.stream().map(ItemRequestMapper::toDto).toList();
    }

    @Override
    public ItemRequestWithAnswersDto getItemRequestById(long requestId) {

        ItemRequestWithAnswersDto requestWithAnswers = itemRequestRepository.findById(requestId)
                .map(ItemRequestMapper::toDtoWithAnswers)
                .orElseThrow(() -> new NotFoundException("Заявка с id=%d не найдена".formatted(requestId)));
        requestWithAnswers.getItems().addAll(itemRepository.findItemsByRequestId(requestId)
                .stream()
                .map(ItemRequestWithAnswersDto.ItemAnswer::fromItem)
                .toList());
        log.debug("Запрос информации по заявке с id={} вернул {}", requestId, requestWithAnswers);
        return requestWithAnswers;
    }
}
