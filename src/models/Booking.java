package models;

import java.util.Date;

public class Booking {
    private int bookingID;
    private int clientID;  // User ID of the client
    private int serviceProviderID;  // User ID of the service provider
    private int serviceID;  // ID of the service booked
    private Date bookingDate;  // Date when the booking was created
    private Date preferredTime;  // Preferred time for the service
    private String bookingStatus;  // Status of the booking (Pending, Confirmed, Cancelled, Completed)
    private String paymentStatus;  // Status of payment (Paid, Unpaid)
    private String Sellername;
    private String Servicename;  

    // Constructor
    public Booking(int bookingID, int clientID, int serviceProviderID, int serviceID,
                   Date bookingDate, Date preferredTime, String status, String paymentStatus ,String Sellername,String Servicename ) {
        this.bookingID = bookingID;
        this.clientID = clientID;
        this.serviceProviderID = serviceProviderID;
        this.serviceID = serviceID;
        this.bookingDate = bookingDate;
        this.preferredTime = preferredTime;
        this.bookingStatus = status;
        this.paymentStatus = paymentStatus;
        this.Sellername = Sellername;
        this.Servicename = Servicename;
    }
    
    public Booking()
    {
    	
    }
    // Constructor
    public Booking(int bookingID, int clientID, int serviceProviderID, int serviceID,
                   Date bookingDate, Date preferredTime, String status, String paymentStatus  ) {
        this.bookingID = bookingID;
        this.clientID = clientID;
        this.serviceProviderID = serviceProviderID;
        this.serviceID = serviceID;
        this.bookingDate = bookingDate;
        this.preferredTime = preferredTime;
        this.bookingStatus = status;
        this.paymentStatus = paymentStatus;

    }

    public String getSellername() {
		return Sellername;
	}

	public void setSellername(String sellername) {
		Sellername = sellername;
	}

	public String getServicename() {
		return Servicename;
	}

	public void setServicename(String servicename) {
		Servicename = servicename;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	// Getters and Setters
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
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

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Date getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(Date preferredTime) {
        this.preferredTime = preferredTime;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setStatus(String status) {
        this.bookingStatus = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}