package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    public BookingDto toBook(BookingDto bookingDto, Integer userId);

    public Booking setStatus(Integer bookingId, Boolean approved, Integer userId);

    public Booking getStatus(Integer bookingId, Integer userId);

    public List<Booking> getUserBookings(String state, Integer userId);

    public List<Booking> getOwnerBookings(String state, Integer userId);
}
