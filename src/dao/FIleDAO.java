package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Payment;

public class FIleDAO implements  DAO<Payment>{

	 private final String paymentFilePath = "data/payments.txt";

	    @Override
	    public Payment get(int id) {
	        // Not implemented
	        return null;
	    }

	    @Override
	    public Map<Integer, Payment> getAll() {
	        // Not implemented
	        return null;
	    }

	    @Override
	    public int insert(Payment payment) {
	    	File paymentDirectory = new File("data");
	        if (!paymentDirectory.exists()) {
	            paymentDirectory.mkdirs();  // Create the directory if it doesn't exist
	        }
	        
	        // Check if the file exists, create it if not
	        File paymentFile = new File(paymentFilePath);
	        if (!paymentFile.exists()) {
	            try {
	                paymentFile.createNewFile();  // Create the file if it doesn't exist
	            } catch (IOException e) {
	                e.printStackTrace();
	                return 0; // Indicate failure to create the file
	            }
	        }
	        List<String> paymentsData = new ArrayList<>();
	        boolean isDuplicate = false;

	        try (BufferedReader reader = new BufferedReader(new FileReader(paymentFilePath))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                String[] fields = line.split(",");
	                int existingBookingID = Integer.parseInt(fields[1].split(":")[1].trim()); // Extract bookingID
	                if (existingBookingID == payment.getBookingID()) {
	                    isDuplicate = true;
	                }
	                paymentsData.add(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        if (isDuplicate) {
	            return 0; // Indicates failure due to duplication
	        }

	        // Format and add the new payment data
	        paymentsData.add(formatPaymentData(payment));

	        // Write updated data back to the file
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(paymentFilePath))) {
	            for (String paymentData : paymentsData) {
	                writer.write(paymentData);
	                writer.newLine();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return 1; // Indicates successful insertion
	    }

	    @Override
	    public int update(Payment payment) {
	        // Not implemented
	        return 0;
	    }

	    @Override
	    public int delete(Payment payment) {
	        // Not implemented
	        return 0;
	    }

	    // Helper method to format Payment data as a string
	    private String formatPaymentData(Payment payment) {
	        return "paymentID:" + payment.getPaymentID() +
	               ",bookingID:" + payment.getBookingID() +
	               ",amount:" + payment.getAmount() +
	               ",paymentMethod:" + payment.getPaymentMethod() +
	               ",transactionDate:" + payment.getTransactionDate() +
	               ",payerID:" + payment.getPayerID() +
	               ",receiverID:" + payment.getReceiverID();
	    }

}
