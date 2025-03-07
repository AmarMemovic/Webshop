// OrderItem.java
public class OrderItem {
    private int order_item_id;
    private int order_id;
    private int product_id;
    private int quantity;
    private double price_at_time; // Price when order was placed
    private String product_name; // For display purposes

    // Constructor with ID (for existing order items)
    public OrderItem(int order_item_id, int order_id, int product_id, int quantity,
                     double price_at_time, String product_name) {
        this.order_item_id = order_item_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price_at_time = price_at_time;
        this.product_name = product_name;
    }

    // Constructor without ID (for new order items)
    public OrderItem(int product_id, int quantity, double price_at_time, String product_name) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.price_at_time = price_at_time;
        this.product_name = product_name;
    }

    // Calculate subtotal
    public double getSubtotal() {
        return quantity * price_at_time;
    }

    // Getters and setters
    public int getOrder_item_id() { return order_item_id; }
    public void setOrder_item_id(int order_item_id) { this.order_item_id = order_item_id; }
    public int getOrder_id() { return order_id; }
    public void setOrder_id(int order_id) { this.order_id = order_id; }
    public int getProduct_id() { return product_id; }
    public void setProduct_id(int product_id) { this.product_id = product_id; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice_at_time() { return price_at_time; }
    public void setPrice_at_time(double price_at_time) { this.price_at_time = price_at_time; }
    public String getProduct_name() { return product_name; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + order_item_id +
                ", product='" + product_name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price_at_time +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}