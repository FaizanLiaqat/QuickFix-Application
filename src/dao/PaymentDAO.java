package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import models.Payment;

public abstract class PaymentDAO implements DAO<Payment> {
    protected Connection connection;

    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Payment get(int id) throws SQLException {
        throw new UnsupportedOperationException("This method must be implemented by subclasses.");
    }

    @Override
    public Map<Integer, Payment> getAll() throws SQLException {
        throw new UnsupportedOperationException("This method must be implemented by subclasses.");
    }

    @Override
    public int insert(Payment payment) throws SQLException {
        throw new UnsupportedOperationException("This method must be implemented by subclasses.");
    }

    @Override
    public int update(Payment payment) throws SQLException {
        throw new UnsupportedOperationException("This method must be implemented by subclasses.");
    }

    @Override
    public int delete(Payment payment) throws SQLException {
        String sql = "DELETE FROM Payment WHERE paymentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, payment.getPaymentID());
            return statement.executeUpdate();
        }
    }
}