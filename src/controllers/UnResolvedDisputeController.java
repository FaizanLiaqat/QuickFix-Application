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
import models.Dispute;
import models.Notification;
import models.User;
import utils.UserSingleton;

public class UnResolvedDisputeController implements Initializable {

	@FXML
	private GridPane grid2;

	@FXML
	private ScrollPane scroll2;

	private List<Dispute> getData(String type) throws SQLException {

		List<Dispute> dispute = new ArrayList<>();

		dao.DisputeDAO disdao = new dao.DisputeDAO(); // Initialize the ServiceDAO object

		// Call the filterByLocation method and pass the location as an argument
		dispute = disdao.getDisputesByStatus(type);

		return dispute;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Dispute> disputeAll = null;
		try {
			disputeAll = getData("Open");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int column1 = 0;
		int row1 = 1;


		try {
			// Loop for Pending bookings and populate grid1
			for (Dispute dispute : disputeAll) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/DisputeCards.fxml"));
				Pane pane = fxmlLoader.load();

				DisputeUnResolvedCardController disputeController = fxmlLoader.getController();
				disputeController.setData(dispute); // Set data for Pending booking

				if (column1 == 1) {
					column1 = 0;
					row1++;
				}
				grid2.add(pane, column1++, row1); // Add pane to grid1
				GridPane.setMargin(pane, new Insets(10));
			}

			// Set grid dimensions


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}