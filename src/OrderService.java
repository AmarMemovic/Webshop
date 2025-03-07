// OrderService.java
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;

    public OrderService() {
        this.orderRepository = new OrderRepository();
        this.productRepository = new ProductRepository();
        this.customerRepository = new CustomerRepository();
    }

    // Get all orders
    public List<Order> getAllOrders() throws SQLException {
        return orderRepository.findAll();
    }

    // Get order by ID
    public Order getOrderById(int orderId) throws SQLException {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        return order;
    }

    // Get order history for a customer
    public List<Order> getCustomerOrderHistory(int customerId) throws SQLException {
        // Check if customer exists
        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with ID: " + customerId);
        }

        return orderRepository.findByCustomerId(customerId);
    }

    // Create a new order
    public Order createOrder(int customerId) throws SQLException {
        // Check if customer exists
        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with ID: " + customerId);
        }

        return new Order(customerId);
    }

    // Add product to order
    public void addProductToOrder(Order order, int productId, int quantity) throws SQLException {
        // Validate quantity
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        // Check if product exists and has sufficient stock
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }

        if (product.getStock_quantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
        }

        // Create and add order item
        OrderItem item = new OrderItem(
                productId,
                quantity,
                product.getPrice(),
                product.getName()
        );

        order.addItem(item);
    }

    // Complete an order
    public Order placeOrder(Order order) throws SQLException {
        if (order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot place an empty order");
        }

        // Start transaction to place order and update stock
        Order savedOrder = orderRepository.save(order);

        // Update stock for each product
        for (OrderItem item : savedOrder.getOrderItems()) {
            productRepository.reduceStock(item.getProduct_id(), item.getQuantity());
        }

        return savedOrder;
    }
}
