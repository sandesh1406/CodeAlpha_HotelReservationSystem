package src;
import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roomId;
    private RoomCategory category;
    private double pricePerNight;
    private int capacity;
    private boolean available;

    public Room(String roomId, RoomCategory category, double pricePerNight, int capacity, boolean available) {
        this.roomId = roomId;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.available = available;
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public RoomCategory getCategory() { return category; }
    public void setCategory(RoomCategory category) { this.category = category; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public void markAsBooked() { this.available = false; }
    public void markAsAvailable() { this.available = true; }

    @Override
    public String toString() {
        return "Room{" + "id='" + roomId + '\'' + ", category=" + category + ", price=" + pricePerNight + ", available=" + available + '}';
    }
}
