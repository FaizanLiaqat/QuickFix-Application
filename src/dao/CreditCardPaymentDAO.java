package dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import models.CreditCardPayment;
import models.Payment;

public class CreditCardPaymentDAO extends PaymentDAO {

    public CreditCardPaymentDAO() {
        super(DatabaseConnection.getInstance().getConnection());
    }

    @Override
    public CreditCardPayment get(int id) throws SQLException {
        String sql = "SELECT * FROM Payment p INNER JOIN CreditCardPayment c ON p.paymentID = c.paymentID WHERE p.paymentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToCreditCardPayment(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching CreditCardPayment with ID " + id, e);
        }
        return null;
    }

    @Override
    public Map<Integer, Payment> getAll() throws SQLException {
        String sql = "SELECT * FROM Payment p INNER JOIN CreditCardPayment c ON p.paymentID = c.paymentID";
        Map<Integer, Payment> payments = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                // Polymorphically add CreditCardPayment to the Payment map
                Payment payment = mapToCreditCardPayment(resultSet);
                payments.put(payment.getPaymentID(), payment);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all CreditCardPayments", e);
        }
        return payments;
    }
    
    @Override
    public int insert(Payment payment) throws SQLException {
        if (!(payment instanceof CreditCardPayment)) {
            throw new IllegalArgumentException("Invalid payment type.");
        }

        CreditCardPayment creditCardPayment = (CreditCardPayment) payment;

        // Check if there is already a payment with the same bookingID
        if (isPaymentExists(creditCardPayment.getBookingID())) {
            throw new IllegalArgumentException("A payment already exists for this booking ID.");
        }

        String sql = "INSERT INTO Payment (bookingID, amount, paymentMethod, paymentStatus) VALUES (?, ?, 'CreditCard', ?)";
        String sqlCreditCard = "INSERT INTO CreditCardPayment (paymentID, cardNumber, cardType, cardHolderName, expirationDate) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement paymentStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement creditCardStatement = connection.prepareStatement(sqlCreditCard)) {

            // Insert into Payment table
            paymentStatement.setInt(1, creditCardPayment.getBookingID());
            paymentStatement.setBigDecimal(2, creditCardPayment.getAmount());
            paymentStatement.setString(3, creditCardPayment.getPaymentStatus());
            paymentStatement.executeUpdate();

            try (ResultSet keys = paymentStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int paymentID = keys.getInt(1);
                    creditCardPayment.setPaymentID(paymentID);

                    // Insert into CreditCardPayment table
                    creditCardStatement.setInt(1, paymentID);
                    creditCardStatement.setString(2, creditCardPayment.getCardNumber());
                    creditCardStatement.setString(3, creditCardPayment.getCardType());
                    creditCardStatement.setString(4, creditCardPayment.getCardHolderName());
                    creditCardStatement.setDate(5, creditCardPayment.getExpirationDate());
                    return creditCardStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting CreditCardPayment", e);
        }
        return 0;
    }


    @Override
    public int update(Payment payment) throws SQLException {
        if (!(payment instanceof CreditCardPayment)) {
            throw new IllegalArgumentException("Invalid payment type.");
        }

        CreditCardPayment creditCardPayment = (CreditCardPayment) payment;
        String sql = "UPDATE Payment SET bookingID = ?, amount = ?, paymentStatus = ? WHERE paymentID = ?";
        String sqlCreditCard = "UPDATE CreditCardPayment SET cardNumber = ?, cardType = ?, cardHolderName = ?, expirationDate = ? WHERE paymentID = ?";

        try (PreparedStatement paymentStatement = connection.prepareStatement(sql);
             PreparedStatement creditCardStatement = connection.prepareStatement(sqlCreditCard)) {

            // Update Payment table
            paymentStatement.setInt(1, creditCardPayment.getBookingID());
            paymentStatement.setBigDecimal(2, creditCardPayment.getAmount());
            paymentStatement.setString(3, creditCardPayment.getPaymentStatus());
            paymentStatement.setInt(4, creditCardPayment.getPaymentID());
            paymentStatement.executeUpdate();

            // Update CreditCardPayment table
            creditCardStatement.setString(1, creditCardPayment.getCardNumber());
            creditCardStatement.setString(2, creditCardPayment.getCardType());
            creditCardStatement.setString(3, creditCardPayment.getCardHolderName());
            creditCardStatement.setDate(4, creditCardPayment.getExpirationDate());
            creditCardStatement.setInt(5, creditCardPayment.getPaymentID());
            return creditCardStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating CreditCardPayment", e);
        }
    }

    private CreditCardPayment mapToCreditCardPayment(ResultSet resultSet) throws SQLException {
        int paymentID = resultSet.getInt("paymentID");
        int bookingID = resultSet.getInt("bookingID");
        BigDecimal amount = resultSet.getBigDecimal("amount"); // Convert amount to BigDecimal
        String paymentMethod = resultSet.getString("paymentMethod"); // Assuming this is stored in the database
        String paymentStatus = resultSet.getString("paymentStatus");
        
        int paymentID2 = resultSet.getInt("paymentID"); // If `paymentID2` is meant to be a duplicate of `paymentID`
        String cardNumber = resultSet.getString("cardNumber");
        String cardType = resultSet.getString("cardType");
        String cardHolderName = resultSet.getString("cardHolderName");
        Date expirationDate = resultSet.getDate("expirationDate");

        return new CreditCardPayment(
            paymentID,
            bookingID,
            amount,
            paymentMethod,
            paymentStatus,
            paymentID2,
            cardNumber,
            cardType,
            cardHolderName,
            expirationDate
        );
    }
    private boolean isPaymentExists(int bookingID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Payment WHERE bookingID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookingID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;  // Return true if a payment already exists for the bookingID
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error checking for existing payment with bookingID " + bookingID, e);
        }
        return false; // No existing payment found for the bookingID
    }
}
