package ru.practicum.shareit.requests.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    private String description;
    private LocalDateTime created;

    public ItemRequestDto(String description, LocalDateTime created) {
        this.description = description;
        this.created = created;
    }
}
