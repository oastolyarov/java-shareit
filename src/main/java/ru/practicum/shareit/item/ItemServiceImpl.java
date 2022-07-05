package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    private int id = 1;
    private Map<Integer, Item> items = new HashMap<>();

    @Override
    public ItemDto create(ItemDto itemDto, User user) {
        items.put(id, ItemMapper.toItem(itemDto, id, user));
        id++;
        return itemDto;
    }

    @Override
    public ItemDto update(Item item, int userId) {
        for (int i : items.keySet()) {
            if (items.get(i).getOwner().getId() == userId) {
                items.get(i).setName(item.getName());
                items.get(i).setDescription(item.getDescription());
                return ItemMapper.toItemDto(item);
            }
        }
        return null;
    }

    @Override
    public ItemDto getById(int id) {
        return ItemMapper.toItemDto(items.get(id));
    }

    @Override
    public List<Item> getAll(int userId) {
        List<Item> userItem = new ArrayList<>();

        for (int i : items.keySet()) {
            if (items.get(i).getOwner().getId() == userId) {
                userItem.add(items.get(i));
            }
        }
        return userItem;
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> searchItems = new ArrayList<>();

        for (int i : items.keySet()) {
            if (items.get(i).getName().contains(text) || items.get(i).getDescription().contains(text)) {
                searchItems.add(ItemMapper.toItemDto(items.get(i)));
            }
        }

        return searchItems;
    }
}
