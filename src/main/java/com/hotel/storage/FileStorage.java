package com.hotel.storage;

import com.hotel.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {

    private static final String ROOMS_FILE    = "rooms.dat";
    private static final String CUSTOMERS_FILE = "customers.dat";
    private static final String BOOKINGS_FILE  = "bookings.dat";

    @SuppressWarnings("unchecked")
    public static <T> List<T> load(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static <T> void save(String filename, List<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    public static List<Room>     loadRooms()     { return load(ROOMS_FILE); }
    public static List<Customer> loadCustomers() { return load(CUSTOMERS_FILE); }
    public static List<Booking>  loadBookings()  { return load(BOOKINGS_FILE); }

    public static void saveRooms(List<Room> r)        { save(ROOMS_FILE, r); }
    public static void saveCustomers(List<Customer> c){ save(CUSTOMERS_FILE, c); }
    public static void saveBookings(List<Booking> b)  { save(BOOKINGS_FILE, b); }
}