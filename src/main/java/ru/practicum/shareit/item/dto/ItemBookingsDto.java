package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;

@Data
@NoArgsConstructor
public class ItemBookingsDto extends ItemDto {
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;

    public ItemBookingsDto(int id,
                           String name,
                           String description,
                           Boolean available,
                           BookingShortDto lastBooking,
                           BookingShortDto nextBooking) {
        super(id, name, description, available);
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }
}
