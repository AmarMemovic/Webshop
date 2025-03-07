// ProductService.java
import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private ProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
    }

    // Get all products
    public List<Product> getAllProducts() throws SQLException {
        return productRepository.findAll();
    }

    // Get product by ID
    public Product getProductById(int productId) throws SQLException {
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        return product;
    }

    // Search products by name
    public List<Product> searchProducts(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }
        return productRepository.searchByName(keyword);
    }

    // Filter products by category
    public List<Product> filterByCategory(String category) throws SQLException {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        return productRepository.filterByCategory(category);
    }

    // Update product price
    public boolean updateProductPrice(int productId, double newPrice) throws SQLException {
        if (newPrice <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        // Check if product exists
        getProductById(productId);

        return productRepository.updatePrice(productId, newPrice);
    }

    // Update product stock
    public boolean updateProductStock(int productId, int newQuantity) throws SQLException {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        // Check if product exists
        getProductById(productId);

        return productRepository.updateStock(productId, newQuantity);
    }

    // Check if product has sufficient stock
    public boolean hasSufficientStock(int productId, int quantity) throws SQLException {
        Product product = getProductById(productId);
        return product.getStock_quantity() >= quantity;
    }


//Ny kod för att testa
// ProductService.java
public Product createProduct(String name, String description, double price, int stockQuantity, String category) throws SQLException {
    // Validate inputs
    if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("Product name cannot be empty");
    }
    if (description == null || description.trim().isEmpty()) {
        throw new IllegalArgumentException("Product description cannot be empty");
    }
    if (price <= 0) {
        throw new IllegalArgumentException("Product price must be greater than zero");
    }
    if (stockQuantity < 0) {
        throw new IllegalArgumentException("Product stock quantity cannot be negative");
    }
    if (category == null || category.trim().isEmpty()) {
        throw new IllegalArgumentException("Product category cannot be empty");
    }

    Product newProduct = new Product(name, description, price, stockQuantity, category);

    return productRepository.save(newProduct);
}
}

