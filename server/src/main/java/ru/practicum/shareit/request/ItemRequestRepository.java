package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    /**
     * Поиск всех заявок от пользователя с id
     * @param requestorId Id пользователя
     * @return Список заявок
     */
    List<ItemRequest> findAllItemsRequestsByRequestorIdOrderByCreatedDesc(Long requestorId);

    /**
     * Поиск всех заявок от пользователей с id, не равным заданному
     * @param requestorId Id пользователя
     * @return Список заявок
     */
    List<ItemRequest> findItemRequestsByRequestorIdNot(Long requestorId);


}
