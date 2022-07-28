package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingDto {
    private Integer id = 0;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
    private Status status;

    public BookingDto(Integer id, LocalDateTime start, LocalDateTime end, int itemId, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.status = status;
    }
}


