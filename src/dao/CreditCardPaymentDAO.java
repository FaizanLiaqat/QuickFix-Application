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

import models.CreditCardPayment;
import models.Payment;

public class CreditCardPaymentDAO extends PaymentDAO {

    public CreditCardPaymentDAO() {
        super(DatabaseConnection.getInstance().getConnection());
    }

    @Override
    public CreditCardPayment get(int id) {
        // SQL query to fetch data from both Payment and CreditCardPayment tables
        String sql = "SELECT * FROM Payment p INNER JOIN CreditCardPayment c ON p.paymentID = c.paymentID WHERE p.paymentID = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToCreditCardPayment(resultSet);
                }
            }
        } catch (SQLException e) {
           return null;
        }
        return null; // Return null if no payment found
    }

    @Override
    public Map<Integer, Payment> getAll() {
        // SQL query to fetch all CreditCardPayments
        String sql = "SELECT * FROM Payment p INNER JOIN CreditCardPayment c ON p.paymentID = c.paymentID";
        Map<Integer, Payment> payments = new HashMap<>();
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                // Using polymorphism to add CreditCardPayment to the map
                Payment payment = mapToCreditCardPayment(resultSet);
                payments.put(payment.getPaymentID(), payment);
            }
        } catch (SQLException e) {
           return null;
        }
        return payments;
    }
    
    @Override
    public int insert(Payment payment) {
        if (!(payment instanceof CreditCardPayment)) {
            throw new IllegalArgumentException("Invalid payment type.");
        }

        CreditCardPayment creditCardPayment = (CreditCardPayment) payment;

        // Check if a payment already exists for the same bookingID
        try {
			if (isPaymentExists(creditCardPayment.getBookingID())) {
			   utils.AlertUtils.showError("Duplicate", "A payment already exists for this booking ID.");
			   throw new SQLException("Error inserting CreditCardPayment");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

        // SQL to insert data into the Payment table
        String sql = "INSERT INTO payment (bookingID, amount, paymentMethod, transactionDate, payerID, receiverID) VALUES (?, ?, 'CreditCard', ?, ?, ?)";
        String sqlCreditCard = "INSERT INTO creditcardpayment (paymentID, cardNumber, cardType, cardHolderName, expirationDate) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement paymentStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement creditCardStatement = connection.prepareStatement(sqlCreditCard)) {

            // Insert into Payment table
            paymentStatement.setInt(1, creditCardPayment.getBookingID());
            paymentStatement.setBigDecimal(2, creditCardPayment.getAmount());
            paymentStatement.setTimestamp(3, creditCardPayment.getTransactionDate());  // Added payerID

            paymentStatement.setInt(4, creditCardPayment.getPayerID());  // Added payerID
            
            paymentStatement.setInt(5, creditCardPayment.getReceiverID());  // Added receiverID
            paymentStatement.executeUpdate();

            // Retrieve the generated paymentID
            try (ResultSet keys = paymentStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int paymentID = keys.getInt(1);
                    creditCardPayment.setPaymentID(paymentID);  // Set the generated paymentID to the CreditCardPayment

                    // Insert into CreditCardPayment table
                    creditCardStatement.setInt(1, paymentID);
                    creditCardStatement.setString(2, creditCardPayment.getCardNumber());
                    creditCardStatement.setString(3, creditCardPayment.getCardType());
                    creditCardStatement.setString(4, creditCardPayment.getCardHolderName());
                    creditCardStatement.setTimestamp(5,creditCardPayment.getExpirationDate()); // Using setDate for expirationDate
                    return creditCardStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
        	 utils.AlertUtils.showError("Error", "Error inserting CreditCardPayment");
            return -1;
        }

        return -1; // Return 0 if insertion fails
    }

    @Override
    public int update(Payment payment) {
        if (!(payment instanceof CreditCardPayment)) {
            throw new IllegalArgumentException("Invalid payment type.");
        }

        CreditCardPayment creditCardPayment = (CreditCardPayment) payment;

        // SQL to update Payment table
        String sql = "UPDATE Payment SET bookingID = ?, amount = ? WHERE paymentID = ?";
        // SQL to update CreditCardPayment table
        String sqlCreditCard = "UPDATE CreditCardPayment SET cardNumber = ?, cardType = ?, cardHolderName = ?, expirationDate = ? WHERE paymentID = ?";

        try (PreparedStatement paymentStatement = connection.prepareStatement(sql);
             PreparedStatement creditCardStatement = connection.prepareStatement(sqlCreditCard)) {

            // Update Payment table
            paymentStatement.setInt(1, creditCardPayment.getBookingID());
            paymentStatement.setBigDecimal(2, creditCardPayment.getAmount());
            //paymentStatement.setString(3, creditCardPayment.getPaymentStatus());
            paymentStatement.setInt(3, creditCardPayment.getPaymentID());
            paymentStatement.executeUpdate();

            // Update CreditCardPayment table
            creditCardStatement.setString(1, creditCardPayment.getCardNumber());
            creditCardStatement.setString(2, creditCardPayment.getCardType());
            creditCardStatement.setString(3, creditCardPayment.getCardHolderName());
            creditCardStatement.setTimestamp(4, creditCardPayment.getExpirationDate());
            creditCardStatement.setInt(5, creditCardPayment.getPaymentID());
            return creditCardStatement.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
    }

    // Helper method to map the result set to a CreditCardPayment object
    private CreditCardPayment mapToCreditCardPayment(ResultSet resultSet) throws SQLException {
        // Extracting the fields from the Payment table (parent class)
        int paymentID = resultSet.getInt("paymentID");
        int bookingID = resultSet.getInt("bookingID");
        BigDecimal amount = resultSet.getBigDecimal("amount"); // Amount is stored in the Payment table
        String paymentMethod = resultSet.getString("paymentMethod");
        //String paymentStatus = resultSet.getString("paymentStatus");
        int payerID = resultSet.getInt("payerID");
        int receiverID = resultSet.getInt("receiverID");
        java.sql.Timestamp transactionDate = resultSet.getTimestamp("transactionDate");

        // Extracting the fields from the CreditCardPayment table (child class)
        String cardNumber = resultSet.getString("cardNumber");
        String cardType = resultSet.getString("cardType");
        String cardHolderName = resultSet.getString("cardHolderName");
        java.sql.Timestamp expirationDate = resultSet.getTimestamp("expirationDate");

        // Map Payment table values along with CreditCardPayment-specific values to the CreditCardPayment object
        return new CreditCardPayment(
            paymentID, // paymentID from the Payment table
            bookingID, // bookingID
            amount, // amount
            paymentMethod, // paymentMethod
            //paymentStatus, // paymentStatus
            transactionDate, // transactionDate
            payerID, // payerID
            receiverID, // receiverID
            cardNumber, // cardNumber from CreditCardPayment table
            cardType, // cardType
            cardHolderName, // cardHolderName
            expirationDate // expirationDate
        );
    }


    // Check if a payment already exists for the given bookingID
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
        String sql = "SELECT * FROM Payment p INNER JOIN CreditCardPayment c ON p.paymentID = c.paymentID WHERE p.bookingID = ?";
        Map<Integer, Payment> payments = new HashMap<>();
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookingID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Using polymorphism to add CreditCardPayment to the map
                    Payment payment = mapToCreditCardPayment(resultSet);
                    payments.put(payment.getPaymentID(), payment);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching CreditCardPayments by bookingID " + bookingID, e);
        }
        return payments;
    }
    
    


    public Map<Integer, Payment> getPaymentsBySenderID(int payerID) throws SQLException {
        String sql = "SELECT * FROM Payment p INNER JOIN CreditCardPayment c ON p.paymentID = c.paymentID WHERE p.payerID = ?";
        Map<Integer, Payment> payments = new HashMap<>();
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, payerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Using polymorphism to add CreditCardPayment to the map
                    Payment payment = mapToCreditCardPayment(resultSet);
                    payments.put(payment.getPaymentID(), payment);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching CreditCardPayments by payerID " + payerID, e);
        }
        return payments;
    }

    public Map<Integer, Payment> getPaymentsByReceiverID(int receiverID) throws SQLException {
        String sql = "SELECT * FROM Payment p INNER JOIN CreditCardPayment c ON p.paymentID = c.paymentID WHERE p.receiverID = ?";
        Map<Integer, Payment> payments = new HashMap<>();
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, receiverID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Using polymorphism to add CreditCardPayment to the map
                    Payment payment = mapToCreditCardPayment(resultSet);
                    payments.put(payment.getPaymentID(), payment);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching CreditCardPayments by receiverID " + receiverID, e);
        }
        return payments;
    }
    
