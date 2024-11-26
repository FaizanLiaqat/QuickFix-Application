package controllers;

import java.io.IOException;
import java.sql.SQLException;

import dao.ServiceDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Service;
import models.User;
import utils.UserSingleton;

public class AddServiceController {

    @FXML
    private Button create_button;
    

    @FXML
    private Button back_button;

    @FXML
    private TextField description;

    @FXML
    private TextField price;

    @FXML
    private TextField service_name;

    private User user; // Declare a user object to store the current user

    @FXML
    void create_buttonOnAction(ActionEvent event) {
        // Retrieve the current user from UserSingleton
        this.user = UserSingleton.getInstance().getUserObject();

        // Validate inputs before proceeding
        if (!validateInputs()) {
            return; // Exit if inputs are invalid
        }

        // Initialize the ServiceDAO object and create a new service
        ServiceDAO sdao = new ServiceDAO();
        Service service = new Service();
        service.setServiceName(service_name.getText());
        service.setServiceDescription(description.getText());
        service.setServicePrice(Integer.parseInt(price.getText()));
        service.setServiceRating(1); // Default rating for a new service
        service.setServiceProviderID(user.getUserID());

        // Insert the service into the database
		sdao.insert(service);
		System.out.println("Service successfully added!");

        // Close the current window and open the "view_service.fxml" window
        closeCurrentWindow();
        openViewServicesWindow();
    }

    /**
     * Validates user inputs and shows an alert if validation fails.
     */
    private boolean validateInputs() {
        if (service_name.getText().isEmpty() || description.getText().isEmpty() || price.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please fill in all fields.");
            return false;
        }

        try {
            Integer.parseInt(price.getText()); // Validate that price is an integer
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Price", "Please enter a valid number for the price.");
            return false;
        }

        return true;
    }

    /**
     * Closes the current stage.
     */
    private void closeCurrentWindow() {
        Stage stage = (Stage) create_button.getScene().getWindow();
        stage.close();
    }

    /**
     * Opens the "view_service.fxml" window.
     */
    private void openViewServicesWindow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/view_service.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            Scene scene = new Scene(root, 810, 620);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open the services view.");
        }
    }

    /**
     * Shows an alert with the specified type, title, and content.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void backButtonOnAction(javafx.event.ActionEvent event) {
    	try {

			// Close the current window (home.fxml)
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/views/view_service.fxml"));

			// Create a new Stage (window) for Access.fxml
			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED); // Make the window undecorated (no borders or title bar)
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

