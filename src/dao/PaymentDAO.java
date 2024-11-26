package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import models.Payment;

public abstract class PaymentDAO implements DAO<Payment> {
    protected Connection connection;

    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Payment get(int id) {
        throw new UnsupportedOperationException("This method must be implemented by subclasses.");
    }

    @Override
    public Map<Integer, Payment> getAll() {
        throw new UnsupportedOperationException("This method must be implemented by subclasses.");
    }

    @Override
    public int insert(Payment payment) {
        throw new UnsupportedOperationException("This method must be implemented by subclasses.");
    }

    @Override
    public int update(Payment payment) {
        throw new UnsupportedOperationException("This method must be implemented by subclasses.");
    }

    @Override
    public int delete(Payment payment) {
        String sql = "DELETE FROM Payment WHERE paymentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, payment.getPaymentID());
            return statement.executeUpdate();
        } catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
    }
    
    
   // public abstract List<Payment> getPaymentByStatus(String status, int userId) ;
    
    public abstract Map<Integer, Payment> getPaymentsByBookingID(int bookingID) throws SQLException;

	//public abstract Map<Integer, Payment> getPaymentsBySenderID(int id); 
    
}