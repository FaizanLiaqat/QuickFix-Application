package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.BookingDAO;
import dao.DatabaseConnection;
import dao.ServiceDAO;
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
import models.Booking;
import models.Payment;
import models.Service;
import models.User;
import utils.UserSingleton;

public class paymenthistoryController implements Initializable {

	@FXML
	private Button back_button;

	private String callerType = "buyer"; // Field to track the caller
    @FXML
    private GridPane grid1;


    


    @FXML
    private ScrollPane scroll1;


    

	private List<Payment> payments = new ArrayList<>();
	
	private User user; // Declare a user object to store the current user


	// Method to set the caller type
	public void setCallerType(String callerType) {
		this.callerType = callerType;
	}

	private List<Payment> getData(int id) throws SQLException {

		List<Payment> payments = new ArrayList<>();

		
	
		
		dao.CreditCardPaymentDAO pcred = new dao.CreditCardPaymentDAO();
		dao.BankTransferPaymentDAO pcred2 = new dao.BankTransferPaymentDAO();
		
		
		Map<Integer, Payment> creditCardPayments = pcred.getPaymentsBySenderID(id);
		
		payments.addAll(creditCardPayments.values());



		// Fetch payments from bank transfer DAO and add them to the payments list
		Map<Integer, Payment> bankTransferPayments = pcred2.getPaymentsBySenderID(id);
		payments.addAll(bankTransferPayments.values());
		
//		
		
//	    BookingDAO bdao = new BookingDAO(); // Initialize the ServiceDAO object
//		
//	    try {
//	        // Call the filterByLocation method and pass the location as an argument
//	    	booking = bdao.getBookingsByStatus(status,id);
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	    
////		services.add(new Service(1, "Plumber", "Full house plumbing service", 2.99, 1, 5));
////		services.add(new Service(2, "Electrician", "Basic electrical repair services", 3.99, 2, 4));
////		services.add(new Service(3, "Carpenter", "Carpentry and furniture repair", 1.50, 3, 5));
	return payments;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		this.user = UserSingleton.getInstance().getUserObject();
		
		System.out.println(this.user.getUserID());
		//pending , completed, failed.
	    List<Payment> pendingPayments = null;
		try {
			pendingPayments = getData(user.getUserID());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Load Pending bookings
	    // Load Pending bookings
	   // List<Booking> confirmedBookings = getData("COnfirmed" , user.getUserID()); // Load Completed bookings
	    

	    int column1 = 0;
	    int row1 = 1;

	    int column2 = 0;
	    int row2 = 1;

	    try {
	        // Loop for Pending bookings and populate grid1
	        for (Payment payment : pendingPayments) {
	            FXMLLoader fxmlLoader = new FXMLLoader();
	            fxmlLoader.setLocation(getClass().getResource("/views/payment_item.fxml"));
	            Pane pane = fxmlLoader.load();

	            PaymentItemController itemController = fxmlLoader.getController();
	            itemController.setData(payment); // Set data for Pending booking


	            if (column1 == 1) {
	                column1 = 0;
	                row1++;

	            }
	            grid1.add(pane, column1++, row1); // Add pane to grid1
	            GridPane.setMargin(pane, new Insets(10));
	        }

	        

	        // Set grid dimensions
	        setGridDimensions(grid1);
//	        setGridDimensions(grid2);
//	        setGridDimensions(grid3);
	        //setGridDimensions(grid4);


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
			stage.initStyle(StageStyle.DECORATED);
			Scene scene = new Scene(root, 810, 620);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
