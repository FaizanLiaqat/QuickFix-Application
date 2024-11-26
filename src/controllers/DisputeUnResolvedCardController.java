package controllers;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import dao.BookingDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import models.Booking;
import models.Dispute;
import models.Notification;
import models.Service;
import models.User;
import utils.AlertUtils;

public class DisputeUnResolvedCardController {
	@FXML
	private Label buyerName;
	@FXML
	private Label serviceName;
	@FXML
	private Label SellernameLabel;
	@FXML
	private Label DescriptionLabel;
	@FXML
	private Button disputeResolve;
	@FXML
	private Button disputeReject;
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

	}

	public void disputeResolveAction() {
		// Resolved
		dao.DisputeDAO disputeDao = new dao.DisputeDAO();
		Dispute dispute = disputeDao.get(this.disputeID);
		dispute.setDisputeStatus("Resolved");
		disputeDao.update(dispute);

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
		dao.NotificationDAO notificationdao = new dao.NotificationDAO();
		// int recipientID, String notificationMessage, java.sql.Timestamp timestamp,
		// String status, String type, String recipientRole
		if (buyer != null && seller != null && service != null) {
			Notification notifyBuyer = new Notification(dispute.getBuyerID(),
					buyer.getUserName() + " Your Dispute has been Accepted in your favor against Seller: "
							+ seller.getUserName() + " on Service: " + service.getServiceName(),
					new Timestamp(new Date().getTime()), "Unread", "Dispute", "Buyer");
			int a = notificationdao.insert(notifyBuyer);
			System.out.println(a);
			Notification notifySeller = new Notification(dispute.getSellerID(),
					seller.getUserName() + " The dispute against you by Client: "
							+ buyer.getUserName() + " on Service: " + service.getServiceName()+" Has been Accepted.",
					new Timestamp(new Date().getTime()), "Unread", "Dispute", "Seller");
			int id = notificationdao.insert(notifySeller);
			System.out.println(id);

		}
		utils.AlertUtils.showSuccess("Dispute Resolved Successfully!");

	}

	public void disptueRejectAction() {
		// Resolved
		dao.DisputeDAO disputeDao = new dao.DisputeDAO();
		Dispute dispute = disputeDao.get(this.disputeID);
		dispute.setDisputeStatus("Rejected");
		disputeDao.update(dispute);
		
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
		dao.NotificationDAO notificationdao = new dao.NotificationDAO();
		// int recipientID, String notificationMessage, java.sql.Timestamp timestamp,
		// String status, String type, String recipientRole
		if (buyer != null && seller != null && service != null) {
			Notification notifyBuyer = new Notification(dispute.getBuyerID(),
					buyer.getUserName() + " Your Dispute has been Rejected against Seller: "
							+ seller.getUserName() + " on Service: " + service.getServiceName(),
					new Timestamp(new Date().getTime()), "Unread", "Dispute", "Buyer");
			int a = notificationdao.insert(notifyBuyer);
			System.out.println(a);
			Notification notifySeller = new Notification(dispute.getSellerID(),
					seller.getUserName() + " The dispute against you by Client: "
							+ buyer.getUserName() + " on Service: " + service.getServiceName()+" Has been Rejected.",
					new Timestamp(new Date().getTime()), "Unread", "Dispute", "Seller");
			int id = notificationdao.insert(notifySeller);
			System.out.println(id);

		}
		utils.AlertUtils.showSuccess("Dispute Rejected Successfully!");
	}
}
