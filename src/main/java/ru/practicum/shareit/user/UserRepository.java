package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query("update User u set u.name = ?1, u.email = ?2 where u.id = ?3")
    void setUserInfoById(String name, String email, Integer userId);

    @Query("select u from User u where lower(u.email) = lower(?1)")
    Optional<User> getUserByEmail(String email);
}
