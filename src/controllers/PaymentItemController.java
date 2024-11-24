package controllers;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Payment;
import models.Service;

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
		
		paymentId.setText(String.valueOf(payments.getPaymentID()));
		sellerName.setText("john");
		serviceName.setText("Mechanic");
		amountPaid.setText(payments.getAmount()+ " PKR");

	    // Image loading logic
	    
	}


}