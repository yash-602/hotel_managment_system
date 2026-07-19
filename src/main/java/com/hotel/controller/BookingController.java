package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.storage.FileStorage;
import java.time.LocalDate;
import java.util.List;

public class BookingController {
    private List<Booking> bookings;
    private int nextId = 1;

    public BookingController() {
        bookings = FileStorage.loadBookings();
        if (!bookings.isEmpty())
            nextId = bookings.stream()
                .mapToInt(Booking::getBookingId).max().getAsInt() + 1;
    }

    public Booking book(int customerId, int roomNumber,
                        LocalDate checkIn, LocalDate checkOut,
                        double pricePerDay, boolean loyaltyDiscount,
                        List<String> amenities, double amenitiesCost) {
        Booking b = new Booking(nextId++, customerId, roomNumber,
                                checkIn, checkOut, pricePerDay,
                                loyaltyDiscount, amenities, amenitiesCost);
        bookings.add(b);
        FileStorage.saveBookings(bookings);
        return b;
    }

    public void checkout(int bookingId) {
        bookings.stream()
            .filter(b -> b.getBookingId() == bookingId)
            .findFirst()
            .ifPresent(b -> b.setActive(false));
        FileStorage.saveBookings(bookings);
    }

    public List<Booking> getAllBookings()  { return bookings; }

    public List<Booking> getActiveBookings() {
        return bookings.stream().filter(Booking::isActive).toList();
    }
}