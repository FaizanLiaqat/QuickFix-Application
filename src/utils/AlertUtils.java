package utils;

import javafx.scene.control.Alert;

public class AlertUtils {
	// General method to show alerts of different types
    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header text
        alert.setContentText(message);  // Set the content message
        alert.showAndWait();  // Show the alert and wait for the user to close it
    }

    // Specific method to show error alerts
    public static void showError(String title, String message) {
        showAlert(title, message, Alert.AlertType.ERROR);
    }

    // Specific method to show success alerts
    public static void showSuccess(String message) {
        showAlert("Success", message, Alert.AlertType.INFORMATION);
    }

    // Specific method to show warning alerts
    public static void showWarning(String message) {
        showAlert("Warning", message, Alert.AlertType.WARNING);
    }
}
