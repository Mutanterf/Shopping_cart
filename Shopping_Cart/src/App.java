import java.sql.*;


public class App {

    private final String url = "jdbc:postgresql://db-postgresql-ams3-47505-do-user-15053769-0.c.db.ondigitalocean.com:25060/defaultdb";
    private final String user = "doadmin";
    private final String password = "AVNS_w_VlxtdGYg344M9PbZC";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void displayProducts() throws SQLException {
        Statement stmt = null;
        Connection conn = null;
        App app = new App();
        conn = app.connect();
        stmt = conn.createStatement();
        String sql;
        sql = "SELECT p.name, p.price,i.quantity FROM Inventory i inner join Product p on i.product_id = p.product_id;";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            int quantity = rs.getInt("quantity");

            System.out.print("Name: " + name);
            System.out.print(", price: " + price);
            System.out.println(", quantity: " + quantity);
        }
        conn.close();
    }


    public void createProduct(Product product) {
        try (Connection conn = connect()) {
            // Insert into the Product table
            String productSql = "INSERT INTO Product(name, price) VALUES (?, ?) RETURNING product_id";
            try (PreparedStatement productStatement = conn.prepareStatement(productSql)) {
                productStatement.setString(1, product.getProductName());
                productStatement.setDouble(2, product.getProductPrice());

                // Execute the statement and get the last inserted product_id
                try (ResultSet resultSet = productStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int productId = resultSet.getInt("product_id");

                        // If the inserted product is a device
                        if (product instanceof Device) {
                            Device device = (Device) product;
                            // Insert into the Device table
                            String deviceSql = "INSERT INTO Device(product_id, os, screen_size) VALUES (?, ?, ?)";
                            try (PreparedStatement deviceStatement = conn.prepareStatement(deviceSql)) {
                                deviceStatement.setInt(1, productId);
                                deviceStatement.setString(2, device.getOs());
                                deviceStatement.setDouble(3, device.getScreenSize());
                                deviceStatement.executeUpdate();
                                System.out.println("Device created successfully.");
                            }
                        } else if (product instanceof Domestic) {
                            Domestic domestic = (Domestic) product;
                            // Insert into the Domestic table
                            String domesticSql = "INSERT INTO Domestic(product_id, can_connect_to_wifi) VALUES (?, ?)";
                            try (PreparedStatement domesticStatement = conn.prepareStatement(domesticSql)) {
                                domesticStatement.setInt(1, productId);
                                domesticStatement.setBoolean(2, domestic.getCanConnect());
                                domesticStatement.executeUpdate();
                                System.out.println("Domestic product created successfully.");
                            }
                        } else {
                            System.out.println("Unknown product type.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating product: " + e.getMessage());
        }
    }


    public void deleteProduct(int productId) {
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM Product WHERE product_id=?")) {
            preparedStatement.setInt(1, productId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product not found or delete failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    public void displayUserProducts(int customer_id) {
        try (Connection conn = connect();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Cart WHERE customer_id = %s".formatted(customer_id))) {
            while (resultSet.next()) {
                int id = resultSet.getInt("customer_id");
                int username = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");
                System.out.println("ID: " + id + ", Username: " + username + ", Quantity: " + quantity);
            }
        } catch (SQLException e) {
            System.out.println("Error creating customer: " + e.getMessage());
        }
    }

    public void addToCart(int customer_id, int product_id, int quantity) {
        try (Connection conn = connect()) {
            // Check if the product already exists in the cart
            String isExistsSql = "SELECT quantity FROM Cart WHERE customer_id = ? AND product_id = ?";
            try (PreparedStatement isExistsStatement = conn.prepareStatement(isExistsSql)) {
                isExistsStatement.setInt(1, customer_id);
                isExistsStatement.setInt(2, product_id);
                try (ResultSet resultSet = isExistsStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Product already exists in the cart, update the quantity
                        int existingQuantity = resultSet.getInt("quantity");
                        int newQuantity = existingQuantity + quantity;

                        // Update the quantity in the Cart table
                        String updateSql = "UPDATE Cart SET quantity = ? WHERE customer_id = ? AND product_id = ?";
                        try (PreparedStatement updateStatement = conn.prepareStatement(updateSql)) {
                            updateStatement.setInt(1, newQuantity);
                            updateStatement.setInt(2, customer_id);
                            updateStatement.setInt(3, product_id);
                            updateStatement.executeUpdate();
                            System.out.println("Quantity updated in the cart.");
                        }
                    } else {
                        // Product doesn't exist in the cart, insert a new record
                        String cartSql = "INSERT INTO Cart(customer_id, product_id, quantity) VALUES(?, ?, ?)";
                        try (PreparedStatement cartStatement = conn.prepareStatement(cartSql)) {
                            cartStatement.setInt(1, customer_id);
                            cartStatement.setInt(2, product_id);
                            cartStatement.setInt(3, quantity);
                            cartStatement.executeUpdate();
                            System.out.println("Product added to the cart.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating/inserting into the cart: " + e.getMessage());
        }
    }

    public void removeFromCart(int customer_id, int product_id) {
        try (Connection conn = connect()) {
            // Check if the product exists in the cart
            String isExistsSql = "SELECT quantity FROM Cart WHERE customer_id = ? AND product_id = ?";
            try (PreparedStatement isExistsStatement = conn.prepareStatement(isExistsSql)) {
                isExistsStatement.setInt(1, customer_id);
                isExistsStatement.setInt(2, product_id);
                try (ResultSet resultSet = isExistsStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Product exists in the cart, remove it
                        String removeFromCartSql = "DELETE FROM Cart WHERE customer_id = ? AND product_id = ?";
                        try (PreparedStatement removeFromCartStatement = conn.prepareStatement(removeFromCartSql)) {
                            removeFromCartStatement.setInt(1, customer_id);
                            removeFromCartStatement.setInt(2, product_id);
                            removeFromCartStatement.executeUpdate();
                            System.out.println("Product removed from the cart.");
                        }
                    } else {
                        // Product doesn't exist in the cart
                        System.out.println("Product is not in the cart.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error removing from the cart: " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try (Connection conn = connect()) {
            String loginSql = "SELECT * FROM Customer WHERE username = ? AND password = ?";
            try (PreparedStatement loginStatement = conn.prepareStatement(loginSql)) {
                loginStatement.setString(1, username);
                loginStatement.setString(2, password);

                try (ResultSet resultSet = loginStatement.executeQuery()) {
                    return resultSet.next();  // Return true if there is at least one matching user
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            return false;
        }
    }


    public void registerUser(User user) {
        try (Connection conn = connect()) {
            Customer customer = (Customer) user;
            String customerSql = "INSERT INTO Customer(username, password, email) VALUES (?, ?, ?) RETURNING customer_id";
            try (PreparedStatement customerStatement = conn.prepareStatement(customerSql)) {
                customerStatement.setString(1, customer.getUsername());
                customerStatement.setString(2, customer.getPassword());  // Corrected to use getPassword()
                customerStatement.setString(3, customer.getEmail());

                // Execute the statement and get the last inserted customer_id
                try (ResultSet resultSet = customerStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int customerId = resultSet.getInt("customer_id");

                        // If the user is a Staff, insert into the Staff table
                        if (user instanceof Staff) {
                            Staff staff = (Staff) user;
                            String staffSql = "INSERT INTO Staff(customer_id, discount) VALUES (?, ?)";
                            try (PreparedStatement staffStatement = conn.prepareStatement(staffSql)) {
                                staffStatement.setInt(1, customerId);
                                staffStatement.setDouble(2, staff.getDiscount());
                                staffStatement.executeUpdate();
                            }
                        }

                        System.out.println("Customer created successfully.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating customer: " + e.getMessage());
        }
    }

}