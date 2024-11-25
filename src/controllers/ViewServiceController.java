package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.ServiceDAO;
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
import models.User;
import utils.UserSingleton;

public class ViewServiceController implements Initializable {

	@FXML
	private GridPane grid;

	@FXML
	private ScrollPane scroll;

	@FXML
	private Button create_button;
	

	@FXML
	private Button add_button;
	
	@FXML
	private Button back_button;

	private List<Service> services = new ArrayList<>();

	private String type;

	private User user; // Declare a user object to store the current user

	public void setMessage(String message) {
		type = message;
		System.out.println(type);
	}

	private List<Service> getData(int id) {

		List<Service> services = new ArrayList<>();
		ServiceDAO sdao = new ServiceDAO(); // Initialize the ServiceDAO object

		try {
			// Call the filterByLocation method and pass the location as an argument
			services = sdao.getAllBySellerID(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return services;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Retrieve the current user from the UserSingleton
		this.user = UserSingleton.getInstance().getUserObject();
		services.addAll(getData(user.getUserID())); // Load all services

		int column = 0;
		int row = 1;

		try {
			for (Service service : services) {

				// Trim and compare names

				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(service , user);

				// Add click event for the pane
				pane.setOnMouseClicked(event -> {
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Delete Service");
					alert.setHeaderText(null);
					alert.setContentText("Do you want to delete this service?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.OK) {
						ServiceDAO sdao = new ServiceDAO(); // Initialize the ServiceDAO object

						try {
							// Call the filterByLocation method and pass the location as an argument
							System.out.println(sdao.delete(service));
						} catch (SQLException e) {
							e.printStackTrace();
						}

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
			
			Stage stage1 = (Stage) add_button.getScene().getWindow();
			stage1.close();
			
			// Load the Access.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/views/AddServiceWindow.fxml"));

			// Create a new Stage (window) for Access.fxml
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED); // Make the window undecorated (no borders or title bar)

			// Set the new scene with the loaded FXML and desired size
			Scene scene = new Scene(root, 810, 620); // Set dimensions similar to your original configuration
			stage.setScene(scene);

			// Show the new window (stage)
			stage.show();

			// Optional: Refresh the grid after adding a service
			// refreshServices();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Helper to refresh the grid after adding/removing a service
	private void refreshServices() {
		
	    services.clear();
		grid.getChildren().clear();

		// Retrieve the current user from the UserSingleton
		this.user = UserSingleton.getInstance().getUserObject();
		services.addAll(getData(user.getUserID())); // Load all services

		int column = 0;
		int row = 1;

		for (Service service : services) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(service, user);

				// Add click handler for remove
				pane.setOnMouseClicked(event -> {
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Delete Service");
					alert.setHeaderText(null);
					alert.setContentText("Do you want to delete this service?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.OK) {
						ServiceDAO sdao = new ServiceDAO(); // Initialize the ServiceDAO object

						try {
							// Call the filterByLocation method and pass the location as an argument
							System.out.println(sdao.delete(service));
						} catch (SQLException e) {
							e.printStackTrace();
						}

						refreshServices(); // Refresh the grid						System.out.println("Service removed: " + service.getServiceName());
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
			Scene scene = new Scene(root, 810, 620); // Set dimensions similar to your original configuration
			stage.setScene(scene);

			// Show the new window (stage)
			stage.show();

		} catch (IOException e) {
			e.printStackTrace(); // Handle error if FXML file loading fails
		}
	}
}
