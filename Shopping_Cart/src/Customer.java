import java.util.ArrayList;
import java.util.List;


abstract class User {
    private String username;
    private String password;
    private int userId;
    private String email;
    final ShoppingCart cart;


    public User(String username, String password, int userId, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.cart = new ShoppingCart();
    }

    public abstract void displayInfo();


    public int getUserId() {
        return userId;
    }

    public void setCustomerId(int userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void buyProduct() {}
}

class Customer extends User {
    public Customer(String username, String password, int userId, String email) {
        super(username, password, userId, email);
    }

    @Override
    public void displayInfo() {
        System.out.println("Customer Information:");
        System.out.println("Username: " + getUsername());
    }

    public void buyProduct(Product product, int quantity) {
        cart.addProduct(product, quantity);
    }

    public void displayCart() {
        cart.displayCart();
    }

    public void buyProduct() {

    }
}

class Staff extends User {
    private String department;

    private final double discount;

    public Staff(String username, String password, String department, int userId, String email, double discount) {
        super(username, password, userId, email);
        this.department = department;
        this.discount = discount;
    }

    @Override
    public void displayInfo() {
        System.out.println("Staff Information:");
        System.out.println("Username: " + getUsername());
        System.out.println("Department: " + department);
    }

    public String getDepartment() {
        return department;
    }

    public double getDiscount() {
        return discount;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public Phone createPhone(String productName, double price, int quantity, double screenSize, String brand, String model, String os) {
        return new Phone(productName, price, quantity, screenSize, brand, model, os);
    }

    public Dryer createDryer(String productName, double price, int quantity, boolean canConnectToWiFi, String brand, double power) {
        return new Dryer(productName, price, quantity, canConnectToWiFi, brand, power);
    }

}