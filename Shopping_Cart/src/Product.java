
abstract class Product {
    private final String productName;
    private double price;
    private int quantity;

    public Product(String productName, double price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
    public void reduceQuantity(int reduceValue) {
       if(quantity <= 0) {
           throw new Error("Product out of stock");
       }
       int temp = quantity - reduceValue;
       if(temp < 0) {
           throw new Error("We don't have enough amount of product");
       }
    }

    public void setProductQuantity(int newValue) {
        this.quantity = newValue;
    }

    public void setProductPrice(double newPrice) {
        if(newPrice <= 0) {
            System.out.println("Price shouldn't be less than 0");
        } else {
            this.price = newPrice;
        }
    }

    public abstract void turnOn();
}

abstract class Device extends Product {
    private final double screenSize;
    public Device (String productName, double price, int quantity, double screenSize) {
        super(productName, price, quantity);
        this.screenSize = screenSize;
    }

    public double getScreenSize(){
        return screenSize;
    }

    public void turnOn() {
        System.out.println("Screensaver has appeared");
    }
}

abstract class Domestic extends Product {
    private final boolean canConnectToWiFi;

    public Domestic (String productName, double price, int quantity, boolean canConnectToWiFi) {
        super(productName, price, quantity);
        this.canConnectToWiFi = canConnectToWiFi;
    }

    public boolean getCanConnect(){
        return canConnectToWiFi;
    }

    public void turnOn() {
        System.out.println("Indicator lit up");
    }
}

class Phone extends Device {
    private final String brand;
    private final String model;
    private final String os;
    public Phone (String productName, double price, int quantity, double screenSize, String brand, String model, String os) {
        super(productName, price, quantity, screenSize);
        this.brand = brand;
        this.model = model;
        this.os = os;
    }
    public String getBrand(){
        return this.brand;
    }
    public String getModel(){
        return this.model;
    }
    public String getOs(){
        return this.os;
    }

    @Override
    public void turnOn() {
        System.out.println(this.os + " sсreensaver has been appeared");
    }
}

class Dryer extends Domestic {
    private final String brand;
    private final double power;

    public Dryer (String productName, double price, int quantity, boolean canConnectToWiFi, String brand, double power) {
        super(productName, price, quantity, canConnectToWiFi);
        this.brand = brand;
        this.power = power;
    }

    public String getBrand() {
        return this.brand;
    }

    public double getPower() {
       return this.power;
    }

    @Override
    public void turnOn() {
        System.out.println("Dryer is making noise");
    }
}


