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

public class NotificationController implements Initializable {

	@FXML
	private GridPane grid1;

	@FXML
	private GridPane grid2;

	@FXML
	private GridPane grid3;

	@FXML
	private ScrollPane scroll1;

	@FXML
	private ScrollPane scroll2;

	@FXML
	private ScrollPane scroll3;

	private User user; // Declare a user object to store the current user

	private List<Notification> getData(String type , int id) {

		List<Notification> notification = new ArrayList<>();

		NotificationDAO ndao = new NotificationDAO(); // Initialize the ServiceDAO object

		try {
			// Call the filterByLocation method and pass the location as an argument
			notification =  ndao.getNotificationsByStatus(type, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notification;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.user = UserSingleton.getInstance().getUserObject();

		List<Notification> BookingConfirmation = getData("BookingConfirmation", user.getUserID()); // Load Pending bookings
		List<Notification> PaymentStatus = getData("PaymentStatus", user.getUserID()); // Load Completed booking
		List<Notification> FeedbackReceived = getData("FeedbackReceived", user.getUserID()); // Load Pending bookings
		
		System.out.println(BookingConfirmation.size());
		int column1 = 0;
		int row1 = 1;

		int column2 = 0;
		int row2 = 1;

		try {
			// Loop for Pending bookings and populate grid1
			for (Notification notification : BookingConfirmation) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(notification); // Set data for Pending booking

				if (column1 == 1) {
					column1 = 0;
					row1++;
				}
				grid1.add(pane, column1++, row1); // Add pane to grid1
				GridPane.setMargin(pane, new Insets(10));
			}

			// Loop for Completed bookings and populate grid2
			for (Notification notification : PaymentStatus) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(notification); // Set data for Completed booking

				if (column2 == 1) {
					column2 = 0;
					row2++;
				}
				grid2.add(pane, column2++, row2); // Add pane to grid2
				GridPane.setMargin(pane, new Insets(10));
			}

			// Loop for Completed bookings and populate grid2
			for (Notification notification : FeedbackReceived) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(notification); // Set data for Completed booking

				// Add click event for the pane
				pane.setOnMouseClicked(event -> {
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Payment Confirmation");
					alert.setHeaderText("Payment Required");
					alert.setContentText("Do you want to proceed with the payment?");

					// Show the alert and wait for user response
					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.OK) {

						BookingDAO bdao = new BookingDAO(); // Initialize the ServiceDAO object
						 try{
							// Call the filterByLocation method and pass the location as an argument
							bdao.ChangepaymentStatus(notification.getNotificationID());
						} catch (SQLException e) {
							e.printStackTrace();
						}
						System.out.println("User chose to proceed with payment.");
					} else {
						System.out.println("User chose not to proceed with payment.");
					}
				});
				if (column2 == 1) {
					column2 = 0;
					row2++;
				}
				grid3.add(pane, column2++, row2); // Add pane to grid2
				GridPane.setMargin(pane, new Insets(10));
			}

			// Set grid dimensions
			setGridDimensions(grid1);
			setGridDimensions(grid2);
			setGridDimensions(grid3);

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
