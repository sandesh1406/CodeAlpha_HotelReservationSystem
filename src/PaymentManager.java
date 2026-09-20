package src;
import java.util.*;

public class PaymentManager {
    private Map<String, Payment> payments;
    private int transactionCounter = 1000;

    public PaymentManager() {
        this.payments = new HashMap<>();
    }

    public void setTransactionCounter(int counter) {
        this.transactionCounter = counter;
    }

    public int getTransactionCounter() {
        return transactionCounter;
    }

    public void addPayment(Payment payment) {
        if (payment == null) throw new IllegalArgumentException("Payment cannot be null");
        payments.put(payment.getTransactionId(), payment);
    }

    public String generateTransactionId() {
        transactionCounter++;
        return "PAY" + transactionCounter;
    }

    public Payment processPayment(Reservation reservation, PaymentMethod method) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation must exist.");
        }
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot pay for a cancelled reservation.");
        }
        if (method == null) {
            throw new IllegalArgumentException("Please select a payment method.");
        }
        if (reservation.getPayment() != null && reservation.getPayment().getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalArgumentException("Payment has already been completed for this reservation.");
        }
        if (reservation.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Invalid payment amount.");
        }

        String txId = generateTransactionId();
        Payment payment = new Payment(
                txId,
                reservation.getTotalAmount(),
                method,
                PaymentStatus.PAID
        );

        payments.put(txId, payment);
        reservation.setPayment(payment);

        return payment;
    }

    public Payment findPaymentByTransactionId(String transactionId) {
        return payments.get(transactionId);
    }

    public boolean isPaymentCompleted(Reservation reservation) {
        return reservation.getPayment() != null &&
               reservation.getPayment().getPaymentStatus() == PaymentStatus.PAID;
    }

    public int getPaidReservationsCount() {
        return (int) payments.values().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.PAID)
                .count();
    }
}
