package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import models.Dispute;

public class ResolvedDisputeController implements Initializable {

	@FXML
	private GridPane grid1;

	@FXML
	private ScrollPane scroll1;

	private List<Dispute> getData() throws SQLException {
		List<Dispute> disputeResolved = new ArrayList<>();
		List<Dispute> disputeRejected = new ArrayList<>();
		List<Dispute> mergedDisputes = new ArrayList<>();

		dao.DisputeDAO disdao = new dao.DisputeDAO(); // Initialize the DisputeDAO object

		try {
			// Fetch disputes by status
			disputeResolved = disdao.getDisputesByStatus("Resolved");
			disputeRejected = disdao.getDisputesByStatus("Rejected");

			// Merge the two lists
			mergedDisputes.addAll(disputeResolved); // Add resolved disputes
			mergedDisputes.addAll(disputeRejected); // Add rejected disputes

		} catch (Exception e) {
			System.err.println("Unexpected error: " + e.getMessage());
			e.printStackTrace();
		}

		// Check if the merged list is empty
		if (mergedDisputes.isEmpty()) {
			System.out.println("No disputes found for the given statuses.");
		}

		return mergedDisputes; // Return the merged list
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Dispute> disputeAll = null;
		try {
			disputeAll = getData();
			System.out.println(disputeAll.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int column1 = 0;
		int row1 = 1;

		try {
			// Loop for Pending bookings and populate grid1
			for (Dispute dispute : disputeAll) {
				System.out.println(dispute.getDisputeStatus());
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/DisputeResolvedCards.fxml"));
				Pane pane = fxmlLoader.load();

				DisputeResolvedCardsController disputeController = fxmlLoader.getController();
				disputeController.setData(dispute); // Set data for Pending booking

				if (column1 == 1) {
					column1 = 0;
					row1++;
				}
				grid1.add(pane, column1++, row1); // Add pane to grid1
				GridPane.setMargin(pane, new Insets(10));
				setGridDimensions(grid1);
			}

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
