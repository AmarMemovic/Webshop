// CustomerController.java
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CustomerController {
    private Scanner scanner;
    private CustomerService customerService;

    public CustomerController(Scanner scanner) {
        this.scanner = scanner;
        this.customerService = new CustomerService();
    }

    public void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== CUSTOMER MANAGEMENT =====");
            System.out.println("1. List All Customers");
            System.out.println("2. View Customer Details");
            System.out.println("3. Register New Customer");
            System.out.println("4. Update Customer Information");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        listAllCustomers();
                        break;
                    case 2:
                        viewCustomerDetails();
                        break;
                    case 3:
                        registerCustomer();
                        break;
                    case 4:
                        updateCustomer();
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

    private void listAllCustomers() throws SQLException {
        System.out.println("\n----- All Customers -----");
        List<Customer> customers = customerService.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    private void viewCustomerDetails() throws SQLException {
        System.out.print("Enter customer ID: ");
        try {
            int customerId = Integer.parseInt(scanner.nextLine());
            Customer customer = customerService.getCustomerById(customerId);
            System.out.println("\n----- Customer Details -----");
            System.out.println(customer);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Customer ID must be a number");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void registerCustomer() throws SQLException {
        System.out.println("\n----- Register New Customer -----");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        try {
            Customer newCustomer = customerService.registerCustomer(firstName, lastName, email);
            System.out.println("Customer registered successfully!");
            System.out.println(newCustomer);
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void updateCustomer() throws SQLException {
        System.out.println("\n----- Update Customer Information -----");

        System.out.print("Enter customer ID: ");
        try {
            int customerId = Integer.parseInt(scanner.nextLine());

            // Show current details
            Customer customer = customerService.getCustomerById(customerId);
            System.out.println("Current details: " + customer);

            System.out.println("Enter new information (leave blank to keep current value)");

            System.out.print("First Name [" + customer.getFirst_name() + "]: ");
            String firstName = scanner.nextLine();
            if (firstName.trim().isEmpty()) {
                firstName = customer.getFirst_name();
            }

            System.out.print("Last Name [" + customer.getLast_name() + "]: ");
            String lastName = scanner.nextLine();
            if (lastName.trim().isEmpty()) {
                lastName = customer.getLast_name();
            }

            System.out.print("Email [" + customer.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (email.trim().isEmpty()) {
                email = customer.getEmail();
            }

            boolean updated = customerService.updateCustomer(customerId, firstName, lastName, email);
            if (updated) {
                System.out.println("Customer information updated successfully!");
            } else {
                System.out.println("No changes were made.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Customer ID must be a number");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
