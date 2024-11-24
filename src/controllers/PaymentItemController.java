package controllers;

import java.io.File;
import java.sql.SQLException;

import dao.BookingDAO;
import dao.SellerDAO;
import dao.ServiceDAO;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Booking;
import models.Payment;
import models.Service;
import models.User;
import utils.UserSingleton;

public class PaymentItemController {

	@FXML
	private Label paymentId;

	@FXML
	private Label sellerName;

	@FXML
	private Label serviceName;

	@FXML
	private Label amountPaid;

	

	public void setData(Payment payments) {
	    // Set text for various labels
		

		UserDAO sellerdao = new SellerDAO();
		User seller=null ;
		BookingDAO bookingdao = new BookingDAO();
		Booking booking;
		ServiceDAO servicedao = new ServiceDAO();
		Service service =null;
		
		System.out.println("Displaying ---- Payment ID: " + payments.getPaymentID());
		try {
			 seller = sellerdao.get(payments.getReceiverID());
			 booking = bookingdao.get(payments.getBookingID());
			 service = servicedao.get(booking.getServiceID());
			 if(seller!=null)
				 System.out.println(seller.getUserID()+" " + seller.getUserName());
			 if(service!=null)
				 System.out.println(service.getServiceID()+" "+service.getServiceName());
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		if(service!=null && seller!=null) {
			paymentId.setText(String.valueOf(payments.getPaymentID()));
			sellerName.setText(seller.getUserName());
			serviceName.setText(service.getServiceName());
			amountPaid.setText(payments.getAmount()+ " PKR");
		}

	    // Image loading logic
	    
	}


}