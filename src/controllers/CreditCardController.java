package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Booking;
import models.CreditCardPayment;
import models.Notification;
import models.Payment;
import models.Service;
import utils.AlertUtils;

public class CreditCardController {
	@FXML
	private Button pay_button;
	
	@FXML
	private Button back_button;
	
	@FXML
	private TextField cardNumber;
	
	@FXML
	private TextField cardType;
	
	@FXML
	private TextField nameOnCard;
	
	@FXML
	private TextField expDate;
	
	private String cardnumber;
	
	private String cardtype;
	
	private String nameoncard;
	
	private String expdate;



	private int bookingId;
	
	public void setBookingId(int id) {
		this.bookingId = id;
	}

	
	public void goBackOnAction(ActionEvent event) {
		try {

			// Close the current window (home.fxml)
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
            String fxmlFile = "/views/buyer_dashboard.fxml";
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

			// Create a new Stage (window) for Access.fxml
			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED); // Make the window undecorated (no borders or title bar)
			stage.setTitle("Home Window"); // Set the title of the new window

			// Set the new scene with the loaded FXML and desired size
			Scene scene = new Scene(root, 810, 620); // Set dimensions similar to your original configuration
			stage.setScene(scene);

			// Show the new window (stage)
			stage.show();

		} catch (IOException e) {
			e.printStackTrace(); // Handle error if FXML file loading fails
		}
	}
	
	public void actionOnPay(ActionEvent event) {
		cardnumber = cardNumber.getText();
		cardtype = cardType.getText();
		nameoncard = nameOnCard.getText();
		expdate = expDate.getText();
		
		if (cardNumber.getText().isEmpty() || cardType.getText().isEmpty() || 
		        nameOnCard.getText().isEmpty() || expDate.getText().isEmpty()) {
		        AlertUtils.showError("Empty Field", "All fields are required");
		        return;
		    }
		
		 if (!(cardtype.equals("MasterCard") || 
				 cardtype.equals("Visa") || 
				 cardtype.equals("American Express"))) {
			 	AlertUtils.showError("Incorrect Card Type","Card type must be either MasterCard, Visa, or American Express.");
		        return;
		    }


		    // Validate card number (should be only digits)
		    if (!cardNumber.getText().matches("\\d+")) {
		    	 AlertUtils.showError("Invalid Card Number","Card number should only contain numbers.");
		        return ;
		    }

		    // Validate expiration date format
		    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    try {
		        LocalDate.parse(expDate.getText(), formatter1);
		    } catch (DateTimeParseException e) {
		    	AlertUtils.showError("Invalid Expiry Date or Invalid Format","Expiration date must be in the format yyyy-MM-dd.");
		        return ;
		    }
		    
		expdate += " 11:59:00";
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(expdate, formatter);

        // Convert LocalDateTime to Timestamp
        Timestamp timestamp_exp = Timestamp.valueOf(dateTime);

		
		// Create an instance of CreditCardPaymentDAO
//		dao.PaymentDAO creditPaymentDAO = new dao.CreditCardPaymentDAO();
//
//		try {
//		    // Retrieve payments by booking ID
//		    Map<Integer, Payment> paymentsMap = creditPaymentDAO.getPaymentsByBookingID(this.bookingId);
//
//		    // Iterate through each entry in the map
//		    for (Map.Entry<Integer, Payment> entry : paymentsMap.entrySet()) {
//		        Payment payment = entry.getValue(); // Get the Payment object from the map entry
//		        
//		        payment.setPaymentMethod("Credit Card");
//		        //payment.setPaymentStatus("Completed");
//		        payment.processPayment();
//		        // Call the update method for each payment
//		        creditPaymentDAO.update(payment);
//		    }
//		} catch (SQLException e) {
//		    // Handle any SQL exceptions
//		    e.printStackTrace();
//		}

		dao.BookingDAO bookingdao = new dao.BookingDAO();
		Booking booking = null;
		booking = bookingdao.get(bookingId);
		booking.setPaymentStatus("Paid");
		
		bookingdao.update(booking);

		//public CreditCardPayment(int bookingID, BigDecimal amount, String paymentMethod, String paymentStatus, java.sql.Timestamp transactionDate, int payerID, int receiverID, String cardNumber, String cardType, String cardHolderName, java.sql.Timestamp expirationDate)
		int serviceId = booking.getServiceID();
		
		dao.ServiceDAO servicedao = new dao.ServiceDAO();
		Service service = null;
		service = servicedao.get(serviceId);
		Timestamp currentTimestamp = new Timestamp(new Date().getTime());
		
		Payment payment = new CreditCardPayment(this.bookingId,BigDecimal.valueOf(service.getServicePrice()),"Credit Card", currentTimestamp,booking.getClientID(),service.getServiceProviderID(),cardnumber,cardtype,nameoncard,timestamp_exp);
		
		dao.PaymentDAO paymentdao = new dao.CreditCardPaymentDAO();
		
		int id = paymentdao.insert(payment);
		if(id==-1) {
			return;
		}
		
		payment.getPaymentStrategy().processPayment(payment);
		// here i will call payment.paymentStrategy.process(payment)
			// it will deduct a specified amount of payment from payment 
			// send the deduction to admin
			// send the remaining to seller
		//send notification
		dao.NotificationDAO notificationdao = new dao.NotificationDAO();
		//create and insert a notification
		// public Notification(int recipientID, String notificationMessage, 
		//java.sql.Timestamp timestamp, String status, String type, String recipientRole)
		String priceString = BigDecimal.valueOf(service.getServicePrice()).toString();

		String notification_msg = "Your Payment of amount " + priceString + " has been processed for service "+ service.getServiceName();
	
		Notification notification = new Notification(service.getServiceProviderID(),notification_msg,currentTimestamp , "Unread", "PaymentStatus", "Seller");
		
		int a = notificationdao.insert(notification);
		System.out.println(a);
		
//		System.out.println("Booking ID: " + this.bookingId);
//		System.out.println("Service ID: " + serviceId);
//		System.out.println("Service Provider Id: " + service.getServiceProviderID());
//		
		AlertUtils.showSuccess("Payment Processed!");

		pay_button.setDisable(true);

	}
	
}
	