package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
public class BookingDto {
    private LocalDate start;
    private LocalDate end;
    private int itemId;

    public BookingDto(LocalDate start, LocalDate end, int itemId) {
        this.start = start;
        this.end = end;
        this.itemId = itemId;
    }
}


