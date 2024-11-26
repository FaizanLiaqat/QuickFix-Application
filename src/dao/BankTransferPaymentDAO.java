package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.BankTransferPayment;
import models.CreditCardPayment;
import models.Payment;

public class BankTransferPaymentDAO extends PaymentDAO {

    public BankTransferPaymentDAO() {
        super(DatabaseConnection.getInstance().getConnection());
    }

    @Override
    public BankTransferPayment get(int id) throws SQLException {
        // SQL to fetch data from both Payment and BankTransferPayment tables
        String sql = "SELECT * FROM Payment p INNER JOIN BankTransferPayment b ON p.paymentID = b.paymentID WHERE p.paymentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToBankTransferPayment(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching BankTransferPayment with ID " + id, e);
        }
        return null; // Return null if no payment found
    }

    @Override
    public Map<Integer, Payment> getAll() throws SQLException {
        // SQL to fetch all BankTransferPayments
        String sql = "SELECT * FROM Payment p INNER JOIN BankTransferPayment b ON p.paymentID = b.paymentID";
        Map<Integer, Payment> payments = new HashMap<>();
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Payment payment = mapToBankTransferPayment(resultSet);
                payments.put(payment.getPaymentID(), payment);
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching all BankTransferPayments", e);
        }
        return payments;
    }

    @Override
    public int insert(Payment payment) throws SQLException {
    	if (!(payment instanceof BankTransferPayment)) {
			throw new IllegalArgumentException("Invalid payment type.");
		}

		BankTransferPayment bankTransferPayment = (BankTransferPayment) payment;

		// Check if a payment already exists for the same bookingID
		if (isPaymentExists(bankTransferPayment.getBookingID())) {
			throw new IllegalArgumentException("A payment already exists for this booking ID.");
		}

		// SQL to insert into Payment table
		String sql = "INSERT INTO payment (bookingID, amount, paymentMethod, transactionDate, payerID, receiverID) VALUES (?, ?, 'BankTransfer', ?, ?, ?)";

		String sqlBankTransfer = "INSERT INTO BankTransferPayment (paymentID, bankAccountNumber, bankName, referenceCode, transferDate) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement paymentStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement bankTransferStatement = connection.prepareStatement(sqlBankTransfer)) {

			paymentStatement.setInt(1, bankTransferPayment.getBookingID());
			paymentStatement.setBigDecimal(2, bankTransferPayment.getAmount());
			paymentStatement.setTimestamp(3, bankTransferPayment.getTransactionDate()); // Added payerID

			paymentStatement.setInt(4, bankTransferPayment.getPayerID()); // Added payerID

			paymentStatement.setInt(5, bankTransferPayment.getReceiverID()); // Added receiverID
			paymentStatement.executeUpdate();
			// Retrieve the generated paymentID
			try (ResultSet keys = paymentStatement.getGeneratedKeys()) {
				if (keys.next()) {
					int paymentID = keys.getInt(1);
					bankTransferPayment.setPaymentID(paymentID);

					// Insert into BankTransferPayment table
					bankTransferStatement.setInt(1, paymentID);
					bankTransferStatement.setString(2, bankTransferPayment.getBankAccountNumber());
					bankTransferStatement.setString(3, bankTransferPayment.getBankName());
					bankTransferStatement.setString(4, bankTransferPayment.getReferenceCode());
					bankTransferStatement.setTimestamp(5,
							new Timestamp(bankTransferPayment.getTransferDate().getTime()));
					return bankTransferStatement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			throw new SQLException("Error inserting BankTransferPayment", e);
		}
		return 0;
    }

    @Override
    public int update(Payment payment) throws SQLException {
        if (!(payment instanceof BankTransferPayment)) {
            throw new IllegalArgumentException("Invalid payment type.");
        }

        BankTransferPayment bankTransferPayment = (BankTransferPayment) payment;
        String sql = "UPDATE Payment SET bookingID = ?, amount = ?WHERE paymentID = ?";
        String sqlBankTransfer = "UPDATE BankTransferPayment SET bankAccountNumber = ?, bankName = ?, referenceCode = ?, transferDate = ? WHERE paymentID = ?";

        try (PreparedStatement paymentStatement = connection.prepareStatement(sql);
             PreparedStatement bankTransferStatement = connection.prepareStatement(sqlBankTransfer)) {

            // Update Payment table
            paymentStatement.setInt(1, bankTransferPayment.getBookingID());
            paymentStatement.setBigDecimal(2, bankTransferPayment.getAmount());
            //paymentStatement.setString(3, bankTransferPayment.getPaymentStatus());
            paymentStatement.setInt(3, bankTransferPayment.getPaymentID());
            paymentStatement.executeUpdate();

            // Update BankTransferPayment table
            bankTransferStatement.setString(1, bankTransferPayment.getBankAccountNumber());
            bankTransferStatement.setString(2, bankTransferPayment.getBankName());
            bankTransferStatement.setString(3, bankTransferPayment.getReferenceCode());
            bankTransferStatement.setTimestamp(4, new Timestamp(bankTransferPayment.getTransferDate().getTime()));
            bankTransferStatement.setInt(5, bankTransferPayment.getPaymentID());
            return bankTransferStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating BankTransferPayment", e);
        }
    }

    // Helper method to map the result set to a BankTransferPayment object
    private BankTransferPayment mapToBankTransferPayment(ResultSet resultSet) throws SQLException {
        // Extracting fields from the Payment table (parent class)
        int paymentID = resultSet.getInt("paymentID");
        int bookingID = resultSet.getInt("bookingID");
        BigDecimal amount = resultSet.getBigDecimal("amount"); // Amount from Payment table
        String paymentMethod = resultSet.getString("paymentMethod"); // Payment method from Payment table
        //String paymentStatus = resultSet.getString("paymentStatus");
        java.sql.Timestamp transactionDate = resultSet.getTimestamp("transactionDate"); // Transaction date from Payment table
        int payerID = resultSet.getInt("payerID"); // Extracting payerID from Payment table
        int receiverID = resultSet.getInt("receiverID"); // Extracting receiverID from Payment table

        // Fields from BankTransferPayment table (child class)
        String bankAccountNumber = resultSet.getString("bankAccountNumber");
        String bankName = resultSet.getString("bankName");
        String referenceCode = resultSet.getString("referenceCode");
        java.sql.Timestamp transferDate = resultSet.getTimestamp("transferDate"); // Transfer date from BankTransferPayment table

        // Return new BankTransferPayment object, passing the extracted fields
        return new BankTransferPayment(
            paymentID,
            bookingID,
            amount,
            paymentMethod,
           // paymentStatus,
            transactionDate, // Transaction date from Payment table
            payerID, // payerID from Payment table
            receiverID, // receiverID from Payment table
            bankAccountNumber,
            bankName,
            referenceCode,
            transferDate  // transferDate from BankTransferPayment table
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
    
    public Map<Integer, Payment> getPaymentsByBookingID(int bookingID) throws SQLException {
        String sql = "SELECT * FROM Payment p INNER JOIN BankTransferPayment b ON p.paymentID = b.paymentID WHERE p.bookingID = ?";
        Map<Integer, Payment> payments = new HashMap<>();
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookingID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Payment payment = mapToBankTransferPayment(resultSet);
                    payments.put(payment.getPaymentID(), payment);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching BankTransferPayments by bookingID " + bookingID, e);
        }
        return payments;
    }

    public Map<Integer, Payment> getPaymentsBySenderID(int payerID) throws SQLException {
        String sql = "SELECT * FROM Payment p INNER JOIN BankTransferPayment b ON p.paymentID = b.paymentID WHERE p.payerID = ?";
        Map<Integer, Payment> payments = new HashMap<>();
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, payerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Payment payment = mapToBankTransferPayment(resultSet);
                    payments.put(payment.getPaymentID(), payment);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching BankTransferPayments by payerID " + payerID, e);
        }
        return payments;
    }

    public Map<Integer, Payment> getPaymentsByReceiverID(int receiverID) throws SQLException {
        String sql = "SELECT * FROM Payment p INNER JOIN BankTransferPayment b ON p.paymentID = b.paymentID WHERE p.receiverID = ?";
        Map<Integer, Payment> payments = new HashMap<>();
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, receiverID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Payment payment = mapToBankTransferPayment(resultSet);
                    payments.put(payment.getPaymentID(), payment);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching BankTransferPayments by receiverID " + receiverID, e);
        }
        return payments;
    }
    
