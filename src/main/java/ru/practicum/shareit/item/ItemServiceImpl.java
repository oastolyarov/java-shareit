package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserIdNotValidException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingsDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserServiceImpl userServiceImpl;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserServiceImpl userServiceImpl, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userServiceImpl = userServiceImpl;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto create(ItemDto itemDto, Integer userId) {

        UserDto userDto = userServiceImpl.getUserById(userId);
        itemDto.setId(0);

        return ItemMapper.toItemDto(itemRepository
                .save(ItemMapper.toItem(itemDto,
                        UserMapper.toUser(userServiceImpl.getUserById(userId)))));
    }

    @Override
    public ItemDto updateById(ItemDto itemDto, int userId, int itemId) {
        UserDto userDto = userServiceImpl.getUserById(userId);
        ItemDto currentItem = ItemMapper.toItemDto(itemRepository.findById(itemId).get());
        if (userDto.getId() != itemRepository.getItemOwnerId(itemId).getId()) {
            throw new UserIdNotValidException("Предмет принадлежит другому пользователю.");
        }

        String name = itemDto.getName() == null ?
                currentItem.getName() :
                itemDto.getName();

        String description = itemDto.getDescription() == null ?
                currentItem.getDescription() :
                itemDto.getDescription();

        Boolean available = itemDto.getAvailable() == null ?
                currentItem.getAvailable() :
                itemDto.getAvailable();

        itemRepository.setItemInfoById(name, description, available, itemId);

        return new ItemDto(itemId, name, description, available);
    }

    @Override
    public ItemCommentsDto getById(int id, Integer userId) {

        if (itemRepository.findById(id).isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + id + " не найден.");
        }

        List<CommentDto> commentList = getComments(id);

        BookingShortDto lastBooking;
        BookingShortDto nextBooking;

        Item item = itemRepository.findById(id).get();

        if (item.getOwner().getId() != userId) {
            return new ItemCommentsDto(item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    null,
                    null,
                    commentList);
        }

        // проверяю, что у предмета были бронирования
        Optional<List<Booking>> bookings = bookingRepository.getBookingByItemId(id);

        if (bookings.isEmpty()) {
            return new ItemCommentsDto(item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    null,
                    null,
                    commentList);
        }

        // получаю список прошедших бронирований
        List<Booking> lastBookingList = bookings.get().stream()
                .filter(s -> s.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        // проверяю, что список с учетом фильтров не пустой
        if (lastBookingList.isEmpty()) {
            lastBooking = null;
        } else {
            Optional<Booking> booking1 = lastBookingList.stream()
                    .min((o1, o2) -> o2.getEnd().compareTo(o1.getEnd()));

            lastBooking = BookingMapper.toBookingShortDto(booking1.get());
        }

        // получаю список будущих бронирований
        List<Booking> nextBookingList = bookings.get().stream()
                .filter(s -> s.getStart().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (nextBookingList.isEmpty()) {
            nextBooking = null;
        } else {
            Optional<Booking> booking2 = nextBookingList.stream()
                    .min(((o1, o2) -> o2.getEnd().compareTo(o1.getEnd())));

            nextBooking = BookingMapper.toBookingShortDto(booking2.get());
        }
        return new ItemCommentsDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                commentList);
    }

    @Override
    public List<ItemBookingsDto> getAll(int userId) {
        List<ItemBookingsDto> itemBookingsDtos = new ArrayList<>();

        List<Item> items = itemRepository.findAll();
        items = items.stream().filter(s -> s.getOwner().getId() == userId).collect(Collectors.toList());

        for (int i = 0; i < items.size(); i++) {
            itemBookingsDtos.add(getById(items.get(i).getId(), userId));
        }

        return itemBookingsDtos;
    }

    @Override
    public List<ItemDto> search(String text) {

        if (text == null || text.equals("")) {
            return new ArrayList<>();
        }

        return itemRepository.search(text).stream().filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto setComment(int itemId, Comment comment, int authorId) {

        if (comment == null || comment.getText() == null || comment.getText().equals("")) {
            throw new NullPointerException("Комментарий не может быть пустым.");
        }

        Optional<List<Booking>> booking = bookingRepository.getBookingByItemIdAndUserId(itemId, authorId);

        List<Booking> bookings = List.copyOf(booking.get());

        if (bookings.isEmpty()) {
            throw new NullPointerException(String
                    .format("Пользователь с id %d не бронировал предмет с id %d", authorId, itemId));
        }

        bookings = bookings.stream()
                .filter(s -> s.getStatus().equals(Status.APPROVED))
                .filter(s -> s.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (bookings.isEmpty()) {
            throw new NullPointerException(String
                    .format("Пользователь с id %d не бронировал предмет с id %d", authorId, itemId));
        }

        User user = UserMapper.toUser(userServiceImpl.getUserById(authorId));

        Item item = ItemMapper.toItem(getById(itemId, authorId), user);

        comment.setItem(item);
        comment.setAuthor(user);

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private List<CommentDto> getComments(Integer itemId) {
        return commentRepository.getCommentByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
