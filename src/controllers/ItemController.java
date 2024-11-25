package controllers;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Booking;
import models.Dispute;
import models.Notification;
import models.Service;
import models.User;

import java.text.SimpleDateFormat;
import java.time.Instant;
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

	@FXML
	private Button MessageButton;

	private Service currentService;

	private int clientid;

	public void setData(Service services, User user) {

		this.clientid = user.getUserID();
		this.currentService = services;
		// Set text for various labels
		nameLabel.setText(services.getServiceName());
		DescriptionLabel.setText(services.getServiceDescription());
		Ratinglabel.setText("Service Rating: " + services.getServiceRating());
		PriceLabel.setText(services.getServicePrice() + " PKR");
		SellernameLabel.setText(services.getsellerName());
		if (user.getUserType() == "Buyer") {
			MessageButton.setText("Book Me!");
		} else {
			MessageButton.setText("Remove");
		}

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

	public void BookMe() {

		if ("Book Me!".equals(MessageButton.getText()))

		{
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Booking Confirmation");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to book it?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				System.out.println("Booking confirmed for: " + currentService.getServiceName());
				BookingDAO bdao = new BookingDAO(); // Initialize the ServiceDAO object

				Booking booking = new Booking();
				booking.setClientID(clientid);
				booking.setServiceID(currentService.getServiceID());
				booking.setServiceProviderID(currentService.getServiceProviderID());
				booking.setBookingDate(Timestamp.from(Instant.now())); // Current date and time
				booking.setPreferredTime(Timestamp.from(Instant.now())); // Current date and time
				try {
					// Call the filterByLocation method and pass the location as an argument
					bdao.insert(booking);
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Booking canceled for: " + currentService.getServiceName());
			}
		} else if ("Click to confirm".equals(MessageButton.getText())) {
			System.out.println("it is confirmed");
		} else if ("Review and Pay".equals(MessageButton.getText())) {
			System.out.println("it is confirmed");
		} else if ("Remove".equals(MessageButton.getText())) {
			System.out.println("it is confirmed");
		} else {
			System.out.println("Button issue!");
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

		String paymentStatus = booking.getPaymentStatus();
		String status = booking.getBookingStatus();

		// Handle different combinations of payment and booking status
		if ("Pending".equals(status)) {
			// Booking is pending
			MessageButton.setText("Pending");
			MessageButton.setDisable(true); // Disable the button to prevent interaction
		} else if ("Confirmed".equals(status)) {

			MessageButton.setText("Seller Confirmed");
			MessageButton.setDisable(true); // Enable the button for payment action
		} else if ("Completed".equals(status)) {
			// Booking is confirmed
			if ("Unpaid".equals(paymentStatus)) {
				// If payment is still unpaid, let the user know to pay
				MessageButton.setText("Review and Pay");
				MessageButton.setDisable(false); // Enable the button for payment action
			} else {
				// If payment is done, allow user to take action or confirm
				MessageButton.setText("Paid");
				MessageButton.setDisable(true); // Disable the button because no further action is needed
			}
		} else if ("Canceled".equals(status)) {
			// Booking is canceled
			MessageButton.setText("Booking Canceled");
			MessageButton.setDisable(true); // Disable the button as no further action is needed
		} else {
			System.out.println("Button issue");
		}

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

		MessageButton.setText("Click to confirm");

		// Setting text for various labels
		nameLabel.setText(notification.getRecipientRole()); // Sets recipient role
		DescriptionLabel.setText(notification.getNotificationMessage()); // Sets notification message

		// Define date and time formatters
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy"); // Date format
		SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm"); // Time format

		// Convert timestamp to formatted date
		if (notification.getTimestamp() != null) {
			String formattedDate = dateFormatter.format(notification.getTimestamp());
			Ratinglabel.setText(formattedDate); // Set formatted date
		} else {
			Ratinglabel.setText("No Date Available"); // Handle null case
		}

		// Convert timestamp to formatted time
		if (notification.getTimestamp() != null) {
			String formattedTime = timeFormatter.format(notification.getTimestamp());
			PriceLabel.setText(formattedTime); // Set formatted time
		} else {
			PriceLabel.setText("No Time Available"); // Handle null case
		}

		// Optionally set another label (e.g., payment status)
		SellernameLabel.setText("Payment Pending"); // Replace with the actual status or logic

		img.setImage(null); // Hide image
	}

	
}