package models;


import java.math.BigDecimal;
import java.sql.Date;

import strategies.CreditCardPaymentStrategy;

public class CreditCardPayment extends Payment {
    private String cardNumber;
    private String cardType;
    private String cardHolderName;
    private java.sql.Timestamp expirationDate;

    // Constructor
    public CreditCardPayment(int paymentID, int bookingID, BigDecimal amount, String paymentMethod,  java.sql.Timestamp transactionDate, int payerID, int receiverID, String cardNumber, String cardType, String cardHolderName, java.sql.Timestamp expirationDate) {

        super(paymentID, bookingID, amount, paymentMethod, transactionDate, payerID, receiverID,new CreditCardPaymentStrategy());

        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
    }
    
    public CreditCardPayment(int bookingID, BigDecimal amount, String paymentMethod, java.sql.Timestamp transactionDate, int payerID, int receiverID, String cardNumber, String cardType, String cardHolderName, java.sql.Timestamp expirationDate) {

        super(bookingID, amount, paymentMethod, transactionDate, payerID, receiverID,new CreditCardPaymentStrategy());

        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
    }

   
    // Getters and Setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    public java.sql.Timestamp getExpirationDate() { return expirationDate; }
    public void setExpirationDate(java.sql.Timestamp expirationDate) { this.expirationDate = expirationDate; }

    @Override
    public void processPayment() {
        // Implement the logic for processing credit card payments
        System.out.println("Processing Credit Card Payment for amount: " + getAmount() + " using card: " + cardType);
        //setPaymentStatus("Completed");
        // Add payment gateway interaction logic here
    }
}