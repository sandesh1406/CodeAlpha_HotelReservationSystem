package src;
import java.io.Serializable;

public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String transactionId;
    private double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    public Payment(String transactionId, double amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    @Override
    public String toString() {
        return "Payment{" + "id='" + transactionId + '\'' + ", amount=" + amount + ", status=" + paymentStatus + '}';
    }
}
