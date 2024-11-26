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
import javafx.scene.control.TextArea;

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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BuyerDisputeController implements Initializable {

	@FXML
	private Button back_button;

	@FXML
	private Button submit_button;
	
	 @FXML
	private TextArea text_dispute;
	
	@FXML
	private AnchorPane contentArea;

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

	public void backOnAction(ActionEvent event) {
		try { // Close the
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the appropriate dashboard based on callerType
			String fxmlFile = callerType.equals("buyer") ? "/views/buyer_dashboard.fxml"
					: "/views/seller_dashboard.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

			// Create a new stage for the dashboard
			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
			Scene scene = new Scene(root, 810, 620);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void submitOnAction(ActionEvent event) {
		// what happens when change password button is clicked
		String text = text_dispute.getText();
	}

}
