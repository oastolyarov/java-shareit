package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingMapper {
    public static BookingDto toBokingDto(Booking booking) {
        return new BookingDto(booking.getStart(), booking.getEnd(), booking.getItem().getId());
    }
}
