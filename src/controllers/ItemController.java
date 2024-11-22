package controllers;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Service;

public class ItemController {

	@FXML
	private Label DescriptionLabel;

	@FXML
	private Label PriceLabel;

	@FXML
	private Label Ratinglabel;

	@FXML
	private Label SellernameLabel;

	@FXML
	private ImageView img;

	@FXML
	private Label nameLabel;

	public void setData(Service services) {
	    // Set text for various labels
	    nameLabel.setText(services.getServiceName());
	    DescriptionLabel.setText(services.getServiceDescription());
	    Ratinglabel.setText("Service Rating: " + services.getServiceRating());
	    PriceLabel.setText(services.getServicePrice() + " PKR");
	    SellernameLabel.setText("Data to be fetched");

	    // Image loading logic
	    String imagePath;
	    if (services.getServiceName().equals("Mechanic")) {
	        imagePath = "file:/QuickFixv2/src/media/mechanic_logo.png";
	    } else if (services.getServiceName().equals("Plumber")) {
	        imagePath = "file:/QuickFixv2/src/media/Plumber_logo.png";
	    } else if (services.getServiceName().equals("Carpenter")) {
	        imagePath = "file:/QuickFixv2/src/media/Carpenter_logo.png";
	    } else {
	        imagePath = "file:/QuickFixv2/src/media/Electrician_logo.jpg";
	    }

	    File imageFile = new File(imagePath);

	    // Check if the image file exists
	    if (!imageFile.exists()) {
	        System.out.println("Image file does not exist at path: " + imagePath);
	    } else {
	        try {
	            Image image = new Image(imagePath);
	            img.setImage(image);
	        } catch (Exception e) {
	            System.out.println("Error loading image: " + e.getMessage());
	        }
	    }
	}


}