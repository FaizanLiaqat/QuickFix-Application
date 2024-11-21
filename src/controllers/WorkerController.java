package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import models.Service;

public class WorkerController {

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
	private TableColumn<Service, Button> bookItColumn;
	
	 @FXML
	    private Label messageLabel; 

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

	        // Set the "Book It" button column's cell factory
	        if (bookItColumn != null) {
	            bookItColumn.setCellFactory(new Callback<TableColumn<Service, Button>, TableCell<Service, Button>>() {
	                @Override
	                public TableCell<Service, Button> call(TableColumn<Service, Button> param) {
	                    return new TableCell<Service, Button>() {
	                        private final Button bookButton = new Button("Book it");

	                        @Override
	                        protected void updateItem(Button item, boolean empty) {
	                            super.updateItem(item, empty);
	                            if (empty) {
	                                setGraphic(null);
	                            } else {
	                                // Handle button action
	                                bookButton.setOnAction(event -> {
	                                    Service service = getTableView().getItems().get(getIndex());
	                                    System.out.println("Booked Service: " + service.getServiceName());
	                                    openPopupWindow(service);
	                                });
	                                setGraphic(bookButton);
	                            }
	                        }
	                    };
	                }
	            });
	        } else {
	            System.err.println("bookItColumn is null!");
	        }

	        // Ensure the TableView is not null before setting items
	        if (tableView != null) {
	            tableView.setItems(Service.getSampleServices());
	        } else {
	            System.err.println("tableView is null!");
	        }
	    }
	    private void openPopupWindow(Service service) {
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
}
