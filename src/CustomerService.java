// CustomerService.java
import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService() {
        this.customerRepository = new CustomerRepository();
    }

    // Get all customers
    public List<Customer> getAllCustomers() throws SQLException {
        return customerRepository.findAll();
    }

    // Get customer by ID
    public Customer getCustomerById(int customerId) throws SQLException {
        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with ID: " + customerId);
        }
        return customer;
    }

    // Register new customer with validation
    public Customer registerCustomer(String firstName, String lastName, String email) throws SQLException {
        // Validate input
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        Customer newCustomer = new Customer(firstName, lastName, email);
        return customerRepository.save(newCustomer);
    }

    // Update customer with validation
    public boolean updateCustomer(int customerId, String firstName, String lastName, String email) throws SQLException {
        // Get existing customer
        Customer customer = getCustomerById(customerId);

        // Validate input
        if (firstName != null && !firstName.trim().isEmpty()) {
            customer.setFirst_name(firstName);
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            customer.setLast_name(lastName);
        }
        if (email != null && !email.trim().isEmpty()) {
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format");
            }
            customer.setEmail(email);
        }

        return customerRepository.update(customer);
    }
}