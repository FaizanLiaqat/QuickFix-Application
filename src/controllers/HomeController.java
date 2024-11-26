package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Buyer;
import models.Seller;
import models.Service;
import models.User;

import java.io.IOException;
import java.sql.SQLException;

import dao.ServiceDAO;
import utils.UserSingleton;

public class HomeController {

	@FXML
	private Button exit_button;

	private UserSingleton user = UserSingleton.getInstance();


	// Event to close the current window
	public void exit_buttonOnAction(ActionEvent e) {
		Stage stage = (Stage) exit_button.getScene().getWindow();
		stage.close();
	}

	// Event to open Access.fxml
	public void openAccessButtonOnAction(ActionEvent event) {
		try {
			// Get the source of the event and cast it to Button
			Button clickedButton = (Button) event.getSource();

			// Print or use the fx:id of the clicked button
			System.out.println("Button clicked: " + clickedButton.getId());

			if (clickedButton.getId().equalsIgnoreCase("buyer_button")) {
				user.setUserObject("Buyer");
			} else if (clickedButton.getId().equalsIgnoreCase("seller_button")) {
				user.setUserObject("Seller");
			} else if (clickedButton.getId().equalsIgnoreCase("admin_button")) {
				user.setUserObject("Admin");
			}

			// Close the current window (home.fxml)
			Stage currentStage = (Stage) exit_button.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/views/access.fxml"));

			// Create a new Stage (window) for Access.fxml
			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED); // Make the window undecorated (no borders or title bar)
			stage.setTitle("Access Window"); // Set the title of the new window

			// Set the new scene with the loaded FXML and desired size
			Scene scene = new Scene(root, 810, 620); // Set dimensions similar to your original configuration
			stage.setScene(scene);

			// Show the new window (stage)
			stage.show();

		} catch (IOException e) {
			e.printStackTrace(); // Handle error if FXML file loading fails
		}
	}

	// Event handler for the "Add Servcice" button
	public void openAdminLogin(ActionEvent event) {
		try {
			// Get the source of the event and cast it to Button
			Button clickedButton = (Button) event.getSource();

			// Print or use the fx:id of the clicked button
			System.out.println("Button clicked: " + clickedButton.getId());
			
				user.setUserObject("Admin");

			// Close the current window (home.fxml)
			Stage currentStage = (Stage) exit_button.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));

			// Create a new Stage (window) for Access.fxml
			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED); // Make the window undecorated (no borders or title bar)
			stage.setTitle("Access Window"); // Set the title of the new window

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
