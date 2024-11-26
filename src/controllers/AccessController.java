package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AccessController {
	@FXML private Button login_button;
	@FXML private Button register_button;
	// Event handler for the "Back" button
		public void loginButtonOnAction(ActionEvent event) {
			try {

				// Close the current window (home.fxml)
				Stage currentStage = (Stage) login_button.getScene().getWindow();
				currentStage.close();

				// Load the Access.fxml file
				Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));

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
		public void registerButtonOnAction(ActionEvent event) {
			try {

				// Close the current window (home.fxml)
				Stage currentStage = (Stage) register_button.getScene().getWindow();
				currentStage.close();

				// Load the Access.fxml file
				Parent root = FXMLLoader.load(getClass().getResource("/views/register.fxml"));
		       
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
