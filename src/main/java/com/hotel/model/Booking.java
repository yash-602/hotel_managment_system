package com.hotel.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Booking implements Serializable {
    private static final long serialVersionUID = 2L;

    private int bookingId;
    private int customerId;
    private int roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double roomBill;
    private double amenitiesCost;
    private double discountAmount;
    private double totalBill;
    private boolean active;
    private List<String> amenities;

    public Booking(int bookingId, int customerId, int roomNumber,
                   LocalDate checkIn, LocalDate checkOut,
                   double pricePerDay, boolean loyaltyDiscount,
                   List<String> amenities, double amenitiesCost) {
        this.bookingId     = bookingId;
        this.customerId    = customerId;
        this.roomNumber    = roomNumber;
        this.checkIn       = checkIn;
        this.checkOut      = checkOut;
        this.amenities     = amenities;
        this.amenitiesCost = amenitiesCost;

        long days    = checkOut.toEpochDay() - checkIn.toEpochDay();
        this.roomBill = days * pricePerDay;

        double subtotal = this.roomBill + amenitiesCost;
        this.discountAmount = loyaltyDiscount ? subtotal * 0.20 : 0.0;
        this.totalBill      = subtotal - discountAmount;
        this.active         = true;
    }

    public int        getBookingId()      { return bookingId; }
    public int        getCustomerId()     { return customerId; }
    public int        getRoomNumber()     { return roomNumber; }
    public LocalDate  getCheckIn()        { return checkIn; }
    public LocalDate  getCheckOut()       { return checkOut; }
    public double     getRoomBill()       { return roomBill; }
    public double     getAmenitiesCost()  { return amenitiesCost; }
    public double     getDiscountAmount() { return discountAmount; }
    public double     getTotalBill()      { return totalBill; }
    public boolean    isActive()          { return active; }
    public List<String> getAmenities()    { return amenities; }
    public void setActive(boolean active) { this.active = active; }
}