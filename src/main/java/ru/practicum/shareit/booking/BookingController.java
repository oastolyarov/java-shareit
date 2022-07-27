package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingLongDto;

import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto toBook(@RequestBody BookingDto bookingDto,
                             @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.toBook(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingLongDto setStatus(@PathVariable Integer bookingId,
                                    @RequestParam Boolean approved,
                                    @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.setStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingLongDto getStatus(@PathVariable Integer bookingId,
                             @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getStatus(bookingId, userId);
    }

    @GetMapping
    public List<BookingLongDto> getUserBookings(@RequestParam(required = false) String state,
                                         @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getUserBookings(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingLongDto> getOwnerBookings(@RequestParam(required = false) String state,
                                          @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getOwnerBookings(state, userId);
    }
}
