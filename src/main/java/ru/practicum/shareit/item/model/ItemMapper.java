package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(ItemDto itemDto, int id, User user) {
        return new Item(
                id,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                user,
                itemDto.getRequestId()
        );
    }
}
