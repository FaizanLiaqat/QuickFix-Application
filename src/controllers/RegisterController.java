package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dao.BuyerDAO;
import dao.SellerDAO;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.Initializable;
import models.User;
import utils.AlertUtils;
import utils.UserSingleton;

public class RegisterController implements Initializable{
	@FXML private Label registerMessage;
	@FXML private TextField name_textfield;
	@FXML private TextField email_textfield;
	@FXML private PasswordField password_textfield;
	@FXML private TextField MobileNumber_textfield;
	@FXML private TextField Age_textfield;
	@FXML private ChoiceBox<String> Location_textfield;
	private String[] Location= {"Islamabad" , "Rawalpindi" , "Lahore", "Karachi" , "Peshawar" , "Quetta"};
	@FXML private Button register_button;
	@FXML private Button back_button;
	
	private UserDAO userDAO;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Location_textfield.getItems().addAll(Location);
	}

	public void RegisterButtonOnAction(ActionEvent event) {
		
		if (name_textfield.getText().isBlank() || email_textfield.getText().isBlank() || password_textfield.getText().isBlank() 
		        || MobileNumber_textfield.getText().isBlank() || Age_textfield.getText().isBlank() || Location_textfield.getSelectionModel().isEmpty()) {

		        registerMessage.setText("Please fill in all fields.");
		    } else {
		    	// Get the User object from the singleton instance
		        User user = UserSingleton.getInstance().getUserObject();
		        
		        // Set user attributes from the input fields
		        user.setUserName(name_textfield.getText());
		        user.setUserEmail(email_textfield.getText());
		        user.setUserPassword(password_textfield.getText());
		        user.setUserPhoneNumber(MobileNumber_textfield.getText());
		        user.setUserLocation(Location_textfield.getValue()); // Assuming Location is a String
		        
		        if(user.getUserType().equalsIgnoreCase("Buyer")) {
					this.userDAO = new BuyerDAO();
				}
				else if(user.getUserType().equalsIgnoreCase("Seller")) {
					this.userDAO = new SellerDAO();
				}
				try {
					if(this.userDAO.emailExists(user)) {
						registerMessage.setText("Please Enter Unique Email");
					}else {
						try {
					        // Inserting user data into User table
					        this.userDAO.insert(user);
					        
					        // Show success message
					        registerMessage.setText("Registration successful! You can now log in.");  
			                AlertUtils.showSuccess("Registeration successful!");
			        		try {

			        			// Close the current window (home.fxml)
			        			Stage currentStage = (Stage) back_button.getScene().getWindow();
			        			currentStage.close();

			        			// Load the Access.fxml file
			        			Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));

			        			// Create a new Stage (window) for Access.fxml
			        			Stage stage = new Stage();
			        			stage.initStyle(StageStyle.UNDECORATED); // Make the window undecorated (no borders or title bar)
			        			stage.setTitle("Login"); // Set the title of the new window

			        			// Set the new scene with the loaded FXML and desired size
			        			Scene scene = new Scene(root, 810, 620); // Set dimensions similar to your original configuration
			        			stage.setScene(scene);

			        			// Show the new window (stage)
			        			stage.show();

			        		} catch (IOException e) {
			        			e.printStackTrace(); // Handle error if FXML file loading fails
			        		}
			    			
					    } catch (SQLException e) {
					        e.printStackTrace();
					        registerMessage.setText("Error during registration. Please try again.");
					    };
					}
				} catch (SQLException e) {
					
					e.printStackTrace();
				} 
		    }

	}
	
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
			Scene scene = new Scene(root, 810, 620); // Set dimensions similar to your original configuration
			stage.setScene(scene);

			// Show the new window (stage)
			stage.show();

		} catch (IOException e) {
			e.printStackTrace(); // Handle error if FXML file loading fails
		}
	}
}
