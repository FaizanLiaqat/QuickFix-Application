package controllers;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.AlertUtils;
import utils.UserSingleton;

public class ChangeAdminPasswordController {
	@FXML
	private Button adminChangePassword;

	@FXML
	private TextField adminConfirmPassword;

	@FXML
	private TextField adminNewPassword;

	@FXML
	private TextField adminOldPassword;
	@FXML
	private Label changePasswordMessage;

	@FXML

	void changePasswordOnAction(ActionEvent event) {
		if (adminOldPassword.getText().isBlank() == false && adminNewPassword.getText().isBlank() == false
				&& adminConfirmPassword.getText().isBlank() == false) {
			if (adminNewPassword.getText().equalsIgnoreCase(adminConfirmPassword.getText())) {
				UpdateAdminPassword();
			} else {
				changePasswordMessage.setText("New And Confrimed Password Don't Match");
			}

		} else {
			changePasswordMessage.setText("Please Fill All the Text Fields");
		}
	}

	private void UpdateAdminPassword() {
		dao.UserDAO adminDao = new dao.AdminDAO();
		try {
			UserSingleton.getInstance().getUserObject().setUserPassword(adminOldPassword.getText());
			if (adminDao.exists(UserSingleton.getInstance().getUserObject()) != -1) {
				System.out.println("Old Password "+UserSingleton.getInstance().getUserObject().getUserPassword());
				UserSingleton.getInstance().getUserObject().setUserPassword(adminConfirmPassword.getText());
				adminDao.update(UserSingleton.getInstance().getUserObject());
				System.out.println("New Password "+UserSingleton.getInstance().getUserObject().getUserPassword());
				
			}else {
				AlertUtils.showError("Change Password Faild",  "Invalid password");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
