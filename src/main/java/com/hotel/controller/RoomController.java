package com.hotel.controller;

import com.hotel.model.Room;
import com.hotel.storage.FileStorage;
import java.util.List;
import java.util.stream.Collectors;

public class RoomController {
    private List<Room> rooms;

    public RoomController() {
        rooms = FileStorage.loadRooms();
    }

    public void addRoom(int number, String type, double price) {
        for (Room r : rooms)
            if (r.getRoomNumber() == number) throw new IllegalArgumentException("Room already exists!");
        rooms.add(new Room(number, type, price));
        FileStorage.saveRooms(rooms);
    }

    public List<Room> getAllRooms()       { return rooms; }

    public List<Room> getAvailableRooms() {
        return rooms.stream().filter(Room::isAvailable).collect(Collectors.toList());
    }

    public Room findRoom(int number) {
        return rooms.stream().filter(r -> r.getRoomNumber() == number)
                             .findFirst().orElse(null);
    }

    public void setAvailability(int number, boolean available) {
        Room r = findRoom(number);
        if (r != null) { r.setAvailable(available); FileStorage.saveRooms(rooms); }
    }
}