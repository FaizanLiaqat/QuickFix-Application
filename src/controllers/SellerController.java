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
import utils.UserSingleton;

public class SellerController {

	@FXML
	private Button view_profile;
	@FXML
	private Button book_service;
	@FXML
	private Button payment_history;
	@FXML
	private Button exit;
	private String currenttype = "seller";

	// Event to close the current window
	public void exit_buttonOnAction(ActionEvent e) {
		Stage stage = (Stage) exit.getScene().getWindow();
		stage.close();
	}

	public void openViewProfileButtonOnAction(ActionEvent event) {
		try {
			// Close the current window (home.fxml)
			Stage currentStage = (Stage) exit.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/view_profile.fxml"));
			Parent root = loader.load();
			// Get the controller for the register window
			viewprofileController viewprofileController = loader.getController();

			// Pass the User object to the register controller

			viewprofileController.setCallerType(currenttype);

			// Create a new Stage (window) for Access.fxml
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED); // Make the window undecorated (no borders or title bar)

			// Set the new scene with the loaded FXML and desired size
			Scene scene = new Scene(root, 520, 400); // Set dimensions similar to your original configuration
			stage.setScene(scene);

			// Show the new window (stage)
			stage.show();

		} catch (IOException e) {
			e.printStackTrace(); // Handle error if FXML file loading fails
		}
	}

	public void openViewServiceButtonOnAction(ActionEvent event) {
		try {
			// Close the current window (home.fxml)
			Stage currentStage = (Stage) exit.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/views/view_service.fxml"));

			// Create a new Stage (window) for Access.fxml
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED); // Make the window undecorated (no borders or title bar)

			// Set the new scene with the loaded FXML and desired size
			Scene scene = new Scene(root, 800 , 620); // Set dimensions similar to your original configuration
			stage.setScene(scene);

			// Show the new window (stage)
			stage.show();

		} catch (IOException e) {
			e.printStackTrace(); // Handle error if FXML file loading fails
		}
	}

	public void openPaymentHistoryButtonOnAction(ActionEvent event) {
		try {
			// Close the current window (home.fxml)
			Stage currentStage = (Stage) exit.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/payment_history.fxml"));
			Parent root = loader.load();
			// Get the controller for the register window
			paymenthistoryController paymenthistoryController = loader.getController();

			// Pass the User object to the register controller

			paymenthistoryController.setCallerType(currenttype);
			// Create a new Stage (window) for Access.fxml
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED); // Make the window undecorated (no borders or title bar)

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
