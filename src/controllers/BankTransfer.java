package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.BankTransferPayment;
import models.Booking;
import models.CreditCardPayment;
import models.Notification;
import models.Payment;
import models.Service;
import utils.AlertUtils;

public class BankTransfer {
	@FXML
	private Button pay_button;
	
	@FXML
	private Button back_button;
	
	@FXML
	private TextField accountNumber;
	
	@FXML
	private TextField bankName;
	
	
	private String accountnumber;
	
	private String bankname;
	
	private int bookingId;
	
	public void setBookingId(int id) {
		this.bookingId = id;
	}


	
	
//	public void goBackOnAction(ActionEvent event) {
//		try {
//
//			// Close the current window (home.fxml)
//			Stage currentStage = (Stage) back_button.getScene().getWindow();
//			currentStage.close();
//
//			// Load the Access.fxml file
//            String fxmlFile = "/views/buyer_dashboard.fxml";
//            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
//
//			// Create a new Stage (window) for Access.fxml
//			Stage stage = new Stage();
//			stage.initStyle(StageStyle.UNDECORATED); // Make the window undecorated (no borders or title bar)
//			stage.setTitle("Home Window"); // Set the title of the new window
//
//			// Set the new scene with the loaded FXML and desired size
//			Scene scene = new Scene(root, 810, 620); // Set dimensions similar to your original configuration
//			stage.setScene(scene);
//
//			// Show the new window (stage)
//			stage.show();
//
//		} catch (IOException e) {
//			e.printStackTrace(); // Handle error if FXML file loading fails
//		}
//	}
//	

	public void actionOnPay(ActionEvent event) {
		accountnumber = accountNumber.getText();
		bankname = bankName.getText();
		
		
		 if (accountNumber.getText().isEmpty() || bankName.getText().isEmpty()) {
			 AlertUtils.showError("Empty TextField","Account number and bank name are required.");
		        return;
		    }

		
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
		//public BankTransferPayment(int bookingID, BigDecimal amount, String paymentMethod, java.sql.Timestamp transactionDate, int payerID, int receiverID, String bankAccountNumber, String bankName, String referenceCode, java.sql.Timestamp transferDate) 
		Payment payment = new BankTransferPayment(this.bookingId,BigDecimal.valueOf(service.getServicePrice()),"Bank Transfer", currentTimestamp,booking.getClientID(),service.getServiceProviderID(),accountnumber,bankname,"123",currentTimestamp);
		
		dao.PaymentDAO paymentdao = new dao.BankTransferPaymentDAO();
		
		paymentdao.insert(payment);
		
		payment.getPaymentStrategy().processPayment(payment);
		//send notification
		dao.NotificationDAO notificationdao = new dao.NotificationDAO();
		//create and insert a notification
		// public Notification(int recipientID, String notificationMessage, 
		//java.sql.Timestamp timestamp, String status, String type, String recipientRole)
		String priceString = BigDecimal.valueOf(service.getServicePrice()).toString();

		String notification_msg = "Your Payment of amount " + priceString + " has been processed for service "+ service.getServiceName();
	
		Notification notification = new Notification(service.getServiceProviderID(),notification_msg,currentTimestamp , "Unread", "PaymentStatus", "Seller");
		
		notificationdao.insert(notification);
		
		AlertUtils.showSuccess("Payment Processed!");

		pay_button.setDisable(true);
		
		
	}
	
}
	