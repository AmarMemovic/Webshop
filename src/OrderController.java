// OrderController.java
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class OrderController {
    private Scanner scanner;
    private OrderService orderService;
    private ProductService productService;

    public OrderController(Scanner scanner) {
        this.scanner = scanner;
        this.orderService = new OrderService();
        this.productService = new ProductService();
    }

    public void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== ORDER MANAGEMENT =====");
            System.out.println("1. View All Orders");
            System.out.println("2. View Customer Order History");
            System.out.println("3. Create New Order");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        viewAllOrders();
                        break;
                    case 2:
                        viewCustomerOrderHistory();
                        break;
                    case 3:
                        createNewOrder();
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

    private void viewAllOrders() throws SQLException {
        System.out.println("\n----- All Orders -----");
        List<Order> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        for (Order order : orders) {
            System.out.println(order);
            System.out.println("Items:");
            for (OrderItem item : order.getOrderItems()) {
                System.out.println("  " + item);
            }
            System.out.println("Total: $" + order.getTotal_amount());
            System.out.println("--------------------------");
        }
    }

    private void viewCustomerOrderHistory() throws SQLException {
        System.out.println("\n----- Customer Order History -----");

        System.out.print("Enter customer ID: ");
        try {
            int customerId = Integer.parseInt(scanner.nextLine());

            List<Order> orders = orderService.getCustomerOrderHistory(customerId);

            if (orders.isEmpty()) {
                System.out.println("No order history found for this customer.");
                return;
            }

            System.out.println("Order history for customer ID " + customerId + ":");
            for (Order order : orders) {
                System.out.println(order);
                System.out.println("Items:");
                for (OrderItem item : order.getOrderItems()) {
                    System.out.println("  " + item);
                }
                System.out.println("Total: $" + order.getTotal_amount());
                System.out.println("--------------------------");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Customer ID must be a number");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createNewOrder() throws SQLException {
        System.out.println("\n----- Create New Order -----");

        System.out.print("Enter customer ID: ");
        try {
            int customerId = Integer.parseInt(scanner.nextLine());

            Order order = orderService.createOrder(customerId);
            boolean addingItems = true;

            while (addingItems) {
                // Show all products
                List<Product> products = productService.getAllProducts();
                System.out.println("\nAvailable Products:");
                for (Product product : products) {
                    if (product.getStock_quantity() > 0) {
                        System.out.println(product.getProduct_id() + ": " + product.getName() +
                                " - $" + product.getPrice() +
                                " (Stock: " + product.getStock_quantity() + ")");
                    }
                }

                System.out.print("\nEnter product ID to add (0 to finish): ");
                int productId = Integer.parseInt(scanner.nextLine());

                if (productId == 0) {
                    addingItems = false;
                    continue;
                }

                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine());

                try {
                    orderService.addProductToOrder(order, productId, quantity);
                    System.out.println("Product added to order!");

                    // Show current order
                    System.out.println("\nCurrent Order:");
                    for (OrderItem item : order.getOrderItems()) {
                        System.out.println("  " + item);
                    }
                    System.out.println("Current Total: $" + order.getTotal_amount());

                    System.out.print("\nAdd more items? (y/n): ");
                    String answer = scanner.nextLine();
                    if (!answer.equalsIgnoreCase("y")) {
                        addingItems = false;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            // Finalize order if it has items
            if (!order.getOrderItems().isEmpty()) {
                System.out.println("\n----- Order Summary -----");
                for (OrderItem item : order.getOrderItems()) {
                    System.out.println("  " + item);
                }
                System.out.println("Total: $" + order.getTotal_amount());

                System.out.print("\nConfirm order? (y/n): ");
                String confirm = scanner.nextLine();

                if (confirm.equalsIgnoreCase("y")) {
                    Order placedOrder = orderService.placeOrder(order);
                    System.out.println("Order placed successfully! Order ID: " + placedOrder.getOrder_id());
                } else {
                    System.out.println("Order cancelled.");
                }
            } else {
                System.out.println("Order cancelled (no items added).");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
