package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static Comment toCommentDto(CommentDto commentDto, Item item, User author, LocalDateTime created) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                item,
                author,
                created);
    }
}
