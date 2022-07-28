package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {
    public ItemDto create(ItemDto itemDto, Integer userId);

    public ItemDto updateById(ItemDto itemDto, int userId, int itemId);

    public ItemDto getById(int id, Integer userId);

    public List<ItemBookingsDto> getAll(int userId);

    public List<ItemDto> search(String text);

    public CommentDto setComment(int itemId, Comment comment, int authorId);

}
