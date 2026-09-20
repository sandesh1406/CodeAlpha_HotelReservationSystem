package src;
import java.io.Serializable;
import java.util.*;

public class ApplicationData implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Room> rooms;
    private List<Reservation> reservations;
    private List<Guest> guests;
    private List<Payment> payments;
    private int reservationCounter;
    private int transactionCounter;

    public ApplicationData(List<Room> rooms, List<Reservation> reservations, List<Guest> guests, List<Payment> payments, int reservationCounter, int transactionCounter) {
        this.rooms = rooms;
        this.reservations = reservations;
        this.guests = guests;
        this.payments = payments;
        this.reservationCounter = reservationCounter;
        this.transactionCounter = transactionCounter;
    }

    public List<Room> getRooms() { return rooms; }
    public List<Reservation> getReservations() { return reservations; }
    public List<Guest> getGuests() { return guests; }
    public List<Payment> getPayments() { return payments; }
    public int getReservationCounter() { return reservationCounter; }
    public int getTransactionCounter() { return transactionCounter; }
}
