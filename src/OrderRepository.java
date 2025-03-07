// OrderRepository.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private DatabaseConnection dbConnection;

    public OrderRepository() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    // Find all orders
    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getString("order_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                );

                // Load order items for this order
                loadOrderItems(order, conn); // Pass the existing connection
                orders.add(order);
            }
        }
        return orders;
    }

    // Find orders by customer ID - FIXED VERSION
    public List<Order> findByCustomerId(int customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE customer_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getString("order_date"),
                            rs.getDouble("total_amount"),
                            rs.getString("status")
                    );

                    // Load order items for this order
                    loadOrderItems(order, conn); // Pass the existing connection
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    // Find order by ID
    public Order findById(int orderId) throws SQLException {
        return findById(orderId, null); // Call the overloaded method with null connection
    }

    // Overloaded findById that accepts an existing connection
    public Order findById(int orderId, Connection existingConn) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean useExistingConn = (existingConn != null);

        try {
            conn = useExistingConn ? existingConn : dbConnection.getConnection();
            String query = "SELECT * FROM orders WHERE order_id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getString("order_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                );

                // Load order items for this order
                loadOrderItems(order, conn);
                return order;
            }
            return null;
        } finally {
            // Only close resources we created (not the existing connection)
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null && !useExistingConn) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Load order items for a specific order
    private void loadOrderItems(Order order) throws SQLException {
        loadOrderItems(order, null); // Call the overloaded method with null connection
    }

    // Overloaded loadOrderItems that accepts an existing connection
    private void loadOrderItems(Order order, Connection existingConn) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean useExistingConn = (existingConn != null);

        try {
            conn = useExistingConn ? existingConn : dbConnection.getConnection();
            String query = "SELECT oi.*, p.name as product_name FROM order_items oi " +
                    "JOIN products p ON oi.product_id = p.product_id " +
                    "WHERE oi.order_id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, order.getOrder_id());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem(
                        rs.getInt("order_item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price_at_time"),
                        rs.getString("product_name")
                );
                order.getOrderItems().add(item);
            }
        } finally {
            // Only close resources we created (not the existing connection)
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null && !useExistingConn) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Create new order
    public Order save(Order order) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert order
            String orderQuery = "INSERT INTO orders (customer_id, status) VALUES (?, ?)";
            pstmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, order.getCustomer_id());
            pstmt.setString(2, order.getStatus());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setOrder_id(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }

            // Close first prepared statement and result set
            if (generatedKeys != null) { generatedKeys.close(); }
            if (pstmt != null) { pstmt.close(); }

            // Insert order items
            String itemQuery = "INSERT INTO order_items (order_id, product_id, quantity, price_at_time) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(itemQuery);
            for (OrderItem item : order.getOrderItems()) {
                pstmt.setInt(1, order.getOrder_id());
                pstmt.setInt(2, item.getProduct_id());
                pstmt.setInt(3, item.getQuantity());
                pstmt.setDouble(4, item.getPrice_at_time());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();

            // Update order total
            String updateQuery = "UPDATE orders SET total_amount = ? WHERE order_id = ?";
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setDouble(1, order.getTotal_amount());
            pstmt.setInt(2, order.getOrder_id());
            pstmt.executeUpdate();
            pstmt.close();

            conn.commit(); // Commit transaction

            // Use the same connection to find the order
            return findById(order.getOrder_id(), conn);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            // Reset auto-commit before closing
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close(); // Explicitly close the connection when done
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}