package com.hotel.model;

import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private int roomNumber;
    private String roomType;   // Single, Double, Deluxe, Suite
    private double pricePerDay;
    private boolean available;

    public Room(int roomNumber, String roomType, double pricePerDay) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;
        this.available = true;
    }

    public int getRoomNumber()       { return roomNumber; }
    public String getRoomType()      { return roomType; }
    public double getPricePerDay()   { return pricePerDay; }
    public boolean isAvailable()     { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " | " + roomType + 
               " | ₹" + pricePerDay + " | " + (available ? "Available" : "Booked");
    }
}