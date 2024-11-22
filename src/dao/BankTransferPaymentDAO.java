package dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import models.BankTransferPayment;
import models.Payment;

public class BankTransferPaymentDAO extends PaymentDAO {

    public BankTransferPaymentDAO() {
        super(DatabaseConnection.getInstance().getConnection());
    }

    @Override
    public BankTransferPayment get(int id) throws SQLException {
        String sql = "SELECT * FROM Payment p INNER JOIN BankTransferPayment b ON p.paymentID = b.paymentID WHERE p.paymentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToBankTransferPayment(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching BankTransferPayment with ID " + id, e);
        }
        return null;
    }

    @Override
    public Map<Integer, Payment> getAll() throws SQLException {
        String sql = "SELECT * FROM Payment p INNER JOIN BankTransferPayment b ON p.paymentID = b.paymentID";
        Map<Integer, Payment> payments = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Payment payment = mapToBankTransferPayment(resultSet);
                payments.put(payment.getPaymentID(), payment);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all BankTransferPayments", e);
        }
        return payments;
    }

    @Override
    public int insert(Payment payment) throws SQLException {
        if (!(payment instanceof BankTransferPayment)) {
            throw new IllegalArgumentException("Invalid payment type.");
        }

        BankTransferPayment bankTransferPayment = (BankTransferPayment) payment;

        // Check if there is already a payment with the same bookingID
        if (isPaymentExists(bankTransferPayment.getBookingID())) {
            throw new IllegalArgumentException("A payment already exists for this booking ID.");
        }

        String sql = "INSERT INTO Payment (bookingID, amount, paymentMethod, paymentStatus) VALUES (?, ?, 'BankTransfer', ?)";
        String sqlBankTransfer = "INSERT INTO BankTransferPayment (paymentID, bankName, accountNumber, referenceNumber) VALUES (?, ?, ?, ?)";

        try (PreparedStatement paymentStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement bankTransferStatement = connection.prepareStatement(sqlBankTransfer)) {

            // Insert into Payment table
            paymentStatement.setInt(1, bankTransferPayment.getBookingID());
            paymentStatement.setBigDecimal(2, bankTransferPayment.getAmount());
            paymentStatement.setString(3, bankTransferPayment.getPaymentStatus());
            paymentStatement.executeUpdate();

            try (ResultSet keys = paymentStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int paymentID = keys.getInt(1);
                    bankTransferPayment.setPaymentID(paymentID);

                    // Insert into BankTransferPayment table
                    bankTransferStatement.setInt(1, paymentID);
                    bankTransferStatement.setString(2, bankTransferPayment.getBankName());
                    bankTransferStatement.setString(3, bankTransferPayment.getBankAccountNumber());
                    bankTransferStatement.setString(4, bankTransferPayment.getReferenceCode());
                    return bankTransferStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting BankTransferPayment", e);
        }
        return 0;
    }

    @Override
    public int update(Payment payment) throws SQLException {
        if (!(payment instanceof BankTransferPayment)) {
            throw new IllegalArgumentException("Invalid payment type.");
        }

        BankTransferPayment bankTransferPayment = (BankTransferPayment) payment;
        String sql = "UPDATE Payment SET bookingID = ?, amount = ?, paymentStatus = ? WHERE paymentID = ?";
        String sqlBankTransfer = "UPDATE BankTransferPayment SET bankName = ?, accountNumber = ?, referenceNumber = ? WHERE paymentID = ?";

        try (PreparedStatement paymentStatement = connection.prepareStatement(sql);
             PreparedStatement bankTransferStatement = connection.prepareStatement(sqlBankTransfer)) {

            // Update Payment table
            paymentStatement.setInt(1, bankTransferPayment.getBookingID());
            paymentStatement.setBigDecimal(2, bankTransferPayment.getAmount());
            paymentStatement.setString(3, bankTransferPayment.getPaymentStatus());
            paymentStatement.setInt(4, bankTransferPayment.getPaymentID());
            paymentStatement.executeUpdate();

            // Update BankTransferPayment table
            bankTransferStatement.setString(1, bankTransferPayment.getBankName());
            bankTransferStatement.setString(2, bankTransferPayment.getBankAccountNumber());
            bankTransferStatement.setString(3, bankTransferPayment.getReferenceCode());
            bankTransferStatement.setInt(4, bankTransferPayment.getPaymentID());
            return bankTransferStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating BankTransferPayment", e);
        }
    }

    private BankTransferPayment mapToBankTransferPayment(ResultSet resultSet) throws SQLException {
        int paymentID = resultSet.getInt("paymentID");
        int bookingID = resultSet.getInt("bookingID");
        BigDecimal amount = resultSet.getBigDecimal("amount"); // Adjusted to BigDecimal
        String paymentMethod = resultSet.getString("paymentMethod"); // Added paymentMethod
        String paymentStatus = resultSet.getString("paymentStatus");

        int paymentID2 = resultSet.getInt("paymentID"); // Retained as a separate field
        String bankAccountNumber = resultSet.getString("accountNumber"); // Adjusted to match parameter name
        String bankName = resultSet.getString("bankName");
        String referenceCode = resultSet.getString("referenceNumber"); // Adjusted to match parameter name
        Timestamp transferDate = resultSet.getTimestamp("transferDate"); // Added transferDate

        return new BankTransferPayment(
            paymentID,
            bookingID,
            amount,
            paymentMethod,
            paymentStatus,
            paymentID2,
            bankAccountNumber,
            bankName,
            referenceCode,
            transferDate
        );
    }
    
 // Helper method to check if a payment already exists for the same bookingID
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
