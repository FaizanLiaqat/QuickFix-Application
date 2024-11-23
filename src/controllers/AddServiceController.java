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

        try {
            // Insert the service into the database
            sdao.insert(service);
            System.out.println("Service successfully added!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add the service. Please try again.");
            return;
        }

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
            stage.initStyle(StageStyle.UNDECORATED);
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
}

