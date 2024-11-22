package application;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.stage.*;
import javafx.scene.*;

/**
 * 
 */
public class Main extends Application {
	
	double x,y;
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/buyer_dashboard.fxml"));
			root.setOnMousePressed(event->{
				x = event.getSceneX();
				y = event.getSceneY();
			});
			root.setOnMouseDragged(event->{
				primaryStage.setX(event.getSceneX() - x);
				primaryStage.setY(event.getSceneY() - y);
			});
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
