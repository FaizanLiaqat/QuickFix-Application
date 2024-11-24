package models;

import java.sql.Date;

/**
 * Represents a dispute between a buyer and a seller related to a booking.
 * The dispute can have various statuses like "Open", "Resolved", or "Rejected".
 */
public class Dispute {

    private int disputeID;         // Unique identifier for the dispute
    private int bookingID;         // Associated booking ID
    private int buyerID;           // Buyer who initiated the dispute
    private int sellerID;          // Seller involved in the dispute
    private String disputeReason;  // The reason for the dispute
    private String disputeStatus;  // Status of the dispute: Open, Resolved, or Rejected
    private String resolutionDetails; // Details on how the dispute was resolved
    private java.sql.Timestamp createdAt; // Timestamp of when the dispute was created
    private java.sql.Timestamp resolvedAt; // Timestamp of when the dispute was resolved (nullable)

    
    public Dispute(int disputeID,int bookingID, int buyerID, int sellerID, String disputeReason,
                   String disputeStatus, String resolutionDetails, java.sql.Timestamp createdAt, 
                   java.sql.Timestamp resolvedAt) {
        
        // Validate inputs
        if (disputeReason == null || disputeReason.trim().isEmpty()) {
            throw new IllegalArgumentException("Dispute reason cannot be null or empty.");
        }
        
        if (!"Open".equals(disputeStatus) && !"Resolved".equals(disputeStatus) && !"Rejected".equals(disputeStatus)) {
            throw new IllegalArgumentException("Dispute status must be 'Open', 'Resolved', or 'Rejected'.");
        }
        this.disputeID = disputeID;
        this.bookingID = bookingID;
        this.buyerID = buyerID;
        this.sellerID = sellerID;
        this.disputeReason = disputeReason;
        this.disputeStatus = disputeStatus;
        this.resolutionDetails = resolutionDetails;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
    }

    // Getters and Setters
    public int getDisputeID() {
        return disputeID;
    }

    public void setDisputeID(int disputeID) {
        this.disputeID = disputeID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(int buyerID) {
        this.buyerID = buyerID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getDisputeReason() {
        return disputeReason;
    }

    public void setDisputeReason(String disputeReason) {
        if (disputeReason == null || disputeReason.trim().isEmpty()) {
            throw new IllegalArgumentException("Dispute reason cannot be null or empty.");
        }
        this.disputeReason = disputeReason;
    }

    public String getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(String disputeStatus) {
        if (!"Open".equals(disputeStatus) && !"Resolved".equals(disputeStatus) && !"Rejected".equals(disputeStatus)) {
            throw new IllegalArgumentException("Dispute status must be 'Open', 'Resolved', or 'Rejected'.");
        }
        this.disputeStatus = disputeStatus;
    }

    public String getResolutionDetails() {
        return resolutionDetails;
    }

    public void setResolutionDetails(String resolutionDetails) {
        this.resolutionDetails = resolutionDetails;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public java.sql.Timestamp getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(java.sql.Timestamp resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

   
    public static Date convertToSqlDate(java.time.LocalDateTime localDateTime) {
        return Date.valueOf(localDateTime.toLocalDate());
    }

    /**
     * Overrides the toString method to provide a readable format of the dispute details.
     * 
     * @return a string representation of the dispute details
     */
    @Override
    public String toString() {
        return "Dispute{" +
                "disputeID=" + disputeID +
                ", bookingID=" + bookingID +
                ", buyerID=" + buyerID +
                ", sellerID=" + sellerID +
                ", disputeReason='" + disputeReason + '\'' +
                ", disputeStatus='" + disputeStatus + '\'' +
                ", resolutionDetails='" + resolutionDetails + '\'' +
                ", createdAt=" + createdAt +
                ", resolvedAt=" + resolvedAt +
                '}';
    }
}

