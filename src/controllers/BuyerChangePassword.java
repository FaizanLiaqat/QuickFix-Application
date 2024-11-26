package controllers;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.AlertUtils;
import utils.UserSingleton;


public class BuyerChangePassword {

	@FXML
	private TextField confirm_password;

	@FXML
	private TextField new_password;

	@FXML
	private TextField old_password;
	@FXML
	private Label changePasswordMessage;
	
	public void changePasswordOnAction(ActionEvent event) {
		if (old_password.getText().isBlank() == false && new_password.getText().isBlank() == false
				&& confirm_password.getText().isBlank() == false) {
			if (new_password.getText().equalsIgnoreCase(confirm_password.getText())) {
				UpdateBuyerPassword();
			} else {
				changePasswordMessage.setText("New And Confrimed Password Don't Match");
			}

		} else {
			changePasswordMessage.setText("Please Fill All the Text Fields");
		}
	}

	private void UpdateBuyerPassword() {
		dao.UserDAO buyerdao = new dao.BuyerDAO();
		try {
			UserSingleton.getInstance().getUserObject().setUserPassword(old_password.getText());
			if (buyerdao.exists(UserSingleton.getInstance().getUserObject()) != -1) {
				System.out.println("Old Password "+UserSingleton.getInstance().getUserObject().getUserPassword());
				UserSingleton.getInstance().getUserObject().setUserPassword(confirm_password.getText());
				buyerdao.update(UserSingleton.getInstance().getUserObject());
				System.out.println("New Password "+UserSingleton.getInstance().getUserObject().getUserPassword());
				
			}else {
				AlertUtils.showError("Change Password Faild",  "Invalid password");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
