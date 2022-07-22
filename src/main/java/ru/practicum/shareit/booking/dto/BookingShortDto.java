package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingShortDto extends BookingDto {
    private Integer bookerId;

    public BookingShortDto(Integer id, LocalDateTime start, LocalDateTime end, int itemId, Integer bookerId) {
        super(id, start, end, itemId);
        this.bookerId = bookerId;
    }
}