//    public List<Payment> getPaymentByStatus(String status, int userId) {
//    	List<Payment> payments = new ArrayList<>();
//
//        // SQL query to fetch credit card payments based on status and user ID
//        String sql = "SELECT p.*, c.bankAccountNumber, c.bankName, c.referenceCode, c.transferDate " +
//                     "FROM payment p " +
//                     "JOIN banktransferpayment c ON p.paymentID = c.paymentID " +
//                     "WHERE p.paymentStatus = ? AND (p.payerID = ? )";
//
//        try (Connection connection = DatabaseConnection.getInstance().getConnection();
//   	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            // Set parameters for the query
//            preparedStatement.setString(1, status);
//            preparedStatement.setInt(2, userId);
//           
//            // Execute the query
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            //    public BankTransferPayment(int paymentID, int bookingID, BigDecimal amount, String paymentMethod, String paymentStatus, java.sql.Timestamp transactionDate, int payerID, int receiverID, String bankAccountNumber, String bankName, String referenceCode, java.sql.Timestamp transferDate) {
//
//            // Process the result set
//            while (resultSet.next()) {
//                Payment payment = new BankTransferPayment(
//                        resultSet.getInt("paymentID"),                        // paymentID
//                        resultSet.getInt("bookingID"),                        // bookingID
//                        resultSet.getBigDecimal("amount"),                    // amount
//                        resultSet.getString("paymentMethod"),                 // paymentMethod
//                        resultSet.getString("paymentStatus"),                 // paymentStatus
//                        resultSet.getTimestamp("transactionDate"),            // transactionDate (as Timestamp)
//                        resultSet.getInt("payerID"),                          // payerID
//                        resultSet.getInt("receiverID"),                       // receiverID
//                        resultSet.getString("bankAccountNumber"),                    // cardNumber
//                        resultSet.getString("bankName"),                      // cardType
//                        resultSet.getString("referenceCode"),                // cardHolderName
//                        resultSet.getTimestamp("transferDate")              // expirationDate (as Timestamp)
//                    );                // Add payment to the list
//                payments.add(payment);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return payments;
//    }

}