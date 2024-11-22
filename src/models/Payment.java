package models;

import java.math.BigDecimal;


public abstract class Payment {

    private int paymentID;              // Unique payment ID
    private int bookingID;              // Associated booking ID
    private BigDecimal amount;         // Payment amount
    private String paymentMethod;      // Payment method ('CreditCard' or 'BankTransfer')
    private String paymentStatus;      // Payment status ('Pending', 'Completed', 'Failed')

    public Payment(int paymentID, int bookingID, BigDecimal amount, String paymentMethod, String paymentStatus) {
		super();
		this.paymentID = paymentID;
		this.bookingID = bookingID;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
	}

	// Getters and setters
    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + paymentID +
                ", bookingID=" + bookingID +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
