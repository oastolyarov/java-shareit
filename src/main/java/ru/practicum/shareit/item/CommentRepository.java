package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Comment;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Transactional
    @Modifying
    @Query("update Booking b set b.status = ?2 where b.id = ?1")
    void setBookingStatus(Integer bookingId, Status status);

    @Query("select c from Comment c where c.item.id = ?1")
    List<Comment> getCommentByItemId(Integer itemId);
}
