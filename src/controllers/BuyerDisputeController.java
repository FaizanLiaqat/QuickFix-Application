/*
 * package controllers;
 * 
 * import java.io.IOException;
 * 
 * import javafx.event.ActionEvent; import javafx.fxml.FXML; import
 * javafx.fxml.FXMLLoader; import javafx.scene.Parent; import
 * javafx.scene.Scene; import javafx.scene.control.Button; import
 * javafx.stage.Stage; import javafx.stage.StageStyle;
 * 
 * public class viewprofileController {
 * 
 * @FXML private Button back_button;
 * 
 * @FXML private Button notification_button;
 * 
 * private String callerType; // Field to track the caller
 * 
 * // Method to set the caller type public void setCallerType(String callerType)
 * { this.callerType = callerType; }
 * 
 * public void backButtonOnAction(ActionEvent event) { try { // Close the
 * current window Stage currentStage = (Stage)
 * back_button.getScene().getWindow(); currentStage.close();
 * 
 * // Load the appropriate dashboard based on callerType String fxmlFile =
 * callerType.equals("buyer") ? "/views/buyer_dashboard.fxml" :
 * "/views/seller_dashboard.fxml"; Parent root =
 * FXMLLoader.load(getClass().getResource(fxmlFile));
 * 
 * // Create a new stage for the dashboard Stage stage = new Stage();
 * stage.initStyle(StageStyle.UNDECORATED); Scene scene = new Scene(root, 520,
 * 400); stage.setScene(scene); stage.show();
 * 
 * } catch (IOException e) { e.printStackTrace(); } }
 * 
 * public void NotificationbuttonOnAction(ActionEvent event) { Stage
 * currentStage = (Stage) notification_button.getScene().getWindow();
 * currentStage.close(); } }
 */

package controllers;

import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

import dao.FeedbackDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Booking;
import models.Dispute;
import models.Notification;
import models.Service;
import utils.AlertUtils;
import utils.UserSingleton;

public class BuyerDisputeController implements Initializable {

	@FXML
	private Button back_button;

	@FXML
	private Button submit_button;
	
	 @FXML
	private TextArea text_dispute;
	
	@FXML
	private AnchorPane contentArea;

	private String callerType; // Field to track the caller

	// Method to set the caller type
	
	private int bookingId;
	
	public void setBookingId(int id) {
		this.bookingId = id;
	}
	public void setCallerType(String callerType) {
		this.callerType = callerType;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

//		try {
//			Parent fxml  = FXMLLoader.load(getClass().getResource("/views/profile.fxml"));
//		    contentArea.getChildren().removeAll();
//		    contentArea.getChildren().setAll(fxml);
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void backOnAction(ActionEvent event) {
		try { // Close the
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the appropriate dashboard based on callerType
			String fxmlFile = callerType.equals("buyer") ? "/views/buyer_dashboard.fxml"
					: "/views/seller_dashboard.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

			// Create a new stage for the dashboard
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			Scene scene = new Scene(root, 810, 620);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void submitOnAction(ActionEvent event) {
		// what happens when change password button is clicked
		String text = text_dispute.getText();
		
		AlertUtils au = null;
		//input validation
		if (text == null || text.trim().isEmpty()) {
	        // Display error for empty review
	        au.showError("Dispute Reason is empty", "Dispute Reason cannot be empty");
	        return;
	    }
		
		System.out.println(text);
		
		
		
		//Insert into database
		dao.BookingDAO bookingdao = new dao.BookingDAO();
		Booking booking = null;
		try {
			 booking = bookingdao.get(bookingId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		//public CreditCardPayment(int bookingID, BigDecimal amount, String paymentMethod, String paymentStatus, java.sql.Timestamp transactionDate, int payerID, int receiverID, String cardNumber, String cardType, String cardHolderName, java.sql.Timestamp expirationDate)
		int serviceId = booking.getServiceID();
		
		dao.ServiceDAO servicedao = new dao.ServiceDAO();
		Service service = null;
		try {
			 service = servicedao.get(serviceId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timestamp currentTimestamp = new Timestamp(new Date().getTime());
		Timestamp res_time = null;
		
		//public Dispute(int bookingID, int buyerID, int sellerID, String disputeReason, String disputeStatus,
		//String resolutionDetails, java.sql.Timestamp createdAt, java.sql.Timestamp resolvedAt)
		
		Dispute dispute = new Dispute(this.bookingId,booking.getClientID(),service.getServiceProviderID(),text,"Open","",currentTimestamp,res_time);
		
		dao.DisputeDAO disputedao = new dao.DisputeDAO();
		int res = disputedao.insert(dispute);
		if(res==-1) {
		    au.showError("Dispute Already submitted", "The dispute for this booking is already submitted");
		    return;


		}
		
		//send notification
				dao.NotificationDAO notificationdao = new dao.NotificationDAO();
				
				
				String buyer_name = UserSingleton.getInstance().getUserObject().getUserName();
				String notification_msg = "Dispute initiated by " + buyer_name + " for service  "+ service.getServiceName() + " : " + text;
			
				Notification notification = new Notification(service.getServiceProviderID(),notification_msg,currentTimestamp , "Unread", "Dispute", "Seller");
				
				try {
					int a = notificationdao.insert(notification);
					System.out.println(a);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			    
				submit_button.setDisable(true);
				
	}

}
