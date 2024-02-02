import java.util.ArrayList;
import java.util.List;
class ShoppingCart {
    private List<Product> cart;

    public ShoppingCart() {
        this.cart = new ArrayList<>();
    }


    public void addProduct(Product product, int quantity) {
        try {
            product.reduceQuantity(quantity);
            boolean isChanged = false;

            for (Product curPro : cart) {
                if (curPro.getProductName().equals(product.getProductName())) {
                    curPro.setProductQuantity(curPro.getQuantity() + quantity);
                    isChanged = true;
                    break;
                }
            }

            if (!isChanged) {
                if (product instanceof Phone) {
                    Phone phone = (Phone) product;
                    Phone newProduct = new Phone(
                            phone.getProductName(),
                            phone.getProductPrice(),
                            quantity,
                            phone.getScreenSize(),
                            phone.getBrand(),
                            phone.getModel(),
                            phone.getOs()
                    );
                    cart.add(newProduct);
                } else if (product instanceof Dryer) {
                    Dryer dryer = (Dryer) product;
                    Dryer newProduct = new Dryer(
                            dryer.getProductName(),
                            dryer.getProductPrice(),
                            quantity,
                            dryer.getCanConnect(),
                            dryer.getBrand(),
                            dryer.getPower()
                    );
                    cart.add(newProduct);
                } else {
                    System.out.println("Продукт не найден!");
                }
            }
        } catch (Error err) {
            System.out.println(err.getMessage());
        }
    }

//    public void addProduct(Product product, int quantity) {
//        try{
//            product.reduceQuantity(quantity);
//            boolean isChanged = false;
//            for(Product curPro : cart){
//                if(curPro.getProductName().equals(product.getProductName())) {
//                    curPro.setProductQuantity(curPro.getQuantity()+quantity);
//                    isChanged = true;
//                    break;
//                }
//            }
//            if(!isChanged) {
//                Product newProduct = new Product(product.getProductName(), product.getProductPrice(), quantity);
//                cart.add(newProduct);
//            }
//        } catch(Error err) {
//            System.out.println(err.getMessage());
//        }
//
//    }

    public double calculateTotal() {
        double total = 0;
        for (Product product : cart) {
            total += product.getProductPrice() * product.getQuantity();
        }
        return total;
    }
    public void removeFromCart(Product product){
        cart.remove(product);
    }

    public void displayCart() {
        System.out.println("Shopping Cart:");
        for (Product product : cart) {
            System.out.printf("%s - Price: $%.2f, Quantity: %d%n",
                    product.getProductName(), product.getProductPrice(), product.getQuantity());
        }
        System.out.printf("Total: $%.2f%n", calculateTotal());
    }
}
