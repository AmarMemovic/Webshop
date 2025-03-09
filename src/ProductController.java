// ProductController.java
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductController {
    private Scanner scanner;
    private ProductService productService;

    public ProductController(Scanner scanner) {
        this.scanner = scanner;
        this.productService = new ProductService();
    }

    public void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== PRODUCT MANAGEMENT =====");
            System.out.println("1. List All Products");
            System.out.println("2. Search Products by Name");
            System.out.println("3. Filter Products by Category");
            System.out.println("4. Update Product Price");
            System.out.println("5. Update Product Stock");
            System.out.println("6. New Product");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        listAllProducts();
                        break;
                    case 2:
                        searchProducts();
                        break;
                    case 3:
                        filterByCategory();
                        break;
                    case 4:
                        updateProductPrice();
                        break;
                    case 5:
                        updateProductStock();
                        break;
                    case 6:
                        createProduct();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void listAllProducts() throws SQLException {
        System.out.println("\n----- All Products -----");
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void searchProducts() throws SQLException {
        System.out.println("\n----- Search Products -----");

        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();

        try {
            List<Product> products = productService.searchProducts(keyword);

            if (products.isEmpty()) {
                System.out.println("No products found matching: " + keyword);
                return;
            }

            System.out.println("Found " + products.size() + " products:");
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void filterByCategory() throws SQLException {
        System.out.println("\n----- Filter by Category -----");

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        try {
            List<Product> products = productService.filterByCategory(category);

            if (products.isEmpty()) {
                System.out.println("No products found in category: " + category);
                return;
            }

            System.out.println("Found " + products.size() + " products in category '" + category + "':");
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateProductPrice() throws SQLException {
        System.out.println("\n----- Update Product Price -----");

        System.out.print("Enter product ID: ");
        try {
            int productId = Integer.parseInt(scanner.nextLine());

            // Show current details
            Product product = productService.getProductById(productId);
            System.out.println("Current details: " + product);

            System.out.print("Enter new price (current: $" + product.getPrice() + "): ");
            double newPrice = Double.parseDouble(scanner.nextLine());

            boolean updated = productService.updateProductPrice(productId, newPrice);
            if (updated) {
                System.out.println("Product price updated successfully!");
            } else {
                System.out.println("Failed to update product price.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateProductStock() throws SQLException {
        System.out.println("\n----- Update Product Stock -----");

        System.out.print("Enter product ID: ");
        try {
            int productId = Integer.parseInt(scanner.nextLine());

            // Show current details
            Product product = productService.getProductById(productId);
            System.out.println("Current details: " + product);

            System.out.print("Enter new stock quantity (current: " + product.getStock_quantity() + "): ");
            int newQuantity = Integer.parseInt(scanner.nextLine());

            boolean updated = productService.updateProductStock(productId, newQuantity);
            if (updated) {
                System.out.println("Product stock updated successfully!");
            } else {
                System.out.println("Failed to update product stock.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    // Ny kod för att testa:
    // ProductController.java
    public void createProduct() {
        System.out.println("\n----- Create New Product -----");

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product description: ");
        String description = scanner.nextLine();

        System.out.print("Enter product price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter stock quantity: ");
        int stockQuantity = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter product category: ");
        String category = scanner.nextLine();

        try {
            Product newProduct = productService.createProduct(name, description, price, stockQuantity, category);
            System.out.println("Product created successfully: " + newProduct);
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private int aabc = 1;
    private int getAabc = 10;

}

//ny kod
