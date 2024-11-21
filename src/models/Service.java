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
    private Button bookButton; /// for BookSevice Table
    private Button removeButton; // For ViewService Table

    // Constructor
    public Service(int serviceID, String serviceName, String serviceDescription, double servicePrice, int serviceIncrement, int serviceRating) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.servicePrice = servicePrice;
        this.serviceIncrement = serviceIncrement;
        this.serviceRating = serviceRating;
        this.bookButton = new Button("Book it");
        this.removeButton = new Button("Remove it");
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

    public Button getBookButton() {
        return bookButton;
    }

    public void setBookButton(Button bookButton) {
        this.bookButton = bookButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public void setRemoveButton(Button removeButton) {
        this.removeButton = removeButton;
    }

    // Method to update service rating
    public void updateRating(int newRating) {
        this.serviceRating = newRating;
    }

    // Method to increment the number of bookings
    public void incrementServiceBooking() {
        this.serviceIncrement++;
    }

    // Method to get the list of services (ObservableList)
    public static ObservableList<Service> getSampleServices() {
        return FXCollections.observableArrayList(
            new Service(1, "Cleaning", "Home Cleaning Service", 100.0, 0, 4),
            new Service(2, "Plumbing", "Pipe Repair Service", 150.0, 0, 5),
            new Service(3, "Gardening", "Lawn Maintenance Service", 80.0, 0, 3)
        );
    }
}
