package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static final String URL = "jdbc:mysql://localhost:3306/quickfixdatabase";
    private static final String USER = "root";



    private static final String PASSWORD = "1234";




    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
            System.out.println("Database driver loaded.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database driver.", e);
        }
    }

    // Synchronized method to ensure thread safety
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    // Method to get the connection object
    // It would create fresh connection 
    public Connection getConnection() {
    	try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create a new database connection.", e);
        }
    }
}



	
