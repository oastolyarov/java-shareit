package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
public class Booking {
    private int id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private User booker;
    private Status status;
}

class BookingMapper {
    public static BookingDto toBokingDto(Booking booking) {
        return new BookingDto(booking.getStart(), booking.getEnd(), booking.getItem().getId());
    }
}
