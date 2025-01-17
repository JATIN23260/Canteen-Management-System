import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;

public class ByteMe implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient Scanner sc = new Scanner(System.in); // Transient field
    protected static TreeMap<String, Customer> customerList = new TreeMap<>();
    protected LinkedList<Order> CheckerCustomerList = new LinkedList<>();
    protected static int PendingVipCustomer = 0;
    protected static LinkedList<Order> NonPendingOrders = new LinkedList<>();
    protected static Menu menu = new Menu(); // Ensure Menu implements Serializable
    protected static LinkedList<Order> RefundOrders = new LinkedList<>();
    public transient EnterPanels Panel = null; // Mark non-serializable objects as transient
    private static final String FILE_PATH = "C://Users//ADMIN//OneDrive//Desktop//BYTE_ME_APP//src//CustomerSerialize"; // Use a relative file path
    private transient boolean isGUIInititalized = false;
    public void Main() {

        TreeMap<Integer, Admin> adminList = new TreeMap<>();
        Customer obj = new Customer("Jatin", true);
        CheckerCustomerList.add(PendingVipCustomer, obj.order);
        loadCustomerList();
        obj.setCustomerId("123hkH");
        System.out.println("Reference : " + "123hkH");
        customerList.putIfAbsent("123hkH", obj);

        Customer obj2 = new Customer("Ashutosh", true);
        customerList.putIfAbsent("123abc", obj2);
        obj2.setCustomerId("123abc");
        CheckerCustomerList.add(PendingVipCustomer, obj2.order);
        System.out.println("Reference : " + obj2.getCustomerId());
        PendingVipCustomer++;

        Customer obj3 = new Customer("Kunnal", false);
        customerList.putIfAbsent("123pqr", obj3);
        CheckerCustomerList.addLast(obj3.order);
        obj3.setCustomerId("123hst");

        Customer obj4 = new Customer("Anuj", false);
        customerList.putIfAbsent("1245itq", obj4);
        obj4.setCustomerId("1245itq");

        CheckerCustomerList.addLast(obj4.order);
        initializeGUI();
        while (true) {
            System.out.println("================================");
            System.out.println("      Welcome to Byte Me!       ");
            System.out.println("================================");
            System.out.println("Please identify yourself:");
            System.out.println("1. Customer");
            System.out.println("2. Admin");
            System.out.println("3. Exit");
            System.out.println("4. Clear All Data from file");
            System.out.print("\nYour choice: ");

            int identify;
            try {
                identify = sc.nextInt();
                sc.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("\nInvalid input! Please enter a number.");
                sc.nextLine(); // clear invalid input
                continue;
            }

            switch (identify) {
                case 1 -> IdentifyCustomer(customerList, menu);
                case 2 -> IdentifyAdmin(adminList, menu);
                case 3 -> {
                    System.out.println("Thank you for using Byte Me! Goodbye!");
                    System.exit(0);
                }
                case 4 -> {
                    System.out.print("Are you sure you want to clear all data? (yes/no): ");
                    String confirmation = sc.nextLine();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        clearData();
                        System.out.println("All data has been cleared.");
                    } else {
                        System.out.println("Data clearing canceled.");
                    }
                }

                default -> System.out.println("\nInvalid selection. Please try again.\n");
            }
        }
    }

    public void clearData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            // Overwrite with empty structures
            oos.writeObject(new TreeMap<String, Customer>()); // Empty customerList
            oos.writeObject(new LinkedList<Order>());        // Empty CheckerCustomerList
            System.out.println("All data has been cleared.");
        } catch (IOException e) {
            System.out.println("Error clearing data: " + e.getMessage());
        }

        // Clear in-memory data as well
        customerList.clear();
        CheckerCustomerList.clear();
        PendingVipCustomer = 0;
        NonPendingOrders.clear();
        RefundOrders.clear();
    }

    protected void IdentifyCustomer(TreeMap<String, Customer> customerList, Menu menu) {
        System.out.print("\nEnter your Customer ID (or enter -1 to create a new account): ");
        String id = sc.nextLine();
        Customer newOrExistingCustomer = null;

        if (id.equalsIgnoreCase("-1")) {
            newOrExistingCustomer = new Customer();
            String SetPassword;
            System.out.print("Please Set your CustomerID (kinda Password) : ");
            SetPassword = sc.nextLine();
            customerList.putIfAbsent(SetPassword, newOrExistingCustomer);
            newOrExistingCustomer.setCustomerId(SetPassword);
            System.out.println("\nNew customer account created! Your ID is: " + newOrExistingCustomer.getCustomerId());
            newOrExistingCustomer.CustomerWelcome(this);
        } else {
            newOrExistingCustomer = CustomerDoesExist(id);
            if (newOrExistingCustomer != null) {
                System.out.println("\nWelcome back, Customer " + newOrExistingCustomer.getCustomerId() + "!");
                newOrExistingCustomer.CustomerWelcome(this);
            } else {
                System.out.println("\nInvalid ID. Please try again.");
                IdentifyCustomer(customerList, menu);
            }
        }
        saveCustomerList();
    }

    public Customer CustomerDoesExist(String CustomerId)
    {
        return customerList.get(CustomerId);
    }


    protected void IdentifyAdmin(TreeMap<Integer, Admin> adminList, Menu menu) {
        System.out.print("\nEnter your Admin ID (or enter -1 to create a new admin account): ");
        int id = sc.nextInt();
        sc.nextLine(); // consume newline

        if (id == -1) {
            Admin newAdmin = new Admin();
            adminList.putIfAbsent(newAdmin.hashCode(), newAdmin);
            newAdmin.setAdminId(newAdmin.hashCode());
            System.out.println("\nNew admin account created! Your ID is: " + newAdmin.getAdminId());
            newAdmin.AdminWelcome(this);
        } else {
            Admin existingAdmin = adminList.get(id);
            if (existingAdmin != null) {
                System.out.println("\nWelcome back, Admin " + id + "!");
                existingAdmin.AdminWelcome(this);
            } else {
                System.out.println("\nInvalid ID. Please try again.");
                IdentifyAdmin(adminList, menu);
            }
        }
    }



    public void saveCustomerList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(customerList);          // Save the customer list
            oos.writeObject(CheckerCustomerList);   // Save the CheckerCustomerList
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }


public void loadCustomerList() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
        customerList = (TreeMap<String, Customer>) ois.readObject();    // Read the customer list
        CheckerCustomerList = (LinkedList<Order>) ois.readObject();      // Read the CheckerCustomerList
        System.out.println("Data loaded successfully.");
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error loading data: " + e.getMessage());
    }
}


    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        sc = new Scanner(System.in); // Reinitialize transient Scanner
        isGUIInititalized = false;  // Reset the flag; GUI will be re-initialized in Main
        Panel = null;               // Ensure no stale Panel instance
    }
    private void initializeGUI() {
        if (!isGUIInititalized) {
            this.Panel = new EnterPanels(this); // Create the GUI
            this.isGUIInititalized = true;      // Mark as initialized
            System.out.println("GUI initialized.");
        }
    }

}
