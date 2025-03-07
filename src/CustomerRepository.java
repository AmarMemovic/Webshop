// CustomerRepository.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private DatabaseConnection dbConnection;

    public CustomerRepository() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    // Find all customers
    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("created_at")
                );
                customers.add(customer);
            }
        }
        return customers;
    }

    // Find customer by ID
    public Customer findById(int customerId) throws SQLException {
        String query = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("created_at")
                );
            }
        }
        return null;
    }

    // Save new customer
    public Customer save(Customer customer) throws SQLException {
        String query = "INSERT INTO customers (first_name, last_name, email) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, customer.getFirst_name());
            pstmt.setString(2, customer.getLast_name());
            pstmt.setString(3, customer.getEmail());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setCustomer_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }

            // Get the complete customer with created_at
            return findById(customer.getCustomer_id());
        }
    }

    // Update existing customer
    public boolean update(Customer customer) throws SQLException {
        String query = "UPDATE customers SET first_name = ?, last_name = ?, email = ? WHERE customer_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customer.getFirst_name());
            pstmt.setString(2, customer.getLast_name());
            pstmt.setString(3, customer.getEmail());
            pstmt.setInt(4, customer.getCustomer_id());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}