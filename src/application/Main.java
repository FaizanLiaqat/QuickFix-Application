package application;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.stage.*;
import javafx.scene.*;

/**
 * 
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setTitle("QuickFix");
			primaryStage.setScene(new Scene(root, 520, 400));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
