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
import java.math.BigDecimal;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Booking;
import models.CreditCardPayment;
import models.FeedBack;
import models.Notification;
import models.Payment;
import models.Service;
import utils.AlertUtils;
import utils.UserSingleton;

public class FeedbackController implements Initializable {

	@FXML
	private Button feedback_submit;
	
	@FXML
	private TextArea feedback_review;
	
	@FXML
	private Slider feedback_slider;
	
	
	private int bookingId;
	
	
	
	
	public void setBookingId(int id) {
		this.bookingId = id;
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
	
	
	
	public void submitOnAction(javafx.event.ActionEvent actionEvent) throws IOException{
		
	    
		int rating = (int) feedback_slider.getValue();
		String review = feedback_review.getText();
		
		AlertUtils au = null;
		//input validation
		if (review == null || review.trim().isEmpty()) {
	        // Display error for empty review
	        au.showError("Review is empty", "Review field cannot be empty");
	        return;
	    }
		
		System.out.println(rating);
		System.out.println(review);
		
		
		//Insert into database
		dao.BookingDAO bookingdao = new dao.BookingDAO();
		Booking booking = null;
		try {
			 booking = bookingdao.get(bookingId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			bookingdao.update(booking);
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
		//public FeedBack(int clientID, int serviceProviderID, int bookingID,int serviceID, int rating, String comments, java.sql.Timestamp feedbackDate)
		FeedBack feedback = new FeedBack(booking.getClientID(),service.getServiceProviderID(),this.bookingId,serviceId,rating,review,currentTimestamp);
		
		FeedbackDAO feedbackdao = new FeedbackDAO();
		try {
			int res = feedbackdao.insert(feedback);
			if(res==-1) {
		        au.showError("Feedback Already Given", "Feedback already given for selected booking");
		        return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//send notification
		dao.NotificationDAO notificationdao = new dao.NotificationDAO();
		
		
		String buyer_name = UserSingleton.getInstance().getUserObject().getUserName();
		String notification_msg = "Feedback has been given by " + buyer_name + " for service  "+ service.getServiceName() + " : " + review;
	
		Notification notification = new Notification(service.getServiceProviderID(),notification_msg,currentTimestamp , "Unread", "FeedbackReceived", "Seller");
		
		try {
			int a = notificationdao.insert(notification);
			System.out.println(a);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 au.showSuccess("Feedback Submitted");
	   
		feedback_submit.setDisable(true);
		
	    	}
	
	
	

	

	

	
	
	
		  
		  
}

