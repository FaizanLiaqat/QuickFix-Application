package models;

import java.math.BigDecimal;
import java.sql.Date;


public abstract class Payment {
    private int paymentID;
    private int bookingID;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private Date transactionDate;
    private int payerID;
    private int receiverID;

    // Constructor
    public Payment(int paymentID, int bookingID, BigDecimal amount, String paymentMethod, String paymentStatus, Date transactionDate, int payerID, int receiverID) {
        this.paymentID = paymentID;
        this.bookingID = bookingID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionDate = transactionDate;
        this.payerID = payerID;
        this.receiverID = receiverID;
    }

    // Getters and Setters
    public int getPaymentID() { return paymentID; }
    public void setPaymentID(int paymentID) { this.paymentID = paymentID; }

    public int getBookingID() { return bookingID; }
    public void setBookingID(int bookingID) { this.bookingID = bookingID; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public int getPayerID() { return payerID; }
    public void setPayerID(int payerID) { this.payerID = payerID; }

    public int getReceiverID() { return receiverID; }
    public void setReceiverID(int receiverID) { this.receiverID = receiverID; }

    // Abstract method to be implemented by child classes
    public abstract void processPayment();
}
