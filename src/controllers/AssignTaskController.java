package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.BookingDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Booking;
import models.User;
import utils.UserSingleton;

public class AssignTaskController implements Initializable {

	@FXML
	private Button back_Button;
	

	@FXML
	private Button refresh;

	@FXML
	private GridPane grid1;

	@FXML
	private GridPane grid2;

	@FXML
	private GridPane grid3;

	@FXML
	private ScrollPane scrol1;

	@FXML
	private ScrollPane scroll2;

	@FXML
	private ScrollPane scroll3;

	private List<Booking> bookings = new ArrayList<>();

	private User user; // Declare a user object to store the current user

	private List<Booking> getData(String status, int id) {

		List<Booking> booking = new ArrayList<>();

		BookingDAO bdao = new BookingDAO(); // Initialize the ServiceDAO object

		try {
			// Call the filterByLocation method and pass the location as an argument
			booking = bdao.getBookingsByStatus2(status, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return booking;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loaddata();
	}

	public void loaddata() {

		this.user = UserSingleton.getInstance().getUserObject();
		List<Booking> Pending = getData("Pending", user.getUserID());
		List<Booking> Inprogress = getData("Confirmed", user.getUserID());
		List<Booking> Completed = getData("Completed", user.getUserID());

		Inprogress.size();
		Completed.size();

		int column1 = 0;
		int row1 = 1;

		int column2 = 0;
		int row2 = 1;

		try {

			for (Booking booking : Pending) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(booking, user); // Set data for Pending booking

				if (column1 == 1) {
					column1 = 0;
					row1++;
				}
				grid1.add(pane, column1++, row1); // Add pane to grid1
				GridPane.setMargin(pane, new Insets(10));
			}

			// Loop for Pending bookings and populate grid1
			for (Booking booking : Inprogress) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(booking, user); // Set data for Pending booking
			
				if (column1 == 1) {
					column1 = 0;
					row1++;
				}
				grid2.add(pane, column1++, row1); // Add pane to grid1
				GridPane.setMargin(pane, new Insets(10));
			}

			// Loop for Completed bookings and populate grid2
			for (Booking booking : Completed) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(booking, user); // Set data for Completed booking

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

	public void backButtonOnAction(javafx.event.ActionEvent event) {
		try {

			// Close the current window (home.fxml)
			Stage currentStage = (Stage) back_Button.getScene().getWindow();
			currentStage.close();

			// Load the Access.fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/views/seller_dashboard.fxml"));

			// Create a new Stage (window) for Access.fxml
			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED); // Make the window undecorated (no borders or title bar)
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
