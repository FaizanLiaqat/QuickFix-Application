package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class WorkerController implements Initializable {

	@FXML
	private GridPane grid;

	@FXML
	private ScrollPane scroll;

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
	            if (service.getServiceName().trim().equalsIgnoreCase(type.trim())) {
	                FXMLLoader fxmlLoader = new FXMLLoader();
	                fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
	                Pane pane = fxmlLoader.load();

	                ItemController itemController = fxmlLoader.getController();
	                itemController.setData(service);
	                
	                // Add click event for the pane
	                pane.setOnMouseClicked(event -> {
	                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	                    alert.setTitle("Booking Confirmation");
	                    alert.setHeaderText(null);
	                    alert.setContentText("Do you want to book it?");

	                    Optional<ButtonType> result = alert.showAndWait();
	                    if (result.isPresent() && result.get() == ButtonType.OK) {
	                        System.out.println("Booking confirmed for: " + service.getServiceName());
	                        // Add booking logic here
	                    } else {
	                        System.out.println("Booking canceled for: " + service.getServiceName());
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
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


}
