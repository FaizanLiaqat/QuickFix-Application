package strategies;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

import models.CreditCardPayment;
import models.Payment;
import utils.AlertUtils;

public class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public void processPayment(Payment payment) {
        if (payment instanceof CreditCardPayment) {
            CreditCardPayment ccPayment = (CreditCardPayment) payment;
            // Logic for processing credit card payment
            System.out.println("Processing Credit Card Payment for amount: " + ccPayment.getAmount() +
                    " using card: " + ccPayment.getCardType());
            //ccPayment.setPaymentStatus("Completed");

            // AlertUtils for feedback
            String message = "Credit Card Payment Successful!\n" +
                    "Amount: " + ccPayment.getAmount() + "\n" +
                    "Card Type: " + ccPayment.getCardType() + "\n" +
                    "Card Holder: " + ccPayment.getCardHolderName() + "\n" +
                    "Expiration Date: " + ccPayment.getExpirationDate();
            AlertUtils.showSuccess(message);
            BigDecimal totalAmount = payment.getAmount(); 

            BigDecimal adminPercentage = new BigDecimal("5");  // Admin gets 5%
            BigDecimal sellerPercentage = new BigDecimal("95"); // Seller gets 95%

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
            AlertUtils.showError("Payment Error", "Invalid payment type for Credit Card Strategy.");
        }
    }
}