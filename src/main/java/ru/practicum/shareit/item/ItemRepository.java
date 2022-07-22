package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Transactional
    @Modifying
    @Query("update Item i set i.name = ?1, i.description = ?2, i.available = ?3 where i.id = ?4")
    void setItemInfoById(String name, String description, Boolean b, Integer itemId);

    @Query("select i.owner from Item i where i.id = ?1")
    User getItemOwnerId(Integer itemId);

    @Query("select i from Item i where lower(i.name) like lower(concat('%', ?1, '%')) or " +
            "lower(i.description) like lower(concat('%', ?1, '%'))")
    List<Item> search(String text);

    @Transactional
    @Modifying
    @Query("update Item i set i.available = ?1 where i.id = ?2")
    void setAvailableById(Boolean available, Integer itemId);


}
