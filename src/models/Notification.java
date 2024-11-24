package models;

import java.sql.Date;

public class Notification {
    private int notificationID;
    private int recipientID;  // User ID of the recipient (buyer or seller)
    private String notificationMessage;  // The notification message
    private String status;  // Status (Unread, Read)
    private java.sql.Timestamp timestamp;  // The timestamp when the notification is created
    private String type;  // Type (BookingConfirmation, PaymentStatus, FeedbackReceived)
    private String recipientRole;  // Role of the recipient (Buyer or Seller)
    
    // Constructor
    public Notification(int notificationID, int recipientID, String notificationMessage, 
    		java.sql.Timestamp timestamp, String status, String type, String recipientRole) {
        this.notificationID = notificationID;
        this.recipientID = recipientID;
        this.notificationMessage = notificationMessage;
        this.timestamp = timestamp;
        this.status = status;
        this.type = type;
        this.recipientRole = recipientRole;
    }

	// Getters and Setters
	public int getNotificationID() {
		return notificationID;
	}

	public void setNotificationID(int notificationID) {
		this.notificationID = notificationID;
	}

	public int getRecipientID() {
		return recipientID;
	}

	public void setRecipientID(int recipientID) {
		this.recipientID = recipientID;
	}

	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRecipientRole() {
		return recipientRole;
	}

	public void setRecipientRole(String recipientRole) {
		this.recipientRole = recipientRole;
	}
}
