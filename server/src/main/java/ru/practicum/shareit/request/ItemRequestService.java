package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    /**
     * Создает новую заявку от текущего пользователя
     * @param newItemRequestDto Данные заявки
     * @return Вновь созданная заявка
     */
    ItemRequestDto createItemRequest(NewItemRequestDto newItemRequestDto);

    /**
     * Получение коллекции заявок указанного пользователя.
     *
     * @param userId Указанный пользователь
     * @return Коллекция заявок с ответами
     */
    List<ItemRequestWithAnswersDto> getUserItemRequests(long userId);

    /**
     * Получение коллекции заявок от других пользователей
     *
     * @param userId Текущий пользователь
     * @return Коллекция заявок
     */
    List<ItemRequestDto> getOtherUsersItemRequests(long userId);

    /**
     * Получение подробной информации по конкретной заявке с указанным id
     * @param requestId Id заявки
     * @return Данные заявки с ответами
     */
    ItemRequestWithAnswersDto getItemRequestById(long requestId);
}
