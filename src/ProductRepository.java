// ProductRepository.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private DatabaseConnection dbConnection;

    public ProductRepository() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    // Find all products
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        rs.getString("category"),
                        rs.getString("created_at")
                );
                products.add(product);
            }
        }
        return products;
    }


    //skapar nya produkter:
    // ProductRepository.java
    public Product save(Product product) throws SQLException {
        String query = "INSERT INTO products (name, description, price, stock_quantity, category) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getStock_quantity());
            pstmt.setString(5, product.getCategory());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setProduct_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }

            return findById(product.getProduct_id());
        }
    }
        //skapar nya produkter

    // Find product by ID
    public Product findById(int productId) throws SQLException {
        String query = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        rs.getString("category"),
                        rs.getString("created_at")
                );
            }
        }
        return null;
    }

    // Search products by name
    public List<Product> searchByName(String searchTerm) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE name LIKE ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        rs.getString("category"),
                        rs.getString("created_at")
                );
                products.add(product);
            }
        }
        return products;
    }

    // Filter products by category
    public List<Product> filterByCategory(String category) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE category = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        rs.getString("category"),
                        rs.getString("created_at")
                );
                products.add(product);
            }
        }
        return products;
    }

    // Update product price
    public boolean updatePrice(int productId, double newPrice) throws SQLException {
        String query = "UPDATE products SET price = ? WHERE product_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, productId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Update product stock quantity
    public boolean updateStock(int productId, int newQuantity) throws SQLException {
        String query = "UPDATE products SET stock_quantity = ? WHERE product_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Reduce stock after order
    public boolean reduceStock(int productId, int quantityToReduce) throws SQLException {
        // First check if we have enough stock
        Product product = findById(productId);
        if (product == null || product.getStock_quantity() < quantityToReduce) {
            return false;
        }

        // Then update the stock
        int newQuantity = product.getStock_quantity() - quantityToReduce;
        return updateStock(productId, newQuantity);
    }
}
