package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") String userId) {


        if (itemDto.getName() == "" || itemDto.getName() == null ||
                itemDto.getDescription() == null || itemDto.getDescription() == "") {
            throw new NullPointerException("Не указано название или описание предмета.");
        }

        if (itemDto.getAvailable() == null) {
            throw new NullPointerException("Укажите доступность предмета.");
        }

        return itemService.create(itemDto, Integer.parseInt(userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateById(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId) {

        return itemService.updateById(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable int itemId,
                           @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemBookingsDto> getAll(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto setComment(@PathVariable int itemId,
                                 @RequestBody Comment comment,
                                 @RequestHeader("X-Sharer-User-Id") int authorId) {
        return itemService.setComment(itemId, comment, authorId);
    }
}
