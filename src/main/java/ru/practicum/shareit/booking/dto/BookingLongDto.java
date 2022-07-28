package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingLongDto extends Booking {

    public BookingLongDto(Integer id,
                          LocalDateTime start,
                          LocalDateTime end,
                          Item item,
                          User booker,
                          Status status) {
        super(id, start, end, item, booker, status);
    }
}


