package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
public class BookingShortDto extends BookingDto {
    private Integer bookerId;

    public BookingShortDto(Integer id,
                           LocalDateTime start,
                           LocalDateTime end,
                           int itemId,
                           Status status,
                           Integer bookerId) {
        super(id, start, end, itemId, status);
        this.bookerId = bookerId;
    }
}
