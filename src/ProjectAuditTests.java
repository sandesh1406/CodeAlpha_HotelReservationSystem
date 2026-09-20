package src;
import java.time.LocalDate;
import java.util.*;
import java.io.*;

public class ProjectAuditTests {
    public static void main(String[] args) {
        System.out.println("Running Phase 1-7 Project Audit Tests...");
        int passed = 0;
        int total = 0;

        try {
            // Phase 2-6 Tests
            total++; testRoomCreation(); passed++; System.out.println("PASSED: testRoomCreation");
            total++; testDuplicateRoomId(); passed++; System.out.println("PASSED: testDuplicateRoomId");
            total++; testInvalidRoomData(); passed++; System.out.println("PASSED: testInvalidRoomData");
            total++; testRoomLookup(); passed++; System.out.println("PASSED: testRoomLookup");
            total++; testCategoryFiltering(); passed++; System.out.println("PASSED: testCategoryFiltering");
            total++; testAvailabilityRetrieval(); passed++; System.out.println("PASSED: testAvailabilityRetrieval");
            total++; testAvailabilityUpdate(); passed++; System.out.println("PASSED: testAvailabilityUpdate");
            total++; testValidDateRange(); passed++; System.out.println("PASSED: testValidDateRange");
            total++; testInvalidDateRange(); passed++; System.out.println("PASSED: testInvalidDateRange");
            total++; testRoomAvailableNoReservation(); passed++; System.out.println("PASSED: testRoomAvailableNoReservation");
            total++; testRoomUnavailableOverlap(); passed++; System.out.println("PASSED: testRoomUnavailableOverlap");
            total++; testRoomAvailableNoOverlap(); passed++; System.out.println("PASSED: testRoomAvailableNoOverlap");
            total++; testCancelledReservationNotBlocking(); passed++; System.out.println("PASSED: testCancelledReservationNotBlocking");
            total++; testGuestCapacityFiltering(); passed++; System.out.println("PASSED: testGuestCapacityFiltering");
            total++; testCombinedFiltering(); passed++; System.out.println("PASSED: testCombinedFiltering");
            total++; testSameDayCheckoutCheckin(); passed++; System.out.println("PASSED: testSameDayCheckoutCheckin");
            total++; testSuccessfulBooking(); passed++; System.out.println("PASSED: testSuccessfulBooking");
            total++; testReservationIdGeneration(); passed++; System.out.println("PASSED: testReservationIdGeneration");
            total++; testGuestInfoStorage(); passed++; System.out.println("PASSED: testGuestInfoStorage");
            total++; testBookingCalculation(); passed++; System.out.println("PASSED: testBookingCalculation");
            total++; testBookingStatus(); passed++; System.out.println("PASSED: testBookingStatus");
            total++; testFindReservationById(); passed++; System.out.println("PASSED: testFindReservationById");
            total++; testOverlappingBookingRejection(); passed++; System.out.println("PASSED: testOverlappingBookingRejection");
            total++; testNonOverlappingBookingAllowed(); passed++; System.out.println("PASSED: testNonOverlappingBookingAllowed");
            total++; testGuestCapacityValidation(); passed++; System.out.println("PASSED: testGuestCapacityValidation");
            total++; testEmptyGuestNameRejection(); passed++; System.out.println("PASSED: testEmptyGuestNameRejection");
            total++; testInvalidDateRangeRejection(); passed++; System.out.println("PASSED: testInvalidDateRangeRejection");
            total++; testPaymentUPI(); passed++; System.out.println("PASSED: testPaymentUPI");
            total++; testPaymentCard(); passed++; System.out.println("PASSED: testPaymentCard");
            total++; testPaymentCash(); passed++; System.out.println("PASSED: testPaymentCash");
            total++; testPaymentTxIdGeneration(); passed++; System.out.println("PASSED: testPaymentTxIdGeneration");
            total++; testPaymentAmountMatch(); passed++; System.out.println("PASSED: testPaymentAmountMatch");
            total++; testPaymentStatusTransition(); passed++; System.out.println("PASSED: testPaymentStatusTransition");
            total++; testPaymentMethodStored(); passed++; System.out.println("PASSED: testPaymentMethodStored");
            total++; testPaymentRetrieval(); passed++; System.out.println("PASSED: testPaymentRetrieval");
            total++; testNegativePaymentRejected(); passed++; System.out.println("PASSED: testNegativePaymentRejected");
            total++; testMissingPaymentMethodRejected(); passed++; System.out.println("PASSED: testMissingPaymentMethodRejected");
            total++; testDuplicatePaymentRejected(); passed++; System.out.println("PASSED: testDuplicatePaymentRejected");
            total++; testCancelledPaymentRejected(); passed++; System.out.println("PASSED: testCancelledPaymentRejected");
            total++; testCancelConfirmedReservation(); passed++; System.out.println("PASSED: testCancelConfirmedReservation");
            total++; testCancelStatusUpdate(); passed++; System.out.println("PASSED: testCancelStatusUpdate");
            total++; testCancelledReservationPersistence(); passed++; System.out.println("PASSED: testCancelledReservationPersistence");
            total++; testDuplicateCancellationRejection(); passed++; System.out.println("PASSED: testDuplicateCancellationRejection");
            total++; testNonExistingCancellationRejection(); passed++; System.out.println("PASSED: testNonExistingCancellationRejection");
            total++; testCancelledRoomAvailability(); passed++; System.out.println("PASSED: testCancelledRoomAvailability");
            total++; testRebookingAfterCancellation(); passed++; System.out.println("PASSED: testRebookingAfterCancellation");
            total++; testPaidReservationCancellation(); passed++; System.out.println("PASSED: testPaidReservationCancellation");
            total++; testCancellationPreservesData(); passed++; System.out.println("PASSED: testCancellationPreservesData");

            // Phase 7 Tests
            total++; testSaveAndLoadBasic(); passed++; System.out.println("PASSED: testSaveAndLoadBasic");
            total++; testPersistenceIntegrity(); passed++; System.out.println("PASSED: testPersistenceIntegrity");
            total++; testMissingFileHandling(); passed++; System.out.println("PASSED: testMissingFileHandling");
            total++; testIdCollisionPrevention(); passed++; System.out.println("PASSED: testIdCollisionPrevention");
            total++; testPersistenceAvailability(); passed++; System.out.println("PASSED: testPersistenceAvailability");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("FAILED: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("\nTest Results: " + passed + "/" + total + " passed.");
        if (passed != total) {
            System.exit(1);
        }
    }

    // --- Phase 2-6 Helpers ---
    private static void testRoomCreation() {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        if (rm.getTotalRoomsCount() != 1) throw new RuntimeException("Room count should be 1");
    }
    private static void testDuplicateRoomId() {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        try { rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true)); throw new RuntimeException("Duplicate ID allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testInvalidRoomData() {
        RoomManager rm = new RoomManager();
        try { rm.addRoom(new Room("", RoomCategory.STANDARD, 2000.0, 2, true)); throw new RuntimeException("Empty ID allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testRoomLookup() {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        if (rm.getRoom("101") == null) throw new RuntimeException("Lookup failed");
    }
    private static void testCategoryFiltering() {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        rm.addRoom(new Room("201", RoomCategory.DELUXE, 3500.0, 3, true));
        if (rm.getRoomsByCategory(RoomCategory.STANDARD).size() != 1) throw new RuntimeException("Cat filter failed");
    }
    private static void testAvailabilityRetrieval() {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        rm.addRoom(new Room("102", RoomCategory.STANDARD, 2000.0, 2, false));
        if (rm.getAvailableRooms().size() != 1) throw new RuntimeException("Availability retrieval failed");
    }
    private static void testAvailabilityUpdate() {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        rm.setRoomAvailability("101", false);
        if (rm.isRoomAvailable("101")) throw new RuntimeException("Availability update failed");
    }
    private static void testValidDateRange() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        if (!resm.isRoomAvailable(room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5))) throw new RuntimeException("Room should be available");
    }
    private static void testInvalidDateRange() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        if (resm.isRoomAvailable(room, LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 1))) throw new RuntimeException("Invalid range allowed");
    }
    private static void testRoomAvailableNoReservation() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        if (!resm.isRoomAvailable(room, LocalDate.now(), LocalDate.now().plusDays(1))) throw new RuntimeException("Should be available");
    }
    private static void testRoomUnavailableOverlap() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Reservation res = new Reservation("R1", null, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 2, 8000.0, ReservationStatus.CONFIRMED, null);
        resm.addReservation(res);
        if (resm.isRoomAvailable(room, LocalDate.of(2026, 10, 2), LocalDate.of(2026, 10, 3))) throw new RuntimeException("Overlap allowed");
    }
    private static void testRoomAvailableNoOverlap() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Reservation res = new Reservation("R1", null, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 2, 8000.0, ReservationStatus.CONFIRMED, null);
        resm.addReservation(res);
        if (!resm.isRoomAvailable(room, LocalDate.of(2026, 10, 6), LocalDate.of(2026, 10, 10))) throw new RuntimeException("No-overlap blocked");
    }
    private static void testCancelledReservationNotBlocking() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Reservation res = new Reservation("R1", null, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 2, 8000.0, ReservationStatus.CANCELLED, null);
        resm.addReservation(res);
        if (!resm.isRoomAvailable(room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5))) throw new RuntimeException("Cancelled blocks");
    }
    private static void testGuestCapacityFiltering() {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        ReservationManager resm = new ReservationManager();
        if (resm.searchAvailableRooms(rm.getAllRooms(), LocalDate.now(), LocalDate.now().plusDays(1), null, 3).size() != 0) throw new RuntimeException("Capacity filter failed");
    }
    private static void testCombinedFiltering() {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        ReservationManager resm = new ReservationManager();
        if (resm.searchAvailableRooms(rm.getAllRooms(), LocalDate.now(), LocalDate.now().plusDays(1), RoomCategory.DELUXE, 1).size() != 0) throw new RuntimeException("Combined filter failed");
    }
    private static void testSameDayCheckoutCheckin() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Reservation res1 = new Reservation("R1", null, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 2, 8000.0, ReservationStatus.CONFIRMED, null);
        resm.addReservation(res1);
        if (!resm.isRoomAvailable(room, LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 10))) throw new RuntimeException("Same day turn blocked");
    }
    private static void testSuccessfulBooking() {
        RoomManager rm = new RoomManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        rm.addRoom(room);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        ReservationManager resm = new ReservationManager();
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 3), 2);
        if (res == null) throw new RuntimeException("Booking failed");
    }
    private static void testReservationIdGeneration() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res1 = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        Reservation res2 = resm.createReservation(guest, room, LocalDate.of(2026, 10, 3), LocalDate.of(2026, 10, 4), 1);
        if (!res1.getReservationId().startsWith("BK") || res1.getReservationId().equals(res2.getReservationId())) {
            throw new RuntimeException("ID generation failed");
        }
    }
    private static void testGuestInfoStorage() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "Sandesh", "1234567890", "sandesh@example.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        if (!res.getGuest().getName().equals("Sandesh")) throw new RuntimeException("Guest info storage failed");
    }
    private static void testBookingCalculation() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 4), 1);
        if (res.getTotalAmount() != 6000.0) throw new RuntimeException("Total amount calculation failed");
    }
    private static void testBookingStatus() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        if (res.getStatus() != ReservationStatus.CONFIRMED) throw new RuntimeException("Status should be CONFIRMED");
    }
    private static void testFindReservationById() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        if (resm.getReservation(res.getReservationId()) == null) throw new RuntimeException("Find by ID failed");
    }
    private static void testOverlappingBookingRejection() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 1);
        try { resm.createReservation(guest, room, LocalDate.of(2026, 10, 3), LocalDate.of(2026, 10, 6), 1); throw new RuntimeException("Overlap allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testNonOverlappingBookingAllowed() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 1);
        Reservation res2 = resm.createReservation(guest, room, LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 10), 1);
        if (res2 == null) throw new RuntimeException("Non-overlapping rejected");
    }
    private static void testGuestCapacityValidation() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        try { resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 3); throw new RuntimeException("Capacity exceeded allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testEmptyGuestNameRejection() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        try { resm.createReservation(null, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1); throw new RuntimeException("Null guest allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testInvalidDateRangeRejection() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        try { resm.createReservation(guest, room, LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 1), 1); throw new RuntimeException("Invalid date range allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testPaymentUPI() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        Payment p = pm.processPayment(res, PaymentMethod.UPI);
        if (p == null || p.getPaymentStatus() != PaymentStatus.PAID) throw new RuntimeException("UPI payment failed");
    }
    private static void testPaymentCard() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        Payment p = pm.processPayment(res, PaymentMethod.CARD);
        if (p == null || p.getPaymentStatus() != PaymentStatus.PAID) throw new RuntimeException("Card payment failed");
    }
    private static void testPaymentCash() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        Payment p = pm.processPayment(res, PaymentMethod.CASH);
        if (p == null || p.getPaymentStatus() != PaymentStatus.PAID) throw new RuntimeException("Cash payment failed");
    }
    private static void testPaymentTxIdGeneration() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res1 = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        Reservation res2 = resm.createReservation(guest, room, LocalDate.of(2026, 10, 3), LocalDate.of(2026, 10, 4), 1);
        Payment p1 = pm.processPayment(res1, PaymentMethod.UPI);
        Payment p2 = pm.processPayment(res2, PaymentMethod.UPI);
        if (!p1.getTransactionId().startsWith("PAY") || p1.getTransactionId().equals(p2.getTransactionId())) {
            throw new RuntimeException("TXID generation failed");
        }
    }
    private static void testPaymentAmountMatch() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 4), 1); // 3 nights = 6000
        Payment p = pm.processPayment(res, PaymentMethod.UPI);
        if (p.getAmount() != 6000.0) throw new RuntimeException("Payment amount mismatch");
    }
    private static void testPaymentStatusTransition() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        if (res.getPayment() != null) throw new RuntimeException("Payment should be null initially");
        pm.processPayment(res, PaymentMethod.UPI);
        if (res.getPayment() == null || res.getPayment().getPaymentStatus() != PaymentStatus.PAID) {
            throw new RuntimeException("Payment status transition failed");
        }
    }
    private static void testPaymentMethodStored() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        pm.processPayment(res, PaymentMethod.CARD);
        if (res.getPayment().getPaymentMethod() != PaymentMethod.CARD) throw new RuntimeException("Payment method not stored");
    }
    private static void testPaymentRetrieval() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        Payment p = pm.processPayment(res, PaymentMethod.UPI);
        if (pm.findPaymentByTransactionId(p.getTransactionId()) == null) throw new RuntimeException("Payment retrieval failed");
    }
    private static void testNegativePaymentRejected() {
        PaymentManager pm = new PaymentManager();
        Room room = new Room("101", RoomCategory.STANDARD, -100.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = new Reservation("R1", guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1, -200.0, ReservationStatus.CONFIRMED, null);
        try { pm.processPayment(res, PaymentMethod.UPI); throw new RuntimeException("Negative payment allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testMissingPaymentMethodRejected() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        try { pm.processPayment(res, null); throw new RuntimeException("Null payment method allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testDuplicatePaymentRejected() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        pm.processPayment(res, PaymentMethod.UPI);
        try { pm.processPayment(res, PaymentMethod.UPI); throw new RuntimeException("Duplicate payment allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testCancelledPaymentRejected() {
        PaymentManager pm = new PaymentManager();
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        res.setStatus(ReservationStatus.CANCELLED);
        try { pm.processPayment(res, PaymentMethod.UPI); throw new RuntimeException("Cancelled payment allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testCancelConfirmedReservation() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        String id = res.getReservationId();
        resm.cancelReservation(id);
        if (resm.getReservation(id).getStatus() != ReservationStatus.CANCELLED) throw new RuntimeException("Status not updated to CANCELLED");
    }
    private static void testCancelStatusUpdate() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        resm.cancelReservation(res.getReservationId());
        if (res.getStatus() != ReservationStatus.CANCELLED) throw new RuntimeException("Reservation object status not updated");
    }
    private static void testCancelledReservationPersistence() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        resm.cancelReservation(res.getReservationId());
        if (resm.getAllReservations().size() != 1) throw new RuntimeException("Cancelled reservation removed from list");
    }
    private static void testDuplicateCancellationRejection() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        resm.cancelReservation(res.getReservationId());
        try { resm.cancelReservation(res.getReservationId()); throw new RuntimeException("Duplicate cancellation allowed"); } catch (IllegalArgumentException e) {}
    }
    private static void testNonExistingCancellationRejection() {
        ReservationManager resm = new ReservationManager();
        try { resm.cancelReservation("NONEXISTENT"); throw new RuntimeException("Non-existing reservation cancelled"); } catch (IllegalArgumentException e) {}
    }
    private static void testCancelledRoomAvailability() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 1);
        if (resm.isRoomAvailable(room, LocalDate.of(2026, 10, 2), LocalDate.of(2026, 10, 3))) throw new RuntimeException("Room should be unavailable");
        resm.cancelReservation(res.getReservationId());
        if (!resm.isRoomAvailable(room, LocalDate.of(2026, 10, 2), LocalDate.of(2026, 10, 3))) throw new RuntimeException("Room should be available after cancellation");
    }
    private static void testRebookingAfterCancellation() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest1 = new Guest("G1", "S1", "1234567890", "s1@e.com");
        Guest guest2 = new Guest("G2", "S2", "0987654321", "s2@e.com");
        Reservation res1 = resm.createReservation(guest1, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 1);
        resm.cancelReservation(res1.getReservationId());
        Reservation res2 = resm.createReservation(guest2, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 1);
        if (res2 == null) throw new RuntimeException("Rebooking after cancellation failed");
    }
    private static void testPaidReservationCancellation() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        Payment p = new Payment("PAY1", res.getTotalAmount(), PaymentMethod.UPI, PaymentStatus.PAID);
        res.setPayment(p);
        resm.cancelReservation(res.getReservationId());
        if (res.getStatus() != ReservationStatus.CANCELLED) throw new RuntimeException("Paid reservation not cancelled");
        if (res.getPayment() == null || res.getPayment().getPaymentStatus() != PaymentStatus.PAID) throw new RuntimeException("Payment info lost after cancellation");
    }
    private static void testCancellationPreservesData() {
        ReservationManager resm = new ReservationManager();
        Room room = new Room("101", RoomCategory.STANDARD, 2000.0, 2, true);
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);
        String id = res.getReservationId();
        resm.cancelReservation(id);
        Reservation cancelled = resm.getReservation(id);
        if (!cancelled.getGuest().getName().equals("S")) throw new RuntimeException("Guest name lost");
        if (!cancelled.getRoom().getRoomId().equals("101")) throw new RuntimeException("Room lost");
        if (!cancelled.getCheckInDate().equals(LocalDate.of(2026, 10, 1))) throw new RuntimeException("Check-in date lost");
        if (cancelled.getTotalAmount() != 2000.0) throw new RuntimeException("Total amount lost");
    }

    // --- Phase 7 Tests ---
    private static void testSaveAndLoadBasic() throws IOException, ClassNotFoundException {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        ReservationManager resm = new ReservationManager();
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Room room = rm.getRoom("101");
        Reservation res = resm.createReservation(guest, room, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);

        ApplicationData data = new ApplicationData(rm.getAllRooms(), resm.getAllReservations(), List.of(guest), new ArrayList<>(), resm.getReservationCounter(), 1000);
        FileManager.save(data);
        ApplicationData loaded = FileManager.load();
        if (loaded == null || loaded.getRooms().size() != 1 || loaded.getReservations().size() != 1) throw new RuntimeException("Save/Load failed");
    }
    private static void testPersistenceIntegrity() throws IOException, ClassNotFoundException {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        ReservationManager resm = new ReservationManager();
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, rm.getRoom("101"), LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2), 1);

        ApplicationData data = new ApplicationData(rm.getAllRooms(), resm.getAllReservations(), List.of(guest), new ArrayList<>(), resm.getReservationCounter(), 1000);
        FileManager.save(data);
        ApplicationData loaded = FileManager.load();
        Reservation loadedRes = loaded.getReservations().get(0);
        if (!loadedRes.getReservationId().equals(res.getReservationId())) throw new RuntimeException("ID changed after load");
        if (!loadedRes.getGuest().getName().equals("S")) throw new RuntimeException("Guest name changed after load");
    }
    private static void testMissingFileHandling() throws IOException, ClassNotFoundException {
        new File("data/hotel_data.dat").delete();
        if (FileManager.load() != null) throw new RuntimeException("Should return null for missing file");
    }
    private static void testIdCollisionPrevention() throws IOException, ClassNotFoundException {
        ReservationManager resm = new ReservationManager();
        resm.setReservationCounter(1005);
        if (!resm.generateReservationId().equals("BK1006")) throw new RuntimeException("ID counter not respected");
    }
    private static void testPersistenceAvailability() throws IOException, ClassNotFoundException {
        RoomManager rm = new RoomManager();
        rm.addRoom(new Room("101", RoomCategory.STANDARD, 2000.0, 2, true));
        ReservationManager resm = new ReservationManager();
        Guest guest = new Guest("G1", "S", "1234567890", "s@e.com");
        Reservation res = resm.createReservation(guest, rm.getRoom("101"), LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 1);

        ApplicationData data = new ApplicationData(rm.getAllRooms(), resm.getAllReservations(), List.of(guest), new ArrayList<>(), resm.getReservationCounter(), 1000);
        FileManager.save(data);

        ApplicationData loaded = FileManager.load();
        ReservationManager loadedResm = new ReservationManager();
        for(Reservation r : loaded.getReservations()) loadedResm.addReservation(r);

        if (loadedResm.isRoomAvailable(rm.getRoom("101"), LocalDate.of(2026, 10, 2), LocalDate.of(2026, 10, 3))) {
            throw new RuntimeException("Room should still be unavailable after load");
        }
    }
}
