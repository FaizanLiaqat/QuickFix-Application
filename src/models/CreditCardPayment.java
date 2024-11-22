package models;

import java.math.BigDecimal;
import java.sql.Date;

public class CreditCardPayment extends Payment{

    private int paymentID;             // Corresponds to Payment.paymentID
    private String cardNumber;         // Encrypted card number
    private String cardType;           // Card type ('Visa', 'MasterCard', 'American Express')
    private String cardHolderName;     // Name of the cardholder
    private java.sql.Date expirationDate; // Expiration date of the card

    

	public CreditCardPayment(int paymentID, int bookingID, BigDecimal amount, String paymentMethod,
			String paymentStatus, int paymentID2, String cardNumber, String cardType, String cardHolderName,
			Date expirationDate) {
		super(paymentID, bookingID, amount, paymentMethod, paymentStatus);
		paymentID = paymentID2;
		this.cardNumber = cardNumber;
		this.cardType = cardType;
		this.cardHolderName = cardHolderName;
		this.expirationDate = expirationDate;
	}

	// Getters and setters
    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public java.sql.Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(java.sql.Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "CreditCardPayment{" +
                "paymentID=" + paymentID +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardType='" + cardType + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", expirationDate=" + expirationDate + '\'' +
                '}';
    }
}
