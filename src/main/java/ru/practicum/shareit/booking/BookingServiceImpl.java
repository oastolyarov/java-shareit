package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingLongDto;
import ru.practicum.shareit.exceptions.BookingIsNotValid;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserIdNotValidException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserService userService,
                              ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingDto toBook(BookingDto bookingDto, Integer userId) {

        if (itemRepository.findById(bookingDto.getItemId()).isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + bookingDto.getItemId() + " не найден.");
        }

        Item item = itemRepository.findById(bookingDto.getItemId()).get();

        if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new UserIdNotValidException("Владелец вещи не может бронировать саму вещь.");
        }

        if (!item.getAvailable()) {
            throw new NullPointerException("Предмет недоступен.");
        }

        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new NullPointerException("Некорректно указаны даты.");
        }

        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(UserMapper.toUser(userService.getUserById(userId)));
        booking.setStatus(Status.WAITING);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingLongDto setStatus(Integer bookingId, Boolean approved, Integer userId) {
        Item item = bookingRepository.getItemByBookingId(bookingId);
        Booking booking = bookingRepository.findById(bookingId).get();

        if ((booking.getStatus().equals(Status.APPROVED) && approved)) {
            throw new NullPointerException("Статус уже установлен.");
        }

        if ((booking.getStatus().equals(Status.REJECTED) && !approved)) {
            throw new NullPointerException("Статус уже установлен.");
        }

        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new UserIdNotValidException(String.format("Пользователь с id %d не является владельцем вещи.", userId));
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
            bookingRepository.setBookingStatus(bookingId, Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
            bookingRepository.setBookingStatus(bookingId, Status.REJECTED);
        }

        return BookingMapper.toBookingLongDto(booking);
    }

    @Override
    public BookingLongDto getStatus(Integer bookingId, Integer userId) {
        Item item = bookingRepository.getItemByBookingId(bookingId);
        Booking booking = new Booking();
        try {
            booking = bookingRepository.findById(bookingId).get();
        } catch (Exception e) {
            throw new BookingIsNotValid("Бронирование не найдено.");
        }

        if (Objects.equals(item.getOwner().getId(), userId) || Objects.equals(booking.getBooker().getId(), userId)) {
            return BookingMapper.toBookingLongDto(booking);
        } else {
            throw new UserIdNotValidException("Указан некорректный пользователь.");
        }
    }

    @Override
    public List<BookingLongDto> getUserBookings(String state, Integer userId) {
        List<Booking> bookings = bookingRepository.getBookingsOfUser(userId);

        return getFromState(state, bookings).stream()
                .map(BookingMapper::toBookingLongDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingLongDto> getOwnerBookings(String state, Integer userId) {


        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(s -> Objects.equals(s.getItem().getOwner().getId(), userId))
                .collect(Collectors.toList());

        return getFromState(state, bookings).stream()
                .map(BookingMapper::toBookingLongDto)
                .collect(Collectors.toList());
    }

    private List<Booking> getFromState(String state, List<Booking> bookings) {
        Comparator<Booking> comparator = (o1, o2) -> o2.getStart().compareTo(o1.getStart());

        if (state != null) {

            switch (state) {
                case "CURRENT": {
                    return bookings.stream()
                            .filter(s -> s.getStart().isBefore(LocalDateTime.now()) &&
                                    s.getEnd().isAfter(LocalDateTime.now()))
                            .sorted(comparator)
                            .collect(Collectors.toList());
                }
                case "PAST": {
                    return bookings.stream()
                            .filter(s -> s.getEnd().isBefore(LocalDateTime.now()))
                            .sorted(comparator)
                            .collect(Collectors.toList());
                }
                case "FUTURE": {
                    return bookings.stream()
                            .filter(s -> s.getStart().isAfter(LocalDateTime.now()))
                            .sorted(comparator)
                            .collect(Collectors.toList());
                }
                case "WAITING": {
                    return bookings.stream()
                            .filter(s -> s.getStatus().equals(Status.WAITING))
                            .sorted(comparator)
                            .collect(Collectors.toList());
                }
                case "REJECTED": {
                    return bookings.stream()
                            .filter(s -> s.getStatus().equals(Status.REJECTED))
                            .sorted(comparator)
                            .collect(Collectors.toList());
                }
            }
        }
        return bookings.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
