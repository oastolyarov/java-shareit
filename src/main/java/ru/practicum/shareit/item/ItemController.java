package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserController;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final UserController userController;

    public ItemController(ItemService itemService, UserController userController) {
        this.itemService = itemService;
        this.userController = userController;
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.create(itemDto, userController.getUserById(userId));
    }

    @PutMapping
    public ItemDto update(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.update(item, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable int itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<Item> getAll(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
