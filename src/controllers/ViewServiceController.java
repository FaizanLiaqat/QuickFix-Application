package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Service;

public class ViewServiceController {
    @FXML
    private TableView<Service> tableView;
    @FXML
    private TableColumn<Service, Integer> idColumn;
    @FXML
    private TableColumn<Service, String> nameColumn;
    @FXML
    private TableColumn<Service, Double> priceColumn;
    @FXML
    private TableColumn<Service, Integer> ratingColumn;
    @FXML
    private TableColumn<Service, Button> removeItColumn;
    
    @FXML
    private Label messageLabel; 

    @FXML
    private Button addaservice;
    
    @FXML
    private void handleAddServiceButtonClick() {
        try {
            // Load the new window (FXML file) for adding a service
        	// Load the Access.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/views/AddServiceWindow.fxml"));

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
            e.printStackTrace(); // Handle any loading or IO errors
        }
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    @FXML
    public void initialize() {
        // Ensure the table columns are correctly initialized
        if (idColumn != null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("serviceID"));
        } else {
            System.err.println("idColumn is null!");
        }

        if (nameColumn != null) {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        } else {
            System.err.println("nameColumn is null!");
        }

        if (priceColumn != null) {
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        } else {
            System.err.println("priceColumn is null!");
        }

        if (ratingColumn != null) {
            ratingColumn.setCellValueFactory(new PropertyValueFactory<>("serviceRating"));
        } else {
            System.err.println("ratingColumn is null!");
        }

        // Set the "Remove" button column's cell factory
        if (removeItColumn != null) {
            removeItColumn.setCellFactory(new Callback<TableColumn<Service, Button>, TableCell<Service, Button>>() {
                @Override
                public TableCell<Service, Button> call(TableColumn<Service, Button> param) {
                    return new TableCell<Service, Button>() {
                        private final Button removeButton = new Button("Remove");

                        @Override
                        protected void updateItem(Button item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                // Handle the "Remove" button action
                                removeButton.setOnAction(event -> {
                                    // Get the service associated with the current row
                                    Service service = getTableView().getItems().get(getIndex());
                                    System.out.println("Removing Service: " + service.getServiceName());

                                    // Remove the service from the TableView
                                    getTableView().getItems().remove(service);
                                });
                                setGraphic(removeButton);
                            }
                        }
                    };
                }
            });
        } else {
            System.err.println("removeItColumn is null!");
        }

        // Ensure the TableView is not null before setting items
        if (tableView != null) {
            // Set the TableView items (e.g., from a sample or your actual data source)
            // For example, set a list of services from a model or a service method:
            tableView.setItems(Service.getSampleServices());
        } else {
            System.err.println("tableView is null!");
        }
    }
}
