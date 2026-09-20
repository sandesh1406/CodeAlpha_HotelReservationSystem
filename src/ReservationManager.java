package src;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationManager {
    private Map<String, Reservation> reservations;
    private int reservationCounter = 1000;

    public ReservationManager() {
        this.reservations = new HashMap<>();
    }

    public void setReservationCounter(int counter) {
        this.reservationCounter = counter;
    }

    public int getReservationCounter() {
        return reservationCounter;
    }

    public String generateReservationId() {
        reservationCounter++;
        return "BK" + reservationCounter;
    }

    public Reservation createReservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut, int guests) {
        if (guest == null || room == null || checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Missing required booking information");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        if (guests < 1 || guests > room.getCapacity()) {
            throw new IllegalArgumentException("Invalid number of guests for this room capacity");
        }
        if (!isRoomAvailable(room, checkIn, checkOut)) {
            throw new IllegalArgumentException("Room is no longer available for the selected dates");
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalAmount = nights * room.getPricePerNight();

        String id = generateReservationId();
        Reservation reservation = new Reservation(
                id,
                guest,
                room,
                checkIn,
                checkOut,
                guests,
                totalAmount,
                ReservationStatus.CONFIRMED,
                null // Payment is PENDING/null in this phase
        );

        reservations.put(id, reservation);
        return reservation;
    }

    public void addReservation(Reservation reservation) {
        if (reservation == null) throw new IllegalArgumentException("Reservation cannot be null");
        reservations.put(reservation.getReservationId(), reservation);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations.values());
    }

    public void cancelReservation(String reservationId) {
        Reservation res = reservations.get(reservationId);
        if (res == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }
        if (res.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalArgumentException("This reservation has already been cancelled.");
        }
        res.setStatus(ReservationStatus.CANCELLED);
    }

    public boolean isReservationCancellable(String reservationId) {
        Reservation res = reservations.get(reservationId);
        return res != null && res.getStatus() != ReservationStatus.CANCELLED;
    }

    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        if (room == null) return false;
        if (checkIn == null || checkOut == null) return false;
        if (!checkOut.isAfter(checkIn)) return false;

        return reservations.values().stream()
                .filter(res -> res.getRoom().getRoomId().equals(room.getRoomId()))
                .filter(res -> res.getStatus() == ReservationStatus.CONFIRMED || res.getStatus() == ReservationStatus.CHECKED_IN)
                .noneMatch(res ->
                    res.getCheckInDate().isBefore(checkOut) &&
                    res.getCheckOutDate().isAfter(checkIn)
                );
    }

    public List<Room> searchAvailableRooms(
            List<Room> allRooms,
            LocalDate checkIn,
            LocalDate checkOut,
            RoomCategory category,
            int guests) {

        if (checkIn == null || checkOut == null) return Collections.emptyList();
        if (!checkOut.isAfter(checkIn)) return Collections.emptyList();
        if (guests <= 0) return Collections.emptyList();

        return allRooms.stream()
                .filter(room -> category == null || room.getCategory() == category)
                .filter(room -> room.getCapacity() >= guests)
                .filter(room -> isRoomAvailable(room, checkIn, checkOut))
                .collect(Collectors.toList());
    }

    public int getActiveReservationsCount() {
        return (int) reservations.values().stream()
                .filter(res -> res.getStatus() == ReservationStatus.CONFIRMED || res.getStatus() == ReservationStatus.CHECKED_IN)
                .count();
    }

    public void checkIn(String reservationId) {
        Reservation res = requireReservation(reservationId);
        if (res.getStatus() != ReservationStatus.CONFIRMED) throw new IllegalArgumentException("Only a confirmed reservation can be checked in.");
        res.setStatus(ReservationStatus.CHECKED_IN);
    }

    public void checkOut(String reservationId) {
        Reservation res = requireReservation(reservationId);
        if (res.getStatus() != ReservationStatus.CHECKED_IN && res.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("Reservation is not currently checked in.");
        }
        res.setStatus(ReservationStatus.CHECKED_OUT);
    }

    private Reservation requireReservation(String id) {
        Reservation res = reservations.get(id);
        if (res == null) throw new IllegalArgumentException("Reservation not found.");
        if (res.getStatus() == ReservationStatus.CANCELLED) throw new IllegalArgumentException("Reservation is cancelled.");
        return res;
    }

    public int getTotalReservationsCount() {
        return reservations.size();
    }

    public int getCancelledReservationsCount() {
        return (int) reservations.values().stream()
                .filter(res -> res.getStatus() == ReservationStatus.CANCELLED)
                .count();
    }
}
