package models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class BankTransferPayment extends Payment {
    private String bankAccountNumber;
    private String bankName;
    private String referenceCode;
    private Date transferDate;

    // Constructor
    public BankTransferPayment(int paymentID, int bookingID, BigDecimal amount, String paymentMethod, String paymentStatus, Date transactionDate, int payerID, int receiverID, String bankAccountNumber, String bankName, String referenceCode, Date transferDate) {
        super(paymentID, bookingID, amount, paymentMethod, paymentStatus, transactionDate, payerID, receiverID);
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
        this.referenceCode = referenceCode;
        this.transferDate = transferDate;
    }

    // Getters and Setters
    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getReferenceCode() { return referenceCode; }
    public void setReferenceCode(String referenceCode) { this.referenceCode = referenceCode; }

    public Date getTransferDate() { return transferDate; }
    public void setTransferDate(Date transferDate) { this.transferDate = transferDate; }

    @Override
    public void processPayment() {
        // Implement the logic for processing bank transfer payments
        System.out.println("Processing Bank Transfer Payment for amount: " + getAmount() + " via bank: " + bankName);
        setPaymentStatus("Completed");
        // Add bank transfer validation and logic here
    }
}


