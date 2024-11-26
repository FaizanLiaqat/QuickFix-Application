package strategies;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

import models.BankTransferPayment;
import models.Payment;
import utils.AlertUtils;

public class BankTransferPaymentStrategy implements PaymentStrategy {
	@Override
	public void processPayment(Payment payment) {

		if (payment instanceof BankTransferPayment) {
			BankTransferPayment bankPayment = (BankTransferPayment) payment;
			// Logic for processing bank transfer payment
			System.out.println("Processing Bank Transfer Payment for amount: " + bankPayment.getAmount() + " via bank: "
					+ bankPayment.getBankName());

			// AlertUtils for feedback
			String message = "Bank Transfer Payment Successful!\n" + "Amount: " + bankPayment.getAmount() + "\n"
					+ "Bank Name: " + bankPayment.getBankName() + "\n" + "Account Number: "
					+ bankPayment.getBankAccountNumber() + "\n" + "Reference Code: " + bankPayment.getReferenceCode()
					+ "\n" + "Transfer Date: " + bankPayment.getTransferDate();
			AlertUtils.showSuccess(message);

			BigDecimal totalAmount = payment.getAmount(); 

            BigDecimal adminPercentage = new BigDecimal("10");  // Admin gets 5%
            BigDecimal sellerPercentage = new BigDecimal("90"); // Seller gets 95%

            // Calculate the amounts using BigDecimal operations
            BigDecimal adminAmount = totalAmount.multiply(adminPercentage).divide(new BigDecimal("100"), RoundingMode.HALF_UP);
            BigDecimal sellerAmount = totalAmount.multiply(sellerPercentage).divide(new BigDecimal("100"), RoundingMode.HALF_UP);
            dao.SellerDAO  sellerDao = new dao.SellerDAO();
            dao.AdminDAO adminDao = new dao.AdminDAO();
            
            try {
				sellerDao.updateSellerBalance(payment.getReceiverID(), sellerAmount);
			} catch (SQLException e) {
				e.printStackTrace();
			}
            try {
				adminDao.updateAllAdminBalances(adminAmount);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			AlertUtils.showError("Payment Error", "Invalid payment type for Bank Transfer Strategy.");
		}
	}

}
