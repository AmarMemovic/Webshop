// Customer.java
public class Customer {
    private int customer_id;
    private String first_name;
    private String last_name;
    private String email;
    private String created_at;

    // Constructor with ID (for existing customers)
    public Customer(int customer_id, String first_name, String last_name, String email, String created_at) {
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.created_at = created_at;
    }

    // Constructor without ID (for new customers)
    public Customer(String first_name, String last_name, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    // Getters and setters
    public int getCustomer_id() { return customer_id; }
    public void setCustomer_id(int customer_id) { this.customer_id = customer_id; }
    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + customer_id +
                ", name='" + first_name + " " + last_name + '\'' +
                ", email='" + email + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
