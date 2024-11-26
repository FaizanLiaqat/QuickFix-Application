package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.AdminDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Admin;
import models.CreditCardPayment;
import models.User;
import utils.UserSingleton;

public class AdminController implements Initializable {

	@FXML
	private Button unResolvedButton;

	@FXML
	private Button resolvedButton;

	@FXML
	private Button adminChangePassword;

	@FXML
	private Button adminQuit;

	@FXML
	private Label bookedServicesLabel;

	@FXML
	private Label Amount;

	@FXML
	private Pane admin_content_area;

	private String callerType; // Field to track the caller

	// Method to set the caller type
	public void setCallerType(String callerType) {
		this.callerType = callerType;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		AdminDAO adminDao = new AdminDAO();
		User admin = adminDao.get(UserSingleton.getInstance().getUserObject().getUserID());

		if (admin instanceof Admin) {
			Admin ccadmin = (Admin) admin;

			Amount.setText(ccadmin.getAmount());
		}

	}

	public void openUnResolvedView() throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/views/ViewUnResolvedDispute.fxml"));
		admin_content_area.getChildren().removeAll();
		admin_content_area.getChildren().setAll(fxml);
	}

	public void openResolvedView() throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/views/ViewResolvedDispute.fxml"));
		admin_content_area.getChildren().removeAll();
		admin_content_area.getChildren().setAll(fxml);
	}

	public void changeAdminPassword(javafx.event.ActionEvent actionEvent) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/views/ChangeAdminPassword.fxml"));
		admin_content_area.getChildren().removeAll();
		admin_content_area.getChildren().setAll(fxml);
	}

	public void adminQuitOnAction(ActionEvent event) {
		Stage stage = (Stage) adminQuit.getScene().getWindow();
		stage.close();
	}

}