//    public List<Payment> getPaymentByStatus(String status, int userId) {
//    	List<Payment> payments = new ArrayList<>();
//
//        // SQL query to fetch credit card payments based on status and user ID
//        String sql = "SELECT p.*, c.cardNumber, c.cardType, c.cardHolderName, c.expirationDate " +
//                     "FROM payment p " +
//                     "JOIN creditcardpayment c ON p.paymentID = c.paymentID " +
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
//            // Process the result set
//            while (resultSet.next()) {
//                Payment payment = new CreditCardPayment(
//                        resultSet.getInt("paymentID"),                        // paymentID
//                        resultSet.getInt("bookingID"),                        // bookingID
//                        resultSet.getBigDecimal("amount"),                    // amount
//                        resultSet.getString("paymentMethod"),                 // paymentMethod
//                        //resultSet.getString("paymentStatus"),                 // paymentStatus
//                        resultSet.getTimestamp("transactionDate"),            // transactionDate (as Timestamp)
//                        resultSet.getInt("payerID"),                          // payerID
//                        resultSet.getInt("receiverID"),                       // receiverID
//                        resultSet.getString("cardNumber"),                    // cardNumber
//                        resultSet.getString("cardType"),                      // cardType
//                        resultSet.getString("cardHolderName"),                // cardHolderName
//                        resultSet.getTimestamp("expirationDate")              // expirationDate (as Timestamp)
//                    );                // Add payment to the list
//                payments.add(payment);
//                System.out.println("one record found");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return payments;
//    }

}