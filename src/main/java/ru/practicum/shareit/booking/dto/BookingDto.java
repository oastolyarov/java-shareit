package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingDto {
    private Integer id = 0;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;

    public BookingDto(Integer id, LocalDateTime start, LocalDateTime end, int itemId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
    }
}


