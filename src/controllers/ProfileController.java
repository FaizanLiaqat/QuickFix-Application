package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Booking;
import models.User;
import utils.UserSingleton;

public class ProfileController {
	@FXML
	private Label profile_name;
	
	@FXML
	private Label profile_email;
	
	@FXML
	private Label profile_phone;
	
	
	@FXML
	private Label profile_location;
	
	@FXML
	private Label profile_services;
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
//				//fetch data from database;
//		
//			profile_name.setText("testing");
//			
//			System.out.println("printing profile");
		}
	
	public void setData() {
		User u = UserSingleton.getInstance().getUserObject();
		dao.BookingDAO bookingdao = new dao.BookingDAO();
		
		profile_name.setText(u.getUserName());
		
		profile_email.setText(u.getUserEmail());

		profile_phone.setText(u.getUserPhoneNumber());

		profile_location.setText(u.getUserLocation());
		
		List<Booking> bookings = null;
		try {
			bookings = bookingdao.getAllByBuyerID(u.getUserID());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int numberOfBookings = bookings.size();
		
		profile_services.setText(String.valueOf(numberOfBookings));

		
	}
	
	

	
	
}
