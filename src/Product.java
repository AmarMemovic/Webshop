// Product.java
public class Product {
    private int product_id;
    private String name;
    private String description;
    private double price;
    private int stock_quantity;
    private String category;
    private String created_at;

    // Constructor with ID (for existing products)
    public Product(int product_id, String name, String description, double price,
                   int stock_quantity, String category, String created_at) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock_quantity = stock_quantity;
        this.category = category;
        this.created_at = created_at;
    }

    // Constructor without ID (for new products)
    public Product(String name, String description, double price,
                   int stock_quantity, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock_quantity = stock_quantity;
        this.category = category;
    }

    // Getters and setters
    public int getProduct_id() { return product_id; }
    public void setProduct_id(int product_id) { this.product_id = product_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock_quantity() { return stock_quantity; }
    public void setStock_quantity(int stock_quantity) { this.stock_quantity = stock_quantity; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + product_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock_quantity=" + stock_quantity +
                ", category='" + category + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}