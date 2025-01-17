import com.sun.source.tree.Tree;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Ref;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = -2466996617081596737L; // Match the serialized file
    protected  TreeMap<String, List<FoodItems>> CartOrderList = new TreeMap<>();
    protected String CustomerId;
    protected TreeMap<String, String> OrderStatus = new TreeMap<>();
    protected Menu menu = ByteMe.menu;
    private transient final Scanner sc = new Scanner(System.in);
    protected String DeliveryAddress;
    protected int FinalAmount;
    protected boolean paymentDone;
    protected List<String> DeniedORCanceled = new ArrayList<>();
    public PendingOrder pendingOrder;
    public TreeMap<String,List<String>> ItemReview = new TreeMap<>();
    public static TreeMap<String,List<String>> SpecialReviews = new TreeMap<>();
    public ByteMe byteMe;
    public int AmountPaid = 0;
    // Order Status -> Preparing, out for Delivery, Denied, Cancel, Delivered
    public Order(String customerId,ByteMe obj_of_byte_me) {
        this.CustomerId = customerId;
        this.byteMe = obj_of_byte_me;
    }
    public Order(ByteMe byteMe)
    {
        this.byteMe = byteMe;
    }
    public void AddItems(Menu menu,String ItemName,int number_of_item,int Single_item) {
        this.menu = menu;
        Scanner sc1 = new Scanner(System.in);
        boolean additionItem = true;
        System.out.println("\n============= BOOK ORDER ============");

        while (additionItem) {
            String name;
            if (ItemName == null) {
                System.out.print("Enter the Name of Dish: ");
                name = sc1.nextLine();
            } else {
                name = ItemName;
            }
            // Find the correct case-sensitive name key from the menu
            String correctName = menu.MenuList.keySet().stream()
                    .filter(key -> key.equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);

            if (correctName != null) {
                FoodItems item = menu.MenuList.get(correctName);

                // Check if the item is available
                if (!item.isAvailable()) {
                    System.out.println(correctName + " is currently unavailable. Please choose another dish.");
                    return ;
                } else {
                    System.out.print("Enter the quantity: ");
                    int quantity;
                    if(number_of_item == 0)
                    {
                        quantity = sc1.nextInt();
                        sc1.nextLine();
                    }
                     else{
                         quantity = number_of_item;
                    }

                    // Add the specified quantity of the item to CartOrderList
                    CartOrderList.computeIfAbsent(correctName, k -> new ArrayList<>());
                    for (int i = 0; i < quantity; i++) {
                        CartOrderList.get(correctName).add(item);
                    }
                    System.out.println(quantity + "x " + item.getName() + " added to the cart.");
                }
            } else {
                System.out.println("There is no such dish. Try again!");
            }


            if(Single_item == 0)
            {
                System.out.print("Do you want to add another item? (Yes/No): ");
            }
            else{
                break;
            }
            String wantToExit = sc1.nextLine();
            additionItem = wantToExit.equalsIgnoreCase("yes");
        }

        System.out.println("\nItems successfully added to your cart.\n");
        return ;
    }


    // Method to display items in the cart with quantities
    public void viewCart() {
        if (CartOrderList.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("=== YOUR CART ===");
        CartOrderList.forEach((name, items) -> {
            int quantity = items.size();
            FoodItems item = items.get(0);  // Since all items in the list are the same, pick the first
            System.out.printf("%-25s x %d - %4d INR each (Available: %-3s) | Rating: %.1f/5.0%n",
                    name, quantity, item.getPrice(), item.isAvailable() ? "Yes" : "No", item.getRating());
        });
        System.out.println("=================\n");
    }
    public void modifyQuantities(Menu menu) {
        this.menu = menu;
        Scanner sc1 = new Scanner(System.in);
        System.out.println("\n========== Modify Cart ==========");
        boolean modificationDone = false;

        while (!modificationDone) {
            // Prompt user for item name to modify
            System.out.print("\nEnter the name of the dish to modify: ");
            String name = sc1.nextLine();

            // Find case-insensitive key match in CartOrderList
            String correctName = CartOrderList.keySet().stream()
                    .filter(key -> key.equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);

            if (correctName != null) {
                // Retrieve current item and quantity
                List<FoodItems> itemList = CartOrderList.get(correctName);
                int currentQuantity = itemList.size();
                FoodItems item = itemList.get(0);

                // Display current quantity and details
                System.out.printf("\nCurrent quantity of %s: %d (Price per item: %d INR)\n",
                        correctName, currentQuantity, item.getPrice());
                System.out.print("Enter the new quantity (or enter 0 to remove from cart): ");
                int modifiedQuantity = sc1.nextInt();
                sc1.nextLine(); // Consume newline

                // Update quantity based on user input
                if (modifiedQuantity > currentQuantity) {
                    for (int i = 0; i < (modifiedQuantity - currentQuantity); i++) {
                        itemList.add(item);
                    }
                    System.out.println("Increased quantity to " + modifiedQuantity);
                } else if (modifiedQuantity < currentQuantity && modifiedQuantity > 0) {
                    for (int i = 0; i < (currentQuantity - modifiedQuantity); i++) {
                        itemList.remove(item);
                    }
                    System.out.println("Decreased quantity to " + modifiedQuantity);
                } else if (modifiedQuantity == 0) {
                    CartOrderList.remove(correctName);
                    System.out.println(correctName + " has been removed from your cart.");
                } else {
                    System.out.println("Invalid quantity entered! Please try again.");
                    continue;
                }
            } else {
                System.out.println("Dish not found in your cart! Please enter a valid name.");
                continue;
            }

            // Check if user wants to modify another item
            System.out.print("\nDo you want to modify another item? (Yes/No): ");
            String wantToContinue = sc1.nextLine();
            modificationDone = !wantToContinue.equalsIgnoreCase("yes");
        }

        System.out.println("\nYour cart has been successfully updated.\n");
//        PendingOrder.updateCheckerCustomerList(ByteMe.CheckerCustomerList);
    }
    public void removeItems(Menu menu) {
        this.menu = menu;
        Scanner sc1 = new Scanner(System.in);
        System.out.println("\n============ Remove Items ============");
        boolean removalDone = false;

        while (!removalDone) {
            // Display current cart contents before removal
            viewCart();

            // Prompt user for item name to remove
            System.out.print("\nEnter the name of the dish to remove: ");
            String name = sc1.nextLine();

            if(CartOrderList.isEmpty())
            {
                System.out.println("Please Select Order !");
                return ;
            }
            // Find case-insensitive key match in CartOrderList
            String correctName = CartOrderList.keySet().stream()
                    .filter(key -> key.equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);

            if (correctName != null) {
                // Retrieve item details for display
                List<FoodItems> itemList = CartOrderList.get(correctName);
                int quantity = itemList.size();
                FoodItems item = itemList.getFirst();

                Set<String> keys = OrderStatus.keySet();
                for(String key : keys)
                {
                    if(key.equals(correctName))
                    {
                        OrderStatus.remove(key);
                    }
                }

                // Confirm item removal with quantity details
                System.out.printf("Are you sure you want to remove %d x %s (Price per item: %d INR)? (Yes/No): ",
                        quantity, correctName, item.getPrice());
                String confirmation = sc1.nextLine();

                if (confirmation.equalsIgnoreCase("yes")) {
                    // Remove item from CartOrderList
                    CartOrderList.remove(correctName);
                    System.out.println(correctName + " has been successfully removed from your cart.");
                } else {
                    System.out.println("Removal canceled.");
                }
            } else {
                System.out.println("Dish not found in your cart! Please enter a valid name.");
                continue;
            }

            // Check if user wants to remove another item
            System.out.print("\nDo you want to remove another item? (Yes/No): ");
            String wantToContinue = sc1.nextLine();
            removalDone = !wantToContinue.equalsIgnoreCase("yes");
        }

        System.out.println("\nYour cart has been successfully updated.\n");
//        PendingOrder.updateCheckerCustomerList(ByteMe.CheckerCustomerList);
    }


    public void viewTotal(Menu menu) {
        this.menu = menu;
        Scanner sc1 = new Scanner(System.in);
        if (CartOrderList.isEmpty()) {
            System.out.println("Your cart is empty. Please add items before viewing the total.");
            return;
        }

        System.out.println("\n============ VIEW TOTAL ============");
        AtomicInteger totalAmount = new AtomicInteger();

        // Display each item with quantity, price per item, and total price for that item
        System.out.println("Items in your cart:");
        CartOrderList.forEach((name, items) -> {
            int quantity = 0;
            for(FoodItems foodItems : items)
            {
//                if(!(foodItems.checkOut))
//                {
//                    quantity++;
//                }
                quantity++;
            }
            FoodItems item = items.getFirst();  // Retrieve the item from the list
            int itemTotal = item.getPrice() * quantity;
            totalAmount.addAndGet(itemTotal);
            System.out.printf("%-25s x %d - %4d INR each | Subtotal: %5d INR%n",
                    name, quantity, item.getPrice(), itemTotal);
        });

        // Display the grand total amount
        System.out.println("-------------------------------------");
        System.out.printf("Grand Total: %35d INR%n", totalAmount.get());
        FinalAmount = totalAmount.get();
        System.out.println("=====================================\n");

        // Confirm if the user wants to proceed with this total
        System.out.print("Would you like to finalize your order with this total? (Yes/No): ");
        String confirmation = sc1.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Thank you! Your order is being processed.");
            System.out.println("Please Finalize your Payment Soon !");
            // Additional logic to finalize the order can be added here
        } else {
            System.out.println("Order not finalized. You can modify items in your cart or add more items.");
        }
    }

    public void PaymentCheckOut() {
        Scanner sc1 = new Scanner(System.in);
        if (CartOrderList.isEmpty()) {
            System.out.println("Your cart is empty. Please add items before checking out.");
            return;
        }

        // Loop to ensure a valid delivery address is entered
        while (true) {
            System.out.print("Please enter your delivery address: ");
            DeliveryAddress = sc1.nextLine().trim();

            if (DeliveryAddress.isEmpty()) {
                System.out.println("Invalid address. Please provide a valid delivery address.");
            } else {
                break; // Exit loop if a valid address is entered
            }
        }

        System.out.printf("Your Grand Total Amount is: %d INR%n", (FinalAmount - AmountPaid));
        int receivedAmount = 0;

        while (true) {
            System.out.print("Enter the amount you wish to pay: ");
            try {
                receivedAmount = Integer.parseInt(sc1.nextLine());
                if (receivedAmount <= 0) {
                    System.out.println("Invalid amount. Please enter a positive number.");
                    continue;
                }
                break; // Exit loop if a valid amount is entered
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
            }
        }


        while(!paymentDone)
        {
            if (receivedAmount < FinalAmount) {
                System.out.printf("Payment is partial: You still owe %d INR.%n", FinalAmount - receivedAmount);
                FinalAmount -= receivedAmount; // Update the remaining amount
                System.out.println("Please make the full payment to complete your order.");
                if(FinalAmount == 0)
                {
                    paymentDone = true;
                    AmountPaid = receivedAmount;
                }
                 continue;
            } else if (receivedAmount > FinalAmount) {
                System.out.printf("You paid more than the total amount by %d INR.%n", receivedAmount - FinalAmount);
                System.out.printf("The excess amount of %d INR has been refunded to you.%n", receivedAmount - FinalAmount);
                AmountPaid = FinalAmount;
                paymentDone = true; // Payment is successful
            } else {
                System.out.println("Payment completed successfully. Thank you for your order!");
                paymentDone = true;// Payment is successful
            }

        }
        this.FinalAmount = 0;
        System.out.printf("Your order will be delivered to: %s%n", DeliveryAddress);
        Set<String> keys = CartOrderList.keySet();
        for(String key : keys)
        {
            OrderStatus.putIfAbsent(key,"preparing");
        }

        Set<String> key1 = CartOrderList.keySet();
        for(String key : key1)
        {
            for(FoodItems items : CartOrderList.get(key))
            {
                items.checkOut = true;
            }
        }

        System.out.println("Your order will be delivered soon.");
    }

    public void ViewOrderStatus() {
        System.out.println("======== View Order Status ===============");

        if (CartOrderList.isEmpty()) {
            System.out.println("Your cart is empty. No order status to display.");
            return;
        }
        boolean anyDeniedOrCanceled = false;

        for (String itemName : CartOrderList.keySet()) {
            String status = OrderStatus.get(itemName);
            List<FoodItems> itemList = CartOrderList.get(itemName);
            int quantity = itemList.size();
            FoodItems item = itemList.getFirst();
            int price = item.getPrice();

            // Check status and print details accordingly
            switch (status) {
                case "denied":
                case "cancelled":
                    anyDeniedOrCanceled = true;
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s%n",
                            itemName, price, quantity, status);
                    DeniedORCanceled.add(itemName);
                    break;

                case "preparing":
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s%n",
                            itemName, price, quantity, status);
                    break;

                case "out for delivery":
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s%n",itemName, price, quantity, status);
                    break;

                case "delivered":
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s%n", itemName, price, quantity, status);
                    break;

                default:
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s (Unknown status)%n",
                            itemName, price, quantity, status);
                    break;
            }
        }

        if (!anyDeniedOrCanceled) {
            System.out.println("All items are currently being processed.");
        }
        for(String key : DeniedORCanceled)
        {
            CartOrderList.remove(key);
        }
        System.out.println("========================================");
    }

    public void CancelOrder() {
        Scanner sc1 = new Scanner(System.in);
        System.out.println("============== Cancelling Order ==============");

        while (true) {
            // Display current order status for user reference
            if (OrderStatus.isEmpty()) {
                System.out.println("No items in your order to cancel.");
                break; // Exit if there are no items to cancel
            }

            System.out.println("Current Order Status:");
            OrderStatus.forEach((item, status) ->
                    System.out.printf("Item: %s | Status: %s%n", item, status));

            System.out.print("Enter the name of the dish to cancel: ");
            String dishToCancel = sc1.nextLine();
            Set<String> keys = CartOrderList.keySet();
            for(String key : keys)
            {
                if(key.equalsIgnoreCase(dishToCancel))
                {
                    dishToCancel = key;
                    break;
                }
            }
            // Check if the dish exists in the order status
            if (OrderStatus.containsKey(dishToCancel)) {
                // Confirm cancellation
                System.out.printf("Are you sure you want to cancel %s? (Yes/No): ", dishToCancel);
                String confirmation = sc1.nextLine().trim();

                if (confirmation.equalsIgnoreCase("yes")) {
                    // Update order status to cancelled
                    OrderStatus.put(dishToCancel, "cancelled");
                    if(!(ByteMe.RefundOrders.contains(this)))
                    {
                        ByteMe.RefundOrders.offer(this);
                    }
                    System.out.println(dishToCancel + " has been successfully canceled from your order.");
                } else {
                    System.out.println("Cancellation cancelled.");
                }
            } else {
                System.out.println("The specified dish was not found in your order. Please enter a valid name.");
            }

            // Ask user if they want to cancel another item
            System.out.print("Do you want to cancel any other food item? (Yes/No): ");
            String wantToContinue = sc1.nextLine().trim();

            if (!wantToContinue.equalsIgnoreCase("yes")) {
                break; // Exit loop if the user doesn't want to cancel more items
            }
        }
        System.out.println("==============================================");
    }

    public  void UpdateOrderStatus(ByteMe byteMe) {
        System.out.println("\n========== Updating All Orders ==========\n");
        Scanner sc1 = new Scanner(System.in);
        LinkedList<Order> customerList = byteMe.CheckerCustomerList;
        int i = 0;

        while (true) {
            if (i >= customerList.size()) {
                System.out.println("No more customers to update.");
                break;
            }

            Order currentOrder = customerList.get(i++);
            if(currentOrder.OrderStatus.isEmpty())
            {
                continue;
            }
            UpdateOrder(currentOrder,byteMe);

            System.out.print("\nDo you want to update orders for other customers? (Yes/No): ");
            String response = sc1.nextLine().trim();

            if (!response.equalsIgnoreCase("yes")) {
                break;
            }
        }
        System.out.println("All Updates have been reflected !");
        System.out.println("=========================================");
//        PendingOrder.updateCheckerCustomerList(ByteMe.CheckerCustomerList);
    }

    public  void UpdateOrder(Order order, ByteMe byteMe) {

        Scanner sc1 = new Scanner(System.in);
        if (order.OrderStatus.isEmpty()) {
            System.out.println("No items in your order to update status.");
            return; // Exit if there are no items to update
        }
        List<String> Fooditem = new ArrayList<>();
        while (true) {
            // Display current order statuses for user reference
            System.out.println("Current Order Status:");
            order.OrderStatus.forEach((item, status) ->
                    System.out.printf("Item: %s | Status: %s%n", item, status));

            // Prompt user for the item name to update status
            System.out.print("Enter the name of the dish to update status: ");
            String dishToUpdate = sc1.nextLine().trim();

            // Check if the dish exists in the order status
            if (order.OrderStatus.containsKey(dishToUpdate)) {
                // Confirm the new status
                System.out.print("Enter the new status (preparing, out for delivery, denied, cancelled, delivered): ");
                String newStatus = sc1.nextLine().trim().toLowerCase();

                // Validate the new status input
                if (newStatus.equals("preparing") || newStatus.equals("out for delivery") ||
                        newStatus.equals("denied") || newStatus.equals("cancelled") ||
                        newStatus.equals("delivered")) {

                    // Update the order status
                    order.OrderStatus.put(dishToUpdate, newStatus);
                    System.out.println("Status of " + dishToUpdate + " has been updated to: " + newStatus);
                }
                else if(newStatus.equalsIgnoreCase("delivered"))
                {
                    Fooditem.add(dishToUpdate);
                }
                else {
                    System.out.println("Invalid status entered! Please enter a valid status.");
                }
            } else {
                System.out.println("The specified dish was not found in your order. Please enter a valid name.");
            }

            // Ask user if they want to update another item's status
            System.out.print("Do you want to update the status of another food item? (Yes/No): ");
            String wantToContinue = sc1.nextLine().trim();

            if (!wantToContinue.equalsIgnoreCase("yes")) {
                break; // Exit loop if the user doesn't want to update more items
            }
        }
        if(!Fooditem.isEmpty())
        {
            System.out.println("\nCustomer ID : " + order.CustomerId + " Your Order Is Ready To be Picked Up : )");
            for(String name : Fooditem)
            {
                System.out.println(name + " INR of "+ order.CartOrderList.get(name).getFirst().getPrice() +" Quantity "+ CartOrderList.get(name).size());
            }
        }
        System.out.println("==============================================");
        TransferToNonPending(byteMe);
    }

    public void TransferToNonPending(ByteMe byteMe)
    {
        LinkedList<Order> checkingPending = byteMe.CheckerCustomerList;
        List<Order> ToBeRemoved = new ArrayList<>();
        for(Order order : checkingPending)
        {
            TreeMap<String, String> NonPending = order.OrderStatus;
            Set<String> keys = NonPending.keySet();
            int temp = 0;
            for(String key : keys)
            {
                if(NonPending.get(key).equalsIgnoreCase("delivered"))
                {
                    temp++;
                }
            }
            if(temp == NonPending.size())
            {
                ToBeRemoved.add(order);
            }
        }
        if(!(ToBeRemoved.isEmpty()))
        {
            for(Order order : ToBeRemoved)
            {
                ByteMe.NonPendingOrders.add(order);
            }
        }
    }

    public void OrderHistory() {
        Scanner sc1 = new Scanner(System.in);
        System.out.println("======== Order History ===============");

        if (CartOrderList.isEmpty()) {
            System.out.println("Your cart is empty. No order status to display.");
            return;
        }
        boolean anyDeniedOrCanceled = false;

        for (String itemName : CartOrderList.keySet()) {
            String status = OrderStatus.get(itemName);
            List<FoodItems> itemList = CartOrderList.get(itemName);
            int quantity = itemList.size();
            FoodItems item = itemList.getFirst();
            int price = item.getPrice();

            // Check status and print details accordingly
            switch (status) {
                case "denied":
                    anyDeniedOrCanceled = true;
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s%n", itemName, price, quantity, status);
                    DeniedORCanceled.add(itemName);
                    break;
                case "cancelled":
                    anyDeniedOrCanceled = true;
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s%n", itemName, price, quantity, status);
                    DeniedORCanceled.add(itemName);
                    break;

                case "preparing":
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s%n",
                            itemName, price, quantity, status);
                    break;

                case "out for delivery":
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s%n", itemName, price, quantity, status);
                    break;

                case "delivered":
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s%n", itemName, price, quantity, status);
                    break;

                default:
                    System.out.printf("Item: %s | Price: %d INR | Quantity: %d | Status: %s (Unknown status)%n",
                            itemName, price, quantity, status);
                    break;
            }
        }
        System.out.print("Do you Want to re-order any Item stated Above : (Yes/No ) : ");
        String reorder = sc1.nextLine();
        if(reorder.equalsIgnoreCase("yes"))
        {
            ByteMe.customerList.get(CustomerId).CartOperation();
        }
    }


    public void Pending_Orders(ByteMe byte_me) {
        byte_me.Panel.OnWhichScreenIAm = 2;
        byte_me.Panel.I_started_with_panel = false;
        byte_me.Panel.showPendingOrderScreen();

        LinkedList<Order> checkerCustomerList =  byte_me.CheckerCustomerList;
        System.out.println("================ Pending Orders ==================");

        // Check if there are any orders to display
        if (checkerCustomerList.isEmpty()) {
            System.out.println("No pending orders found.");
            return;
        }

        System.out.printf("%-20s %-10s %-10s %-15s%n", "Item", "Price (INR)", "Quantity", "Status");
        System.out.println("---------------------------------------------------");

        boolean hasPendingOrders = false;

        for (Order order : checkerCustomerList) {
            TreeMap<String, List<FoodItems>> cart = order.CartOrderList;
            Set<String> keys = cart.keySet();

            for (String key : keys) {
                if(order.OrderStatus.isEmpty())
                {
//                    System.out.println("You need to add some item here !");
                    continue;
                }
                if (order.OrderStatus.get(key).equalsIgnoreCase("preparing")) {
                    hasPendingOrders = true; // Set flag if any order is pending
                    int price = cart.get(key).getFirst().getPrice();
                    int quantity = cart.get(key).size();
                    System.out.printf("%-20s %-10d %-10d %-15s%n", key, price, quantity, "Preparing");
                }
            }
        }

        if (!hasPendingOrders) {
            System.out.println("No pending orders in 'preparing' status.");
        }

        System.out.println("===================================================");
    }

    public void ProvideReview() {
        Scanner sc1 = new Scanner(System.in);
        System.out.println("============ Provide Review ============");

        // Check if the cart is empty
        if (CartOrderList.isEmpty()) {
            System.out.println("Your cart is empty. No dishes to review.");
            return;
        }

        Set<String> keys = CartOrderList.keySet();
        for (String key : keys) {
            System.out.println("Do you want to provide a review for this dish: " + key + "? (yes/no)");
            String checkReview = sc1.nextLine().trim();

            // Validate the input
            while (!checkReview.equalsIgnoreCase("yes") && !checkReview.equalsIgnoreCase("no")) {
                System.out.println("Invalid input! Please enter 'yes' or 'no'.");
                checkReview = sc1.nextLine().trim();
            }

            // If user wants to provide a review
            if (checkReview.equalsIgnoreCase("yes")) {
                System.out.print("Enter your review: ");
                String review = sc1.nextLine().trim();
                // Check if there are existing reviews for the item
                List<String> reviewsList = ItemReview.getOrDefault(key, new ArrayList<>());

                // Add the new review to the list
                reviewsList.add(review);
                ItemReview.put(key, reviewsList);

                System.out.println("Thank you for your review on " + key + ": " + review);
            } else {
                System.out.println("No review provided for " + key + ".");
            }
        }
        System.out.println("All the Reviews have been successfully Added !");
        System.out.println("Thank you for your feedback!");
    }

    public void ViewReviews() {
        Scanner sc1 = new Scanner(System.in);
        boolean wantToExit = true;

        while (wantToExit) {
            System.out.println("============ View Reviews ============");
            System.out.print("Enter the name of the item to view reviews (or type 'exit' to quit): ");
            String itemName = sc1.nextLine().trim();
            Set<String> keys = ItemReview.keySet();
            boolean found = false;
            for(String key : keys)
            {
                if(key.equalsIgnoreCase(itemName))
                {
                    itemName = key;
                    found = true;
                    break;
                }
            }
            if(!found)
            {
                System.out.println("Given Dish " + itemName + " is not found in Menu : Try again !");
                continue;
            }

            // Retrieve reviews for the specified item
            List<String> reviews = ItemReview.get(itemName);

            // Check if there are any reviews for the item
            if (reviews != null && !reviews.isEmpty()) {
                System.out.println("Reviews for " + itemName + ":");
                for (String review : reviews) {
                    System.out.println("- " + review);
                }
            } else {
                System.out.println("No reviews found for " + itemName + ".");
            }

            System.out.print("Do you want to See Review of another Dish : (Yes/No) : ");
            String check = sc1.nextLine();
            wantToExit = check.equalsIgnoreCase("yes");
            System.out.println(); // Add an empty line for better spacing
        }
    }

    public  void ProvideSpecialReview()
    {
        Scanner sc1 = new Scanner(System.in);
        System.out.println("============ Provide Special Review ============");

        // Check if the cart is empty
        if (CartOrderList.isEmpty()) {
            System.out.println("Your cart is empty. No dishes to review.");
            return;
        }

        Set<String> keys = CartOrderList.keySet();
        for (String key : keys) {
            System.out.println("Do you want to provide a review for this dish: " + key + "? (yes/no)");
            String checkReview = sc1.nextLine().trim();

            // Validate the input
            while (!checkReview.equalsIgnoreCase("yes") && !checkReview.equalsIgnoreCase("no")) {
                System.out.println("Invalid input! Please enter 'yes' or 'no'.");
                checkReview = sc1.nextLine().trim();
            }

            // If user wants to provide a review
            if (checkReview.equalsIgnoreCase("yes")) {
                System.out.print("Enter your review: ");
                String review = sc1.nextLine().trim();
                // Check if there are existing reviews for the item
                List<String> reviewsList = SpecialReviews.getOrDefault(key, new ArrayList<>());

                // Add the new review to the list
                reviewsList.add(review);
                SpecialReviews.put(key, reviewsList);

                System.out.println("Thank you for your review on " + key + ": " + review);
            } else {
                System.out.println("No review provided for " + key + ".");
            }
        }
        System.out.println("All the Reviews have been successfully Added !");
        System.out.println("Thank you for your feedback!");
    }


    public void ProcessRefunds() {
        Scanner sc1 = new Scanner(System.in);
        System.out.println("========== Provide Refunds ===========");
        boolean wantToContinue = true;

        // Initialize the RefundList outside the loop
        LinkedList<Order> refundList = ByteMe.RefundOrders;

        // Check if there are any orders to process refunds for
        if (refundList.isEmpty()) {
            System.out.println("No refund orders available to process.");
            return;
        }

        while (wantToContinue) {
            for (Order order : refundList) {
                TreeMap<String, String> deniedOrCancelled = order.OrderStatus;
                Set<String> keys = deniedOrCancelled.keySet();
                boolean hasRefundableItems = false;

                for (String key : keys) {
                    if (deniedOrCancelled.get(key).equalsIgnoreCase("denied") || deniedOrCancelled.get(key).equalsIgnoreCase("cancelled")) {
                        hasRefundableItems = true; // At least one refundable item exists
                        System.out.println("\nDo you want to provide a refund for the dish '" + key + "' to customer ID " + order.CustomerId + "?");
                        System.out.print("Enter (Yes/No): ");
                        String check = sc1.nextLine();

                        if (check.equalsIgnoreCase("yes")) {
                            // Assuming getFirst() returns the first item in the list
                            if(order.OrderStatus.isEmpty())
                            {
                                System.out.println("You need to add items here ");
                                continue;
                            }
                            double price = order.CartOrderList.get(key).getFirst().getPrice();
                            int quantity = order.CartOrderList.get(key).size();
                            double totalAmount = price * quantity;

                            System.out.println("Dish: " + key + " | Price: " + price + " | Quantity: " + quantity);
                            System.out.println("Your amount has been transferred to your account! Grand Total: " + totalAmount);
                        }
                    }
                }

                // If there were no refundable items for this order
                if (!hasRefundableItems) {
                    System.out.println("\nNo refundable items found for customer ID " + order.CustomerId + ".");
                }

                System.out.print("\nDo you want to provide refunds to another customer? (Yes/No): ");
                String doYouWantToContinue = sc1.nextLine().trim();
                wantToContinue = doYouWantToContinue.equalsIgnoreCase("yes");
            }
        }

        System.out.println("Thank you for processing refunds.");
    }

    public void SetCustomerID(String CustomerID)
    {
        this.CustomerId = CustomerID;
    }
    public void ReportGeneration(ByteMe byteMe) {
        System.out.println("========== Daily Sales Report ===========");

        // Initialize counters and storage for sales data
        double totalSales = 0.0;
        int totalOrders = 0;
        Map<String, Integer> itemPopularity = new HashMap<>(); // Store item names and their sale counts

        // Header for order details
        System.out.printf("%-20s %-10s %-10s %-15s%n", "Item", "Quantity", "Price (INR)", "Status");
        System.out.println("---------------------------------------------------");

        // Iterate through all orders in the CheckerCustomerList
        for (Order order : byteMe.CheckerCustomerList) {
            // Check each item in the order
            for (String itemName : order.OrderStatus.keySet()) {
                String status = order.OrderStatus.get(itemName);

                // Retrieve item details from the order's cart
                List<FoodItems> itemList = order.CartOrderList.get(itemName);
                int quantity = 0;
                FoodItems item;
                double price = 0;
                if(itemList != null)
                {
                    quantity = itemList.size();
                    item = itemList.getFirst(); // Assuming getFirst() returns the first item
                    price = item.getPrice();
                }

                // Print order details
                System.out.printf("%-20s %-10d %-10.2f %-15s%n", itemName, quantity, price, status);

                // Update total sales and order count for delivered items
                if (status.equalsIgnoreCase("delivered")) {
                    totalSales += price * quantity;
                    totalOrders++;

                    // Update item popularity
                    itemPopularity.put(itemName, itemPopularity.getOrDefault(itemName, 0) + quantity);
                }
            }
        }

        // Print total sales and order count
        System.out.printf("Total Sales: %.2f INR%n", totalSales);
        System.out.printf("Total Orders: %d%n", totalOrders);

        // Determine the most popular items
        System.out.println("Most Popular Items:");
        itemPopularity.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5) // Show top 5 most popular items
                .forEach(entry ->
                        System.out.printf("Item: %s | Sold: %d times%n", entry.getKey(), entry.getValue()));

        System.out.println("=========================================");
    }

}
