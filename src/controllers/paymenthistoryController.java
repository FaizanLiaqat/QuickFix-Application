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

public class paymenthistoryController {
	@FXML private Button back_button;

    private String callerType; // Field to track the caller

    // Method to set the caller type
    public void setCallerType(String callerType) {
        this.callerType = callerType;
    }
    
	public void backButtonOnAction(ActionEvent event) {
		try {

			// Close the current window (home.fxml)
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
            String fxmlFile = callerType.equals("buyer") ? "/views/buyer_dashboard.fxml" : "/views/seller_dashboard.fxml";
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
}
