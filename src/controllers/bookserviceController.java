package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class bookserviceController implements Initializable {

	@FXML
	private Button back_button;
	
	@FXML
	private StackPane contentArea;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try {
			Parent fxml  = FXMLLoader.load(getClass().getResource("/views/viewHistory.fxml"));
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(fxml);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ViewHistory(javafx.event.ActionEvent actionEvent) throws IOException{
		Parent fxml  = FXMLLoader.load(getClass().getResource("/views/viewHistory.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(fxml);
		
	}
	
	public void loadWorkerView(String message) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Worker.fxml"));
	    Parent fxml = loader.load();

	    // Get the controller and pass the message
	    WorkerController controller = loader.getController();
	    controller.setMessage(message);

	    contentArea.getChildren().removeAll();
	    contentArea.getChildren().setAll(fxml);
	}

	public void Plumber(javafx.event.ActionEvent actionEvent) throws IOException {
	    loadWorkerView("Plumber");
	}

	public void Electrician(javafx.event.ActionEvent actionEvent) throws IOException {
	    loadWorkerView("Electrician");
	}

	public void Mechanic(javafx.event.ActionEvent actionEvent) throws IOException {
	    loadWorkerView("Mechanic");
	}

	public void Carpenter(javafx.event.ActionEvent actionEvent) throws IOException {
	    loadWorkerView("Carpenter");
	}
	

	public void backButtonOnAction(ActionEvent event) {
		try {
			// Close the current window
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the appropriate dashboard based on callerType
			Parent root = FXMLLoader.load(getClass().getResource("/views/buyer_dashboard.fxml"));
			// Create a new stage for the dashboard
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			Scene scene = new Scene(root, 520, 400);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
