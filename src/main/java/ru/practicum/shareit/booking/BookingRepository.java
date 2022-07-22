package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Transactional
    @Modifying
    @Query("update Booking b set b.status = ?2 where b.id = ?1")
    void setBookingStatus(Integer bookingId, Status status);

    @Query("select i from Booking b left join Item i on b.item = i where b.id = ?1")
    Item getItemByBookingId(Integer bookingId);

    @Query("select b from Booking b where b.booker.id = ?1")
    List<Booking> getBookingsOfUser(Integer bookerId);

    @Query("select b from Booking b left join Item i on i.id = b.item.id where i.owner.id = ?1")
    List<Booking> getBookingsOfOwner(Integer ownerId);

    @Query("select b from Booking b where b.item.id = ?1")
    Optional<List<Booking>> getBookingByItemId(Integer itemId);

    @Query("select b from Booking b where b.item.id = ?1 and b.booker.id = ?2")
    Optional<List<Booking>> getBookingByItemIdAndUserId(Integer itemId, Integer userId);
}
