import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 6171789915770113791L;
    protected String CustomerId;
    protected String name;
    protected boolean isVip;
    protected Menu menu;
    private transient final Scanner sc = new Scanner(System.in);
    public ByteMe obj_of_byte_me;
    public Customer(String CustomerID) {
        // Check if the customer with the given ID already exists in the customerList
        Customer existingCustomer = ByteMe.customerList.get(CustomerID);
        if (existingCustomer != null) {
            // Copy all fields from the existing customer to this new instance
            this.CustomerId = existingCustomer.CustomerId;
            this.name = existingCustomer.name;
            this.isVip = existingCustomer.isVip;
            this.menu = existingCustomer.menu;
            this.obj_of_byte_me = existingCustomer.obj_of_byte_me;
            this.order = existingCustomer.order;

            System.out.println("Existing customer found. This instance points to the same customer: " + CustomerID);
        } else {
            // Handle the case where the customer ID does not exist in the list
            System.out.println("No customer found with ID " + CustomerID + ". New customer must be created separately.");
        }
    }


    public Customer(String name, boolean isVip)
    {
        this.name = name;
        this.isVip = isVip;
    }
    public Customer()
    {}
    protected Order order = new Order(this.CustomerId,obj_of_byte_me);
    public void CustomerWelcome(ByteMe byteMe) {
        Scanner sc1 = new Scanner(System.in);
        order.SetCustomerID(this.CustomerId);
        this.menu = byteMe.menu;
        this.obj_of_byte_me = byteMe;
        System.out.println("=================================");
        System.out.println("      Welcome to Byte Me! ");
        System.out.println("=================================");
        if(this.name == null)
        {
            System.out.print("Please enter your name: ");
            this.name = sc1.nextLine();
            System.out.println("Your Customer ID (please remember it): " + this.CustomerId);
        }
        else{
            System.out.println("your Name is : " + this.name);
        }

        if(!isVip)
        {
            System.out.print("Would you like to join the VIP List? (Yes/No): ");
            String getVip = sc1.nextLine();
            if (getVip.equalsIgnoreCase("yes")) {
                getVipSubscription();
            }
            else{
                byteMe.CheckerCustomerList.addLast(this.order);
            }
        }



        boolean continueSession = true;
        while (continueSession) {
            System.out.println("\n=========== Your Options ===========");
            System.out.println("1. Browse Menu");
            System.out.println("2. Manage Cart");
            System.out.println("3. Track Order");
            System.out.println("4. Review Item");
            System.out.println("5. Exit");
            System.out.print("Please select an option by entering its number: ");

            int taskNumber;
            try {
                taskNumber = sc1.nextInt();
                sc1.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("\nInvalid input! Please enter a number from the options above.");
                sc1.nextLine(); // clear invalid input
                continue;
            }

            switch (taskNumber) {
                case 1 -> BrowseMenu();
                case 2 -> CartOperation();
                case 3 -> OrderTracking();
                case 4 -> ItemReview();
                case 5 -> {
                    System.out.println("\nThank you for visiting Byte Me, " + name + "! Goodbye!");
                    continueSession = false;
                }
                default -> System.out.println("\nInvalid selection! Please choose a number between 1 and 5.");
            }
        }
    }

    public void setCustomerId(String id) {
        this.CustomerId = id;
    }

    public String getCustomerId(){
        return this.CustomerId;
    }

    public void getVipSubscription() {
        Scanner sc1 = new Scanner(System.in);
        System.out.println("\nTo join the VIP list, a payment of 500 INR is required.");
        System.out.println("========== Payment Screen ==========");
        System.out.print("Enter amount (or enter -1 if you don't wish to proceed): ");

        int amount = sc1.nextInt();
        sc1.nextLine(); // consume newline

        if (amount >= 500) {
            this.isVip = true;
            ByteMe.PendingVipCustomer++;
            obj_of_byte_me.CheckerCustomerList.add(ByteMe.PendingVipCustomer,this.order);
            System.out.println("Payment successful! Congratulations, you are now a VIP customer!");
        } else if (amount == -1) {
            System.out.println("You chose not to join the VIP list. You can still enjoy our standard services!");
        } else {
            System.out.println("Insufficient amount entered. Returning to main menu.");
            System.out.println("Refund Transferred Back Successfully of INR " + amount);
        }
    }

    // Placeholder methods for each task
    public void BrowseMenu() {
        Scanner sc1 = new Scanner(System.in);
        boolean continueBrowsing = true;

        while (continueBrowsing) {
            System.out.println("\n=== Browse Menu ===");
            System.out.println("1. View All Items");
            System.out.println("2. Search by Name");
            System.out.println("3. Filter by Category");
            System.out.println("4. Sort by Price");
            System.out.println("5. Exit to Main Menu");
            System.out.print("Select an option by entering its number: ");

            int choice;
            try {
                choice = sc1.nextInt();
                sc1.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("\nInvalid input! Please enter a valid number.");
                sc1.nextLine(); // clear invalid input
                continue;
            }

            switch (choice) {
                case 1 -> menu.showMenu(obj_of_byte_me);  // Display all items using Menu's showMenu method
                case 2 -> menu.SearchByName();  // Search functionality using Menu's SearchByName method
                case 3 -> {
                    // Filter by category using Menu's sortByCategory method
                    List<FoodItems> categoryItems = menu.sortByCategory();
                    if (categoryItems != null && !categoryItems.isEmpty()) {
                        System.out.println("Dish Name---------- Amount(INR) ---------- Available -- Rating\n");
                        categoryItems.forEach(menu::showDetails);
                    } else {
                        System.out.println("No items found in the specified category.");
                    }
                }
                case 4 -> sortItemsByPrice();  // Sorting by price using Menu's sortByPrice
                case 5 -> {
                    System.out.println("Exiting browse menu...");
                    continueBrowsing = false;
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    protected void SetCustomerId(String id)
    {
        this.CustomerId = id;
    }
    private void sortItemsByPrice() {
        Scanner sc1 = new Scanner(System.in);
        System.out.print("\nEnter the maximum price to filter items: ");
        int maxPrice;

        try {
            maxPrice = sc1.nextInt();
            sc1.nextLine(); // consume newline
        } catch (Exception e) {
            System.out.println("Invalid input! Please enter a valid integer price.");
            sc1.nextLine(); // clear invalid input
            return;
        }

        // Filter items with price less than or equal to maxPrice and sort in descending order
        List<FoodItems> filteredItems = menu.MenuListByPrice.values().stream()
                .flatMap(List::stream)
                .filter(item -> item.getPrice() <= maxPrice)
                .sorted(Comparator.comparingInt(FoodItems::getPrice).reversed())  // Sort in descending order
                .toList();

        if (filteredItems.isEmpty()) {
            System.out.println("No items found below the entered price of " + maxPrice + " INR.");
        } else {
            System.out.println("Items below " + maxPrice + " INR (sorted by price in descending order):");
            System.out.println("\nDish Name---------- Amount(INR) ---------- Available -- Rating\n");
            filteredItems.forEach(menu::showDetails);
        }
    }

    public void CartOperation() {
        Scanner sc1 = new Scanner(System.in);
        // Create a new Order instance for the customer
        boolean continueCartOperations = true;

        while (continueCartOperations) {
            System.out.println("\n========= Cart Operations =========");
            System.out.println("1. Add Items to Cart");
            System.out.println("2. Modify Quantities");
            System.out.println("3. Remove Items");
            System.out.println("4. View Total");
            System.out.println("5. Checkout");
            System.out.println("6. Exit to Main Menu");
            System.out.print("Please select an option by entering its number: ");

            int choice;
            try {
                choice = sc1.nextInt();
                sc1.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("\nInvalid input! Please enter a number from the options above.");
                sc1.nextLine(); // clear invalid input
                continue;
            }

            switch (choice) {
                case 1 -> order.AddItems(menu,null,0,0); // Call method to add items
                case 2 -> order.modifyQuantities(menu); // Call method to modify item quantities
                case 3 -> order.removeItems(menu); // Call method to remove items
                case 4 -> order.viewTotal(menu); // Call method to view total price
                case 5 -> {
                    order.PaymentCheckOut(); // Proceed to checkout
                }
                case 6 -> {
                    System.out.println("Exiting cart operations...");
                    continueCartOperations = false; // Exit to the main menu
                }
                default -> System.out.println("\nInvalid selection! Please choose a number between 1 and 6.");
            }
        }
    }

    public void OrderTracking() {
        Scanner sc1 = new Scanner(System.in);
        boolean continueOrderTracking = true;

        while (continueOrderTracking) {
            System.out.println("\n========= Order Tracking Menu =========");
            System.out.println("1. View Current Order Status");
            System.out.println("2. Cancel Order");
            System.out.println("3. View Order History");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Please select an option by entering its number: ");

            int choice;
            try {
                choice = sc1.nextInt();
                sc1.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("\nInvalid input! Please enter a number from the options above.");
                sc1.nextLine(); // clear invalid input
                continue;
            }

            switch (choice) {
                case 1 -> {
                    if (order != null) {
                        order.ViewOrderStatus(); // Call method to view order status
                    } else {
                        System.out.println("No current order found. Please place an order first.");
                    }
                }
                case 2 -> {
                    if (order != null) {
                        order.CancelOrder(); // Call method to cancel the order
                    } else {
                        System.out.println("No current order to cancel.");
                    }
                }
                case 3 -> {
                    order.OrderHistory(); // Call method to view order history
                }
                case 4 -> {
                    System.out.println("Exiting from Order Tracking operations...");
                    continueOrderTracking = false; // Exit to the main menu
                }
                default -> System.out.println("\nInvalid selection! Please choose a valid number between 1 and 4.");
            }
        }
    }

    public void ItemReview() {
        Scanner sc1 = new Scanner(System.in);
        boolean continueItemReview = true;

        while (continueItemReview) {
            System.out.println("\n========= Item Review =========");
            System.out.println("1. Provide Review");
            System.out.println("2. View Reviews");
            System.out.println("3. Exit to Main Menu");
            System.out.print("Please select an option by entering its number: ");

            int choice;
            try {
                choice = sc1.nextInt();
                sc1.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("\nInvalid input! Please enter a number from the options above.");
                sc1.nextLine(); // clear invalid input
                continue;
            }

            switch (choice) {
                case 1 -> {
                    order.ProvideReview();
                }
                case 2 -> {
                    order.ViewReviews();
                }
                case 3 -> {
                    System.out.println("Exiting from Item Review operations...");
                    continueItemReview = false; // Exit to the main menu
                }
                default -> System.out.println("\nInvalid selection! Please choose a valid number between 1 and 4.");
            }
        }
    }
}
