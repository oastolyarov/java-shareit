package ru.practicum.shareit.requests;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private int id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}

