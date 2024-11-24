package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
	
	



	
	
	public void backButtonOnAction(ActionEvent event) {
		try {

			// Close the current window (home.fxml)
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
            String fxmlFile = "/views/buyer_dashboard.fxml";
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

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
	
	public void actionOnPay(ActionEvent event) {
		accountnumber = accountNumber.getText();
		bankname = bankName.getText();
		
	}
	
}
	