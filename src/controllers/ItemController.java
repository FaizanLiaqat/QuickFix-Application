package controllers;

import java.io.File;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Booking;
import models.Notification;
import models.Service;
import java.text.SimpleDateFormat;
import java.util.Optional;

import dao.BookingDAO;
import javafx.scene.control.ButtonType;


public class ItemController {

	@FXML
	private Label DescriptionLabel;

	@FXML
	private Label PriceLabel;

	@FXML
	private Label Ratinglabel;

	@FXML
	private Label SellernameLabel;

	@FXML
	private ImageView img;

	@FXML
	private Label nameLabel;

	public void setData(Service services) {
		// Set text for various labels
		nameLabel.setText(services.getServiceName());
		DescriptionLabel.setText(services.getServiceDescription());
		Ratinglabel.setText("Service Rating: " + services.getServiceRating());
		PriceLabel.setText(services.getServicePrice() + " PKR");
		SellernameLabel.setText(services.getsellerName());

		// Image loading logic
		String imagePath;
		if (services.getServiceName().equals("Mechanic")) {
			imagePath = "file:/QuickFixv2/src/media/mechanic_logo.png";
		} else if (services.getServiceName().equals("Plumber")) {
			imagePath = "file:/QuickFixv2/src/media/Plumber_logo.png";
		} else if (services.getServiceName().equals("Carpenter")) {
			imagePath = "file:/QuickFixv2/src/media/Carpenter_logo.png";
		} else {
			imagePath = "file:/QuickFixv2/src/media/Electrician_logo.jpg";
		}

		File imageFile = new File(imagePath);

		// Check if the image file exists
		if (!imageFile.exists()) {
			System.out.println("Image file does not exist at path: " + imagePath);
		} else {
			try {
				Image image = new Image(imagePath);
				img.setImage(image);
			} catch (Exception e) {
				System.out.println("Error loading image: " + e.getMessage());
			}
		}
	}

	public void setData(Booking booking) {
	    // Set text for various labels
	    nameLabel.setText(booking.getServicename());
	    DescriptionLabel.setText(booking.getSellername());

	    // Define date and time formatters
	    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy"); // Customize as needed
	    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm"); // Customize as needed

	    // Convert booking date to a formatted string
	    String formattedDate = dateFormatter.format(booking.getBookingDate());
	    Ratinglabel.setText(formattedDate); // Set formatted date to the label

	    // Convert preferred time to a formatted string
	    String formattedTime = timeFormatter.format(booking.getPreferredTime());
	    PriceLabel.setText(formattedTime); // Set formatted time to the label

	    SellernameLabel.setText(booking.getPaymentStatus());

	    
	    // Image loading logic
	    String imagePath;

	    imagePath = "file:/QuickFixv2/src/media/Electrician_logo.jpg";

	    File imageFile = new File(imagePath);

	    // Check if the image file exists
	    if (!imageFile.exists()) {
	        System.out.println("Image file does not exist at path: " + imagePath);
	    } else {
	        try {
	            Image image = new Image(imagePath);
	            img.setImage(image);
	        } catch (Exception e) {
	            System.out.println("Error loading image: " + e.getMessage());
	        }
	    }
	}
	
	
	public void setData(Notification notification) {
	    // Set text for various labels
	    nameLabel.setText(notification.getRecipientRole());
	    DescriptionLabel.setText(notification.getNotificationMessage());

	    // Define date and time formatters
	    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy"); // Customize as needed
	    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm"); // Customize as needed

	    // Convert booking date to a formatted string
	    //String formattedDate = dateFormatter.format(notification.get);
	    //Ratinglabel.setText(formattedDate); // Set formatted date to the label

	    // Convert preferred time to a formatted string
	    //String formattedTime = timeFormatter.format(notification.getPreferredTime());
	    //PriceLabel.setText(formattedTime); // Set formatted time to the label

	    //SellernameLabel.setText(notification.getPaymentStatus());

	    img.setImage(null); // Set the image to null, effectively hiding it
	}

}