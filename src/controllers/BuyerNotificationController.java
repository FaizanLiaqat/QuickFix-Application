package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.BookingDAO;
import dao.NotificationDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import models.Booking;
import models.Notification;
import models.User;
import utils.UserSingleton;

public class BuyerNotificationController implements Initializable {

	@FXML
	private GridPane grid5;

	@FXML
	private ScrollPane scroll5;

	private User user; // Declare a user object to store the current user

	private List<Notification> getData(String type, int id) {

		List<Notification> notification = new ArrayList<>();

		NotificationDAO ndao = new NotificationDAO(); // Initialize the ServiceDAO object

		try {
			// Call the filterByLocation method and pass the location as an argument
			notification = ndao.getNotificationsByStatus(type, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notification;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.user = UserSingleton.getInstance().getUserObject();
		List<Notification> Dispute = getData("Dispute", user.getUserID()); // Load Pending bookings
		
		System.out.println("I am in Buyer Notification");
		int column1 = 0;
		int row1 = 1;

		try {

			for (Notification notification : Dispute) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(notification, user); // Set data for Pending booking

				if (column1 == 1) {
					column1 = 0;
					row1++;
				}
				grid5.add(pane, column1++, row1); // Add pane to grid1
				GridPane.setMargin(pane, new Insets(10));
			}

			// Set grid dimensions
			setGridDimensions(grid5);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Helper method to set grid dimensions
	private void setGridDimensions(GridPane grid) {
		grid.setMinWidth(Region.USE_COMPUTED_SIZE);
		grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
		grid.setMaxWidth(Region.USE_PREF_SIZE);

		grid.setMinHeight(Region.USE_COMPUTED_SIZE);
		grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
		grid.setMaxHeight(Region.USE_PREF_SIZE);
	}

}
