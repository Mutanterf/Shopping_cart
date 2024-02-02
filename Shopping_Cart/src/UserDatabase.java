//import java.util.ArrayList;
//import java.util.List;
//
//public class UserDatabase {
//    private final List<Customer> customers = new ArrayList<>();
//    private final List<Staff> staff = new ArrayList<>();
//    private Customer | Staff currentUser;
//
//    public void register(Customer customer) {
//        customers.add(customer);
//    }
//
//    public void register(Staff staffMember) {
//        staff.add(staffMember);
//    }
//
//    public void logIn(String username, String password) {
//        for (Customer customer : customers) {
//            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
//                System.out.println("Customer login successful");
//                return;
//            }
//        }
//
//        for (Staff staffMember : staff) {
//            if (staffMember.getUsername().equals(username) && staffMember.getPassword().equals(password)) {
//                System.out.println("Staff login successful");
//                return;
//            }
//        }
//
//        System.out.println("Login failed. Invalid username or password.");
//    }
//}
