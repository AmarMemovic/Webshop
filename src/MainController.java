// MainController.java
import java.util.Scanner;

public class MainController {
    private Scanner scanner;
    private CustomerController customerController;
    private ProductController productController;
    private OrderController orderController;

    public MainController() {
        this.scanner = new Scanner(System.in);
        this.customerController = new CustomerController(scanner);
        this.productController = new ProductController(scanner);
        this.orderController = new OrderController(scanner);
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== E-COMMERCE SYSTEM =====");
            System.out.println("1. Customer Management");
            System.out.println("2. Product Management");
            System.out.println("3. Order Management");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        customerController.showMenu();
                        break;
                    case 2:
                        productController.showMenu();
                        break;
                    case 3:
                        orderController.showMenu();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Exiting application...");
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

        // Close database connection
        DatabaseConnection.getInstance().closeConnection();
    }
}
