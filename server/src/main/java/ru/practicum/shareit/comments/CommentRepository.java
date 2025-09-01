package ru.practicum.shareit.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Возвращает список комментариев для указанного предмета
     * @param item Предмет
     * @return Список комментариев
     */
    Collection<Comment> findAllByItem(Item item);
}
