package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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

public class viewHistoryController implements Initializable {

	@FXML
	private Button back_button;

	private String callerType = "buyer"; // Field to track the caller

	@FXML
	private GridPane grid1;

	@FXML
	private GridPane grid2;

	@FXML
	private ScrollPane scroll1;

	@FXML
	private ScrollPane scroll2;

	private List<Service> services = new ArrayList<>();

	// Method to set the caller type
	public void setCallerType(String callerType) {
		this.callerType = callerType;
	}

	private List<Service> getData() {

		List<Service> services = new ArrayList<>();
		services.add(new Service(1, "Plumber", "Full house plumbing service", 2.99, 1, 5));
		services.add(new Service(2, "Electrician", "Basic electrical repair services", 3.99, 2, 4));
		services.add(new Service(3, "Carpenter", "Carpentry and furniture repair", 1.50, 3, 5));
		return services;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		services.addAll(getData()); // Load all services

		int column1 = 0;
		int row1 = 1;

		int column2 = 0;
		int row2 = 1;

		try {
			for (Service service : services) {
				// Load and configure Pane for grid1
				FXMLLoader fxmlLoader1 = new FXMLLoader();
				fxmlLoader1.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane1 = fxmlLoader1.load();

				ItemController itemController1 = fxmlLoader1.getController();
				itemController1.setData(service);

				if (column1 == 1) {
					column1 = 0;
					row1++;
				}
				grid1.add(pane1, column1++, row1);

				// Load and configure Pane for grid2
				FXMLLoader fxmlLoader2 = new FXMLLoader();
				fxmlLoader2.setLocation(getClass().getResource("/views/item.fxml"));
				Pane pane2 = fxmlLoader2.load();

				ItemController itemController2 = fxmlLoader2.getController();
				itemController2.setData(service);

				if (column2 == 1) {
					column2 = 0;
					row2++;
				}
				grid2.add(pane2, column2++, row2);

				// Set grid dimensions
				setGridDimensions(grid1);
				setGridDimensions(grid2);

				GridPane.setMargin(pane1, new Insets(10));
				GridPane.setMargin(pane2, new Insets(10));
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

	public void backButtonOnAction(ActionEvent event) {
		try {
			// Close the current window
			Stage currentStage = (Stage) back_button.getScene().getWindow();
			currentStage.close();

			// Load the appropriate dashboard based on callerType
			String fxmlFile = callerType.equals("buyer") ? "/views/buyer_dashboard.fxml"
					: "/views/seller_dashboard.fxml";
			Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

			// Create a new stage for the dashboard
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			Scene scene = new Scene(root, 520, 400);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
