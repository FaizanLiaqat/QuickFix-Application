package controllers;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import dao.BookingDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import models.Booking;
import models.Dispute;
import models.Service;
import models.User;
import utils.AlertUtils;

public class DisputeUnResolvedCardController {
	@FXML private Label buyerName;
	@FXML private Label serviceName;
	@FXML private Label SellernameLabel;
	@FXML private Label DescriptionLabel;
	@FXML private Button disputeResolve;
	@FXML private Button disputeReject;
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
		
		try {
			buyer = buyerDao.get(dispute.getBuyerID());
		} catch (SQLException e) {

			e.printStackTrace();
		}
		try {
			booking = bookingDao.get(dispute.getBookingID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			seller = sellerDao.get(dispute.getSellerID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (booking != null) {
				service = serviceDao.get(booking.getServiceID());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (buyer != null && service != null && seller != null) {
			// Setting text for various labels
			buyerName.setText(buyer.getUserName()); // Sets recipient role
			serviceName.setText(service.getServiceName());
			SellernameLabel.setText(seller.getUserName());
			DescriptionLabel.setText(dispute.getDisputeReason()); // Sets notification message
		}
		
	}
	public void disputeResolveAction() {
		//Resolved
		dao.DisputeDAO disputeDao = new dao.DisputeDAO();
		Dispute dispute = disputeDao.get(this.disputeID);
		dispute.setDisputeStatus("Resolved");
		disputeDao.update(dispute);
		utils.AlertUtils.showSuccess("Dispute Resolved Successfully!");
		
	}
	public void disptueRejectAction() {
		//Resolved
		dao.DisputeDAO disputeDao = new dao.DisputeDAO();
		Dispute dispute = disputeDao.get(this.disputeID);
		dispute.setDisputeStatus("Rejected");
		disputeDao.update(dispute);
		utils.AlertUtils.showSuccess("Dispute Rejected Successfully!");
	}
}
