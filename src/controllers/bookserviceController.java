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
		// Create the FXMLLoader and set the controller manually
		WorkerController controller = new WorkerController();
		controller.setMessage(message); // Call setMessage before loading FXML

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Worker.fxml"));
		loader.setController(controller); // Set the controller manually

		Parent fxml = loader.load(); // Load the FXML

		// Update the content area
		contentArea.getChildren().clear();
		contentArea.getChildren().add(fxml);
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
	

	public void backButtonOnAction(javafx.event.ActionEvent event) {
		try {

			// Close the current window (home.fxml)
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/views/buyer_dashboard.fxml"));

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
