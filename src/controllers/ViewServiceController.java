package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Service;

public class ViewServiceController implements Initializable {

	@FXML
	private GridPane grid;

	@FXML
	private ScrollPane scroll;
	
	@FXML
	private Button create_button;
	
	@FXML
	private Button back_button;

	private List<Service> services = new ArrayList<>();

	private String type;

	public void setMessage(String message) {
		type = message;
		System.out.println(type);
	}

	private List<Service> getData() {

		List<Service> services = new ArrayList<>();
		services.add(new Service(1, "Plumber", "Full house plumbing service", 2.99, 1, 5));
		services.add(new Service(2, "Electrician", "Basic electrical repair services", 3.99, 2, 4));
		services.add(new Service(3, "Carpenter", "Carpentry and furniture repair", 1.50, 3, 5));
		services.add(new Service(4, "Mechanic", "Vehicle repair and maintenance", 0.99, 4, 3));
		services.add(new Service(5, "Plumber", "Professional plumbing service", 4.99, 5, 4));
		services.add(new Service(6, "Electrician", "Electrical repair and maintenance", 2.99, 6, 5));
		services.add(new Service(7, "Carpenter", "Woodwork and custom carpentry", 0.99, 7, 4));
		services.add(new Service(8, "Mechanic", "Comprehensive vehicle repair", 0.99, 8, 3));
		services.add(new Service(9, "Plumber", "Emergency plumbing services", 0.99, 9, 5));
		services.add(new Service(10, "Electrician", "Residential and commercial electrical work", 1.99, 10, 4));

		services.add(new Service(11, "Plumber", "Full house plumbing service", 2.99, 1, 5));
		services.add(new Service(12, "Electrician", "Basic electrical repair services", 3.99, 2, 4));
		services.add(new Service(13, "Carpenter", "Carpentry and furniture repair", 1.50, 3, 5));
		services.add(new Service(14, "Mechanic", "Vehicle repair and maintenance", 0.99, 4, 3));
		services.add(new Service(15, "Plumber", "Professional plumbing service", 4.99, 5, 4));
		services.add(new Service(16, "Electrician", "Electrical repair and maintenance", 2.99, 6, 5));
		services.add(new Service(17, "Carpenter", "Woodwork and custom carpentry", 0.99, 7, 4));
		services.add(new Service(18, "Mechanic", "Comprehensive vehicle repair", 0.99, 8, 3));
		services.add(new Service(19, "Plumber", "Emergency plumbing services", 0.99, 9, 5));
		services.add(new Service(20, "Electrician", "Residential and commercial electrical work", 1.99, 10, 4));

		return services;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		services.addAll(getData()); // Load all services

		int column = 0;
		int row = 1;

		try {
			for (Service service : services) {

				// Trim and compare names

				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(service);

				// Add click event for the pane
				pane.setOnMouseClicked(event -> {
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Delete Service");
					alert.setHeaderText(null);
					alert.setContentText("Do you want to delete this service?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.OK) {
						services.remove(service); // Remove the service from the list
						refreshServices(); // Refresh the grid
						System.out.println("Service removed: " + service.getServiceName());
					} else {
						System.out.println("Service deletion canceled.");
					}
				});

				if (column == 1) {
					column = 0;
					row++;
				}

				grid.add(pane, column++, row); // Add to grid at (column, row)

				// Set grid dimensions
				grid.setMinWidth(Region.USE_COMPUTED_SIZE);
				grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
				grid.setMaxWidth(Region.USE_PREF_SIZE);

				grid.setMinHeight(Region.USE_COMPUTED_SIZE);
				grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
				grid.setMaxHeight(Region.USE_PREF_SIZE);

				GridPane.setMargin(pane, new Insets(10));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Event handler to add a service
	public void addButton(ActionEvent event) {
		try {
			// Load the Access.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/views/AddServiceWindow.fxml"));

			// Create a new Stage (window) for Access.fxml
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED); // Make the window undecorated (no borders or title bar)

			// Set the new scene with the loaded FXML and desired size
			Scene scene = new Scene(root, 520, 400); // Set dimensions similar to your original configuration
			stage.setScene(scene);

			// Show the new window (stage)
			stage.show();

			// Optional: Refresh the grid after adding a service
			//refreshServices();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Helper to refresh the grid after adding/removing a service
	private void refreshServices() {
		grid.getChildren().clear();
		int column = 0;
		int row = 1;

		for (Service service : services) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(service);

				// Add click handler for remove
				pane.setOnMouseClicked(event -> {
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Delete Service");
					alert.setHeaderText(null);
					alert.setContentText("Do you want to delete this service?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.OK) {
						services.remove(service); // Remove the service from the list
						refreshServices(); // Refresh the grid
						System.out.println("Service removed: " + service.getServiceName());
					} else {
						System.out.println("Service deletion canceled.");
					}
				});

				if (column == 1) {
					column = 0;
					row++;
				}

				grid.add(pane, column++, row);
				GridPane.setMargin(pane, new Insets(10));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createService(javafx.event.ActionEvent event) {

			// Close the current window (home.fxml)
			Stage currentStage = (Stage) create_button.getScene().getWindow();
			currentStage.close();

			
		
	}
	
	public void backButtonOnAction(javafx.event.ActionEvent event) {
		try {

			// Close the current window (home.fxml)
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/views/seller_dashboard.fxml"));

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
