package strategies;

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
			bankPayment.setPaymentStatus("Completed");

			// AlertUtils for feedback
			String message = "Bank Transfer Payment Successful!\n" + "Amount: " + bankPayment.getAmount() + "\n"
					+ "Bank Name: " + bankPayment.getBankName() + "\n" + "Account Number: "
					+ bankPayment.getBankAccountNumber() + "\n" + "Reference Code: " + bankPayment.getReferenceCode()
					+ "\n" + "Transfer Date: " + bankPayment.getTransferDate();
			AlertUtils.showSuccess(message);

			payment.setPaymentStatus("Completed");
			dao.PaymentDAO paymentdao = new dao.BankTransferPaymentDAO();
			try {
				paymentdao.update(payment);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			AlertUtils.showError("Payment Error", "Invalid payment type for Bank Transfer Strategy.");
		}
	}

}
