package strategies;

import models.Payment;

public interface PaymentStrategy {
	void processPayment(Payment payment);
}
