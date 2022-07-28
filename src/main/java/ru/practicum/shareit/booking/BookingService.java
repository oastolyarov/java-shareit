package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingLongDto;

import java.util.List;

public interface BookingService {
    public BookingDto toBook(BookingDto bookingDto, Integer userId);

    public BookingLongDto setStatus(Integer bookingId, Boolean approved, Integer userId);

    public BookingLongDto getStatus(Integer bookingId, Integer userId);

    public List<BookingLongDto> getUserBookings(String state, Integer userId);

    public List<BookingLongDto> getOwnerBookings(String state, Integer userId);
}
