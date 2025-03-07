// Order.java
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int order_id;
    private int customer_id;
    private String order_date;
    private double total_amount;
    private String status;
    private List<OrderItem> orderItems;

    // Constructor with ID (for existing orders)
    public Order(int order_id, int customer_id, String order_date, double total_amount, String status) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.order_date = order_date;
        this.total_amount = total_amount;
        this.status = status;
        this.orderItems = new ArrayList<>();
    }

    // Constructor without ID (for new orders)
    public Order(int customer_id) {
        this.customer_id = customer_id;
        this.status = "new";
        this.orderItems = new ArrayList<>();
        this.total_amount = 0.0;
    }

    // Add item to order
    public void addItem(OrderItem item) {
        orderItems.add(item);
        calculateTotal();
    }

    // Calculate total based on items
    public void calculateTotal() {
        this.total_amount = 0.0;
        for (OrderItem item : orderItems) {
            this.total_amount += item.getSubtotal();
        }
    }

    // Getters and setters
    public int getOrder_id() { return order_id; }
    public void setOrder_id(int order_id) { this.order_id = order_id; }
    public int getCustomer_id() { return customer_id; }
    public void setCustomer_id(int customer_id) { this.customer_id = customer_id; }
    public String getOrder_date() { return order_date; }
    public void setOrder_date(String order_date) { this.order_date = order_date; }
    public double getTotal_amount() { return total_amount; }
    public void setTotal_amount(double total_amount) { this.total_amount = total_amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + order_id +
                ", customer_id=" + customer_id +
                ", order_date='" + order_date + '\'' +
                ", total_amount=" + total_amount +
                ", status='" + status + '\'' +
                ", items=" + orderItems.size() +
                '}';
    }
}