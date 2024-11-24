package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.CreditCardPayment;
import models.Payment;
import models.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;



public class paymenthistoryController implements Initializable {

	@FXML
	private GridPane grid;

	@FXML
	private ScrollPane scroll;
	
	@FXML
	private Button back_button;
	

	private String callerType; // Field to track the caller
	  
	  // Method to set the caller type
	public void setCallerType(String callerType) {
		this.callerType = callerType; 
		}
	
	
	List<Payment> payments_array = new ArrayList<>();


//	public void setCallerType(String type) {
//		this.setCallerType(type);
//	}

	private List<Payment> getData() {

		//CreditCardPayment(int paymentID, int bookingID, BigDecimal amount, String paymentMethod,
		//String paymentStatus, int paymentID2, String cardNumber, String cardType, String cardHolderName,
		//Date expirationDate)
		List<Payment> payments = new ArrayList<>();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis()); // January 15, 2025

        // Convert LocalDate to Date
       

//        paymentID, // paymentID from the Payment table
//        bookingID, // bookingID
//        amount, // amount
//        paymentMethod, // paymentMethod
//        paymentStatus, // paymentStatus
//        transactionDate, // transactionDate
//        payerID, // payerID
//        receiverID, // receiverID
//        cardNumber, // cardNumber from CreditCardPayment table
//        cardType, // cardType
//        cardHolderName, // cardHolderName
//        expirationDate // expirationDate
		payments.add(new CreditCardPayment(1,1,new BigDecimal("3000"), "Credit card","Completed",currentTimestamp,2,3,"3467", "PAYPAL", "Ali",currentTimestamp));
		payments.add(new CreditCardPayment(1,1,new BigDecimal("5000"), "Credit card","Completed",currentTimestamp,2,3,"3467", "PAYPAL", "Hamza",currentTimestamp));

		//payments.add(new CreditCardPayment(1,2,4000,"Bank Transfer", "Completed"));

		return payments;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	    payments_array.addAll(getData()); // Load all services

	    int column = 0;
	    int row = 1;

	    try {
	        for (Payment payment : payments_array) {

	            // Trim and compare names
	            
	                FXMLLoader fxmlLoader = new FXMLLoader();
	                fxmlLoader.setLocation(getClass().getResource("/views/payment_item.fxml"));
	                Pane pane = fxmlLoader.load();

	                PaymentItemController paymentitemController = fxmlLoader.getController();
	                paymentitemController.setData(payment);
	                
	                if (column == 1) {
	                    column = 0;
	                    row++;
	                }

	                grid.add(pane, column++, row); // Add to grid at (column, row)

	                // Set grid dimensions
	                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
	                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
	                grid.setMaxWidth(Region.USE_PREF_SIZE);

	                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
	                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
	                grid.setMaxHeight(Region.USE_PREF_SIZE);

	                GridPane.setMargin(pane, new Insets(10));
	            }
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void backButtonOnAction(ActionEvent event) { try { // Close the
		  Stage currentStage = (Stage) back_button.getScene().getWindow();
		  currentStage.close();
		  
		// Load the appropriate dashboard based on callerType
		  String fxmlFile = callerType.equals("buyer") ? "/views/buyer_dashboard.fxml" :
		  "/views/seller_dashboard.fxml"; 
		  Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
		  
		  // Create a new stage for the dashboard
		  Stage stage = new Stage();
		  stage.initStyle(StageStyle.UNDECORATED); Scene scene = new Scene(root, 520,
		  400); stage.setScene(scene); stage.show();
		  
		  } catch (IOException e) { e.printStackTrace(); } }


}
