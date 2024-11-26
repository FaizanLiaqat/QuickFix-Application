package controllers;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.Booking;
import models.Dispute;
import models.Service;
import models.User;

public class DisputeResolvedCardsController {
	private static final boolean True = false;
	@FXML
	private Label buyerName;
	@FXML
	private Label serviceName;
	@FXML
	private Label SellernameLabel;
	@FXML
	private Label DescriptionLabel;
	@FXML
	private Button disputedAction;
	private int disputeID;

	public void setData(Dispute dispute) {
		this.disputeID = dispute.getDisputeID();
		dao.UserDAO buyerDao = new dao.BuyerDAO(); // get buyer name
		dao.BookingDAO bookingDao = new dao.BookingDAO(); // get serivce id
		dao.ServiceDAO serviceDao = new dao.ServiceDAO(); // get service name
		dao.SellerDAO sellerDao = new dao.SellerDAO();

		User buyer = null;
		User seller = null;
		Booking booking = null;
		Service service = null;

		buyer = buyerDao.get(dispute.getBuyerID());
		booking = bookingDao.get(dispute.getBookingID());
		seller = sellerDao.get(dispute.getSellerID());
		if (booking != null) {
			service = serviceDao.get(booking.getServiceID());
		}

		if (buyer != null) {
			// Setting text for various labels
			buyerName.setText(buyer.getUserName()); // Sets recipient role
		}
		if (service != null) {

			serviceName.setText(service.getServiceName());
		}
		if (seller != null) {
			SellernameLabel.setText(seller.getUserName());
		}
		DescriptionLabel.setText(dispute.getDisputeReason()); // Sets notification message
		disputedAction.setText(dispute.getDisputeStatus());

		disputedAction.setDisable(true);
	}
}
