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
import models.Booking;
import models.Service;
import models.User;
import utils.UserSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.ServiceDAO;
import dao.BookingDAO;

public class WorkerController implements Initializable {

	@FXML
	private GridPane grid;

	@FXML
	private ScrollPane scroll;

	private List<Service> services = new ArrayList<>();

	private String type;

	private User user; // Declare a user object to store the current user

	public void setMessage(String message) {
		type = message;
		System.out.println(type);
	}

	private List<Service> getData(String location) {

		List<Service> services = new ArrayList<>();
		ServiceDAO sdao = new ServiceDAO(); // Initialize the ServiceDAO object

		try {
			// Call the filterByLocation method and pass the location as an argument
			services = sdao.filterByLocation(location);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return services;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Retrieve the current user from the UserSingleton
		this.user = UserSingleton.getInstance().getUserObject();

		services.addAll(getData(user.getUserLocation())); // Load all services

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
					itemController.setData(service, user);

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
