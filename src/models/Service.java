package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

public class Service {
    private int serviceID;
    private String serviceName;
    private String serviceDescription;
    private double servicePrice;
    private int serviceIncrement;
    private int serviceRating;
    private int serviceProviderID;

    // Constructor
    public Service(int serviceID, String serviceName, String serviceDescription, double servicePrice, int serviceIncrement, int serviceRating) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.servicePrice = servicePrice;
        this.serviceIncrement = serviceIncrement;
        this.serviceRating = serviceRating;
        
    }

    // Getters and Setters
    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public int getServiceIncrement() {
        return serviceIncrement;
    }

    public void setServiceIncrement(int serviceIncrement) {
        this.serviceIncrement = serviceIncrement;
    }

    public int getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(int serviceRating) {
        this.serviceRating = serviceRating;
    }

    public int getServiceProviderID() {
		return serviceProviderID;
	}

	public void setServiceProviderID(int serviceProviderID) {
		this.serviceProviderID = serviceProviderID;
	}


    // Method to update service rating
    public void updateRating(int newRating) {
        this.serviceRating = newRating;
    }

    // Method to increment the number of bookings
    public void incrementServiceBooking() {
        this.serviceIncrement++;
    }

}
