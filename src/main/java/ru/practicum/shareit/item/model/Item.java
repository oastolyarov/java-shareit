package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
public class Item {
    private int id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private ItemRequest request;

    public Item(int id, String name, String description, boolean available, User user, int requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = user;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestId);
        this.request = itemRequest;
    }
}

