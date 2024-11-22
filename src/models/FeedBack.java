package models;

import java.sql.Date;

public class FeedBack {
    private int feedbackID;
    private int clientID;
    private int serviceProviderID;
    private int bookingID;
    private int serviceID;
    private int rating;
    private String comments;
    private Date feedbackDate;

    // Constructor
    public FeedBack(int feedbackID, int clientID, int serviceProviderID, int bookingID,int serviceID, int rating, String comments, java.sql.Date feedbackDate) {
        this.feedbackID = feedbackID;
        this.clientID = clientID;
        this.serviceProviderID = serviceProviderID;
        this.bookingID = bookingID;
        this.rating = rating;
        this.comments = comments;
        this.feedbackDate = feedbackDate;
    }

    // Getters and Setters
    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getServiceProviderID() {
        return serviceProviderID;
    }

    public void setServiceProviderID(int serviceProviderID) {
        this.serviceProviderID = serviceProviderID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public java.sql.Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(java.sql.Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

	public int getServiceID() {
		return serviceID;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}
    
}

