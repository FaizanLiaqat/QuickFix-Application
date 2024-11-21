package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.User;

import java.io.IOException;
import javafx.fxml.Initializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.URL;

import utils.UserSingleton;

import java.util.ResourceBundle;

import dao.BuyerDAO;
import dao.DatabaseConnection;
import dao.SellerDAO;
import dao.UserDAO;

public class LoginController {
	@FXML private Label loginMessage;
	@FXML private Button login_button;
	@FXML private TextField email_textfield;
	@FXML private PasswordField password_textfield;
	@FXML private Button back_button;
	
	
	private UserSingleton user = UserSingleton.getInstance();
	private UserDAO userDAO;
	
	

	public void loginButtonOnAction(ActionEvent event) {
		
		ValidateLogin();

//		if(email_textfield.getText().isBlank()==false && password_textfield.getText().isBlank()==false) {
//			ValidateLogin();
//		}
//		else {
//			loginMessage.setText("Please enter email and password.");
//		}
	}

	
	
	public void ValidateLogin() {
	    try {
	        if (user.getUserObject().getUserType().equalsIgnoreCase("Buyer")) {
	            //this.userDAO = new BuyerDAO();  // Assign BuyerDAO
//	            if (this.userDAO.exists(this.user.getUserObject()))
	            if(true){
	                System.out.println(this.user.getUserObject() + " Login Successfully");
	                
	                // Close the current window (home.fxml)
	                Stage currentStage = (Stage) login_button.getScene().getWindow();
	                currentStage.close();

	                // Load the Buyer dashboard FXML
	                Parent root = FXMLLoader.load(getClass().getResource("/views/buyer_dashboard.fxml"));
	                
	                // Create a new Stage (window) for Buyer dashboard
	                Stage stage = new Stage();
	                stage.initStyle(StageStyle.UNDECORATED); // Make the window undecorated (no borders or title bar)
	                stage.setTitle("Buyer Dashboard"); // Set the title of the new window

	                // Set the new scene with the loaded FXML and desired size
	                Scene scene = new Scene(root, 520, 400); // Set dimensions similar to your original configuration
	                stage.setScene(scene);

	                // Show the new window (stage)
	                stage.show();
	            } else {
	                loginMessage.setText("Incorrect credentials.");
	            }
	        } else if (user.getUserObject().getUserType().equalsIgnoreCase("Seller")) {
	           // this.userDAO = new SellerDAO();  // Assign SellerDAO
	            //if (this.userDAO.exists(this.user.getUserObject()))
	        	if(true){
	                System.out.println(this.user.getUserObject() + " Login Successfully");
	                
	                // Close the current window (home.fxml)
	                Stage currentStage = (Stage) login_button.getScene().getWindow();
	                currentStage.close();

	                // Load the Seller dashboard FXML
	                Parent root = FXMLLoader.load(getClass().getResource("/views/seller_dashboard.fxml"));
	                
	                // Create a new Stage (window) for Seller dashboard
	                Stage stage = new Stage();
	                stage.initStyle(StageStyle.UNDECORATED); // Make the window undecorated (no borders or title bar)
	                stage.setTitle("Seller Dashboard"); // Set the title of the new window

	                // Set the new scene with the loaded FXML and desired size
	                Scene scene = new Scene(root, 520, 400); // Set dimensions similar to your original configuration
	                stage.setScene(scene);

	                // Show the new window (stage)
	                stage.show();
	            } else {
	                loginMessage.setText("Incorrect credentials.");
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace(); // Handle error if FXML file loading fails
	    }
	}

	
	// Event handler for the "Back" button
		public void backButtonOnAction(ActionEvent event) {
			try {

				// Close the current window (home.fxml)
				Stage currentStage = (Stage) back_button.getScene().getWindow();
				currentStage.close();

				// Load the Access.fxml file
				Parent root = FXMLLoader.load(getClass().getResource("/views/access.fxml"));

				// Create a new Stage (window) for Access.fxml
				Stage stage = new Stage();
				stage.initStyle(StageStyle.UNDECORATED); // Make the window undecorated (no borders or title bar)
				stage.setTitle("Home Window"); // Set the title of the new window

				// Set the new scene with the loaded FXML and desired size
				Scene scene = new Scene(root, 520, 400); // Set dimensions similar to your original configuration
				stage.setScene(scene);

				// Show the new window (stage)
				stage.show();

			} catch (IOException e) {
				e.printStackTrace(); // Handle error if FXML file loading fails
			}
		}
}
