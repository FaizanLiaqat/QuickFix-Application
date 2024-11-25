/*
 * package controllers;
 * 
 * import java.io.IOException;
 * 
 * import javafx.event.ActionEvent; import javafx.fxml.FXML; import
 * javafx.fxml.FXMLLoader; import javafx.scene.Parent; import
 * javafx.scene.Scene; import javafx.scene.control.Button; import
 * javafx.stage.Stage; import javafx.stage.StageStyle;
 * 
 * public class viewprofileController {
 * 
 * @FXML private Button back_button;
 * 
 * @FXML private Button notification_button;
 * 
 * private String callerType; // Field to track the caller
 * 
 * // Method to set the caller type public void setCallerType(String callerType)
 * { this.callerType = callerType; }
 * 
 * public void backButtonOnAction(ActionEvent event) { try { // Close the
 * current window Stage currentStage = (Stage)
 * back_button.getScene().getWindow(); currentStage.close();
 * 
 * // Load the appropriate dashboard based on callerType String fxmlFile =
 * callerType.equals("buyer") ? "/views/buyer_dashboard.fxml" :
 * "/views/seller_dashboard.fxml"; Parent root =
 * FXMLLoader.load(getClass().getResource(fxmlFile));
 * 
 * // Create a new stage for the dashboard Stage stage = new Stage();
 * stage.initStyle(StageStyle.UNDECORATED); Scene scene = new Scene(root, 520,
 * 400); stage.setScene(scene); stage.show();
 * 
 * } catch (IOException e) { e.printStackTrace(); } }
 * 
 * public void NotificationbuttonOnAction(ActionEvent event) { Stage
 * currentStage = (Stage) notification_button.getScene().getWindow();
 * currentStage.close(); } }
 */

package controllers;

import java.io.IOException;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class viewprofileController implements Initializable {

	@FXML
	private Button back_button;

	@FXML
	private Button change_password;

	@FXML
	private Button notification_button;

	@FXML
	private Button change_button;

	@FXML
	private Label bookedServicesLabel;

	@FXML
	private Pane contentArea;

	private String callerType; // Field to track the caller

	// Method to set the caller type
	public void setCallerType(String callerType) {
		this.callerType = callerType;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

//		try {
//			Parent fxml  = FXMLLoader.load(getClass().getResource("/views/profile.fxml"));
//		    contentArea.getChildren().removeAll();
//		    contentArea.getChildren().setAll(fxml);
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void updateLabelBasedOnUserType(String userType) {
		if (userType.equals("Admin")) {
			bookedServicesLabel.setText("Number of managed services:");
		} else if (userType.equals("buyer")) {
			bookedServicesLabel.setText("Number of booked services:");
		} else if (userType.equals("seller")) {
			bookedServicesLabel.setText("Number of offered services:");
		} else {
			bookedServicesLabel.setText("User-specific info:");
		}
	}

	public void viewProfile(javafx.event.ActionEvent actionEvent) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/profile.fxml"));
		Parent fxml = loader.load();

		// Get the controller instance from the loader
		viewprofileController controller = loader.getController();

		// Update the label based on user type using the controller
		controller.updateLabelBasedOnUserType(this.callerType);

		// Update the contentArea (make sure contentArea is of type AnchorPane or
		// similar)
		contentArea.getChildren().clear();
		contentArea.getChildren().add(fxml);
	}

	public void Notifications(javafx.event.ActionEvent actionEvent) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/views/Notifications.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(fxml);
	}

	public void Change_Password(javafx.event.ActionEvent actionEvent) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/views/Change_password.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(fxml);
	}

	public void backButtonOnAction(ActionEvent event) {
		try { // Close the
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the appropriate dashboard based on callerType
			String fxmlFile = callerType.equals("buyer") ? "/views/buyer_dashboard.fxml"
					: "/views/seller_dashboard.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

			// Create a new stage for the dashboard
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			Scene scene = new Scene(root, 810, 620);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void changePasswordOnAction(ActionEvent event) {
		// what happens when change password button is clicked
	}

}
