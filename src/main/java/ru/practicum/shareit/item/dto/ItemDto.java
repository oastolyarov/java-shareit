package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDto {
    private Integer id = 0;
    private String name;
    private String description;
    private Boolean available;

    public ItemDto(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemDto() {

    }
}
