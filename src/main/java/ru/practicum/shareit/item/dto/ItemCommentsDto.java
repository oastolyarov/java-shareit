package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemCommentsDto extends ItemBookingsDto {
    private List<CommentDto> comments;

    public ItemCommentsDto(int id,
                           String name,
                           String description,
                           Boolean available,
                           BookingShortDto lastBooking,
                           BookingShortDto nextBooking,
                           List<CommentDto> commentList) {
        super(id, name, description, available, lastBooking, nextBooking);
        this.comments = commentList;
    }
}
