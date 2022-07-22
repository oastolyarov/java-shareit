package ru.practicum.shareit.exceptions;

public class BookingIsNotValid extends RuntimeException {
    public BookingIsNotValid(String message) {
        super(message);
    }
}
