package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BankTransferPayment extends Payment{

    private int paymentID;             // Corresponds to Payment.paymentID
    private String bankAccountNumber;  // Bank account number used for the transfer
    private String bankName;           // Name of the bank
    private String referenceCode;      // Reference code or transaction ID for the bank transfer
    private java.sql.Timestamp transferDate; // Date and time of the transfer

    public BankTransferPayment(int paymentID, int bookingID, BigDecimal amount, String paymentMethod,
			String paymentStatus, int paymentID2, String bankAccountNumber, String bankName, String referenceCode,
			Timestamp transferDate) {
		super(paymentID, bookingID, amount, paymentMethod, paymentStatus);
		paymentID = paymentID2;
		this.bankAccountNumber = bankAccountNumber;
		this.bankName = bankName;
		this.referenceCode = referenceCode;
		this.transferDate = transferDate;
	}

	// Getters and setters
    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public java.sql.Timestamp getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(java.sql.Timestamp transferDate) {
        this.transferDate = transferDate;
    }

    @Override
    public String toString() {
        return "BankTransferPayment{" +
                "paymentID=" + paymentID +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", referenceCode='" + referenceCode + '\'' +
                ", transferDate=" + transferDate +
                '}';
    }
}

