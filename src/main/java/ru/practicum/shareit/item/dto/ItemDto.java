package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
public class ItemDto {
    private String name;
    private String description;
    private boolean available;
    private int requestId;

    public ItemDto(String name, String description, boolean available, int requestId) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}
