package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Проверка, что существует пользователь с указанной электронной почтой
     * @param email Электронная почта
     * @return true, если пользователь с указанной электронной почтой существует
     */
    boolean existsByEmail(String email);
}
