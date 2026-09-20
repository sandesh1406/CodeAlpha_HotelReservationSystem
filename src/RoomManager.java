package src;
import java.util.*;
import java.util.stream.Collectors;

public class RoomManager {
    private Map<String, Room> rooms;

    public RoomManager() {
        this.rooms = new HashMap<>();
    }

    public void addRoom(Room room) throws IllegalArgumentException {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        if (room.getRoomId() == null || room.getRoomId().trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be empty");
        }
        if (rooms.containsKey(room.getRoomId())) {
            throw new IllegalArgumentException("Duplicate Room ID: " + room.getRoomId());
        }
        if (room.getPricePerNight() <= 0) {
            throw new IllegalArgumentException("Price per night must be positive");
        }
        if (room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (room.getCategory() == null) {
            throw new IllegalArgumentException("Room category cannot be null");
        }

        rooms.put(room.getRoomId(), room);
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public List<Room> getAvailableRooms() {
        return rooms.values().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Room> getRoomsByCategory(RoomCategory category) {
        if (category == null) return getAllRooms();
        return rooms.values().stream()
                .filter(r -> r.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Room> getAvailableRoomsByCategory(RoomCategory category) {
        if (category == null) return getAvailableRooms();
        return rooms.values().stream()
                .filter(r -> r.isAvailable() && r.getCategory() == category)
                .collect(Collectors.toList());
    }

    public boolean isRoomAvailable(String roomId) {
        Room room = rooms.get(roomId);
        return room != null && room.isAvailable();
    }

    public void setRoomAvailability(String roomId, boolean available) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.setAvailable(available);
        }
    }

    public int getTotalRoomsCount() {
        return rooms.size();
    }

    public int getAvailableRoomsCount() {
        return (int) rooms.values().stream().filter(Room::isAvailable).count();
    }

    public int getOccupiedRoomsCount() {
        return getTotalRoomsCount() - getAvailableRoomsCount();
    }

    public void removeRoom(String roomId) throws IllegalArgumentException {
        if (roomId == null || !rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room ID not found: " + roomId);
        }
        rooms.remove(roomId);
    }

    public void updateRoom(Room updatedRoom) throws IllegalArgumentException {
        if (updatedRoom == null) {
            throw new IllegalArgumentException("Updated room cannot be null");
        }
        if (!rooms.containsKey(updatedRoom.getRoomId())) {
            throw new IllegalArgumentException("Room ID not found: " + updatedRoom.getRoomId());
        }
        if (updatedRoom.getPricePerNight() <= 0) {
            throw new IllegalArgumentException("Price per night must be positive");
        }
        if (updatedRoom.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (updatedRoom.getCategory() == null) {
            throw new IllegalArgumentException("Room category cannot be null");
        }
        rooms.put(updatedRoom.getRoomId(), updatedRoom);
    }

    public void initializeDefaultInventory() {
        // Standard Rooms (101-106) - ₹2000, Cap 2
        for (int i = 101; i <= 106; i++) {
            addRoom(new Room(String.valueOf(i), RoomCategory.STANDARD, 2000.0, 2, true));
        }
        // Deluxe Rooms (201-206) - ₹3500, Cap 3
        for (int i = 201; i <= 206; i++) {
            addRoom(new Room(String.valueOf(i), RoomCategory.DELUXE, 3500.0, 3, true));
        }
        // Suite Rooms (301-304) - ₹6000, Cap 4
        for (int i = 301; i <= 304; i++) {
            addRoom(new Room(String.valueOf(i), RoomCategory.SUITE, 6000.0, 4, true));
        }
    }
}
