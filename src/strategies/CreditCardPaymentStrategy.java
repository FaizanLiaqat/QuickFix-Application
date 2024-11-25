package strategies;

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
            
            //payment.setPaymentStatus("Completed");
            dao.PaymentDAO paymentdao = new dao.CreditCardPaymentDAO();
            try {
				paymentdao.update(payment);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
        } else {
            AlertUtils.showError("Payment Error", "Invalid payment type for Credit Card Strategy.");
        }
    }
}