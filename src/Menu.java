import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Menu implements Serializable {
    @Serial
    private static final long serialVersionUID = -4471678785431906350L;
    protected TreeMap<String, FoodItems> MenuList = new TreeMap<>();
    protected TreeMap<Integer, List<FoodItems>> MenuListByPrice = new TreeMap<>();
    private transient final Scanner sc = new Scanner(System.in);
    protected TreeMap<String, List<FoodItems>> MenuCategory = new TreeMap<>();
    protected TreeMap<String, Customer> CustomerList = ByteMe.customerList;
    public Menu() {
        addHardcodedItems();
    }

    private void addHardcodedItems() {
        // Rice category
        addMenuItem("Rice", "Steam Rice", 65);
        addMenuItem("Rice", "Jeera Rice", 135);
        addMenuItem("Rice", "Ghee Rice", 135);
        addMenuItem("Rice", "Peas Pulao", 135);
        addMenuItem("Rice", "Corn Methi Pulao", 135);
        addMenuItem("Rice", "Curd Rice", 120);
        addMenuItem("Rice", "Lemon Chilly Rice", 125);
        addMenuItem("Rice", "Palak Rice", 135);

        // Rotis & Breads category
        addMenuItem("Rotis & Breads", "Tandoori Roti", 30);
        addMenuItem("Rotis & Breads", "Naan", 40);
        addMenuItem("Rotis & Breads", "Garlic Naan", 50);
        addMenuItem("Rotis & Breads", "Plain Kulcha", 50);
        addMenuItem("Rotis & Breads", "Stuffed Kulcha", 50);
        addMenuItem("Rotis & Breads", "Tandoori Bread Basket", 125);

        // Staples - Noodles / Fried Rice category
        addMenuItem("Staples", "Shanghai Egg", 150);
        addMenuItem("Staples", "Shanghai Veg", 145);
        addMenuItem("Staples", "Shanghai Chicken", 170);
        addMenuItem("Staples", "Shanghai Mix", 190);
        addMenuItem("Staples", "Chilly Garlic Egg", 150);
        addMenuItem("Staples", "Chilly Garlic Veg", 145);
        addMenuItem("Staples", "Chilly Garlic Chicken", 170);
        addMenuItem("Staples", "Chilly Garlic Mix", 190);
        addMenuItem("Staples", "Leefu Egg", 150);
        addMenuItem("Staples", "Leefu Veg", 145);
        addMenuItem("Staples", "Leefu Chicken", 170);
        addMenuItem("Staples", "Leefu Mix", 190);
        addMenuItem("Staples", "Burnt Ginger Garlic Egg", 150);
        addMenuItem("Staples", "Burnt Ginger Garlic Veg", 145);
        addMenuItem("Staples", "Burnt Ginger Garlic Chicken", 170);
        addMenuItem("Staples", "Burnt Ginger Garlic Mix", 190);

        // Desserts - Flavored Ice Cream category
        addMenuItem("Desserts", "Vanilla Ice Cream", 55);
        addMenuItem("Desserts", "Strawberry Ice Cream", 60);
        addMenuItem("Desserts", "Chocolate Ice Cream", 65);

        // Desserts - Ice Cream Sundaes category
        addMenuItem("Desserts", "Vanilla Sky Sundae", 115);
        addMenuItem("Desserts", "Choco Delight Sundae", 95);
        addMenuItem("Desserts", "Sinful Secret Sundae", 215);
        addMenuItem("Desserts", "House Special Sundae", 230);
    }

    private void addMenuItem(String category, String name, int price){
        FoodItems item = new FoodItems(name, price);
        item.setAvailability(true);  // Assuming all items are available
        item.setRating(4.0f);         // Setting a default rating

        // Add to MenuList
        MenuList.put(name, item);

        // Add to MenuListByPrice
        MenuListByPrice.computeIfAbsent(price, k -> new ArrayList<>()).add(item);

        // Add to MenuCategory
        MenuCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(item);
    }

    public void addItem() {
        boolean wantToAdd = true;
        System.out.println("\n=== Add Menu Item ===");

        while (wantToAdd) {
            System.out.print("\nEnter the category name of the dish to add: ");
            String category = sc.nextLine();

            System.out.print("Enter the name of the dish: ");
            String name = sc.nextLine();

            System.out.print("Enter the price of the dish: ");
            int price = sc.nextInt();
            sc.nextLine();  // Consume newline left-over after nextInt

            FoodItems item = new FoodItems(name, price);
            MenuList.putIfAbsent(name, item);

            // Add item to price-based listing (supports multiple items at the same price)
            MenuListByPrice.computeIfAbsent(price, k -> new ArrayList<>()).add(item);

            // Add item to category listing
            MenuCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(item);

            System.out.print("\nDo you want to add another item? (Yes/No): ");
            String additionDone = sc.nextLine();
            wantToAdd = additionDone.equalsIgnoreCase("yes");
        }
        System.out.println("\nAll additions completed.\n");
    }

    public void SearchByName()
    {
       boolean SearchingDone = true;
       while(SearchingDone)
       {
           System.out.println("Enter the Name of Dish : ");
           String name = sc.nextLine();
           Set<String> key3 = MenuList.keySet();
           for(String key4 : key3)
           {
               if(key4.equalsIgnoreCase(name))
               {
                   name = key4;
                   break;
               }
           }
           if(MenuList.containsKey(name))
           {
               showDetails(MenuList.get(name));
           }
           else{
               System.out.println("There does not exit such item : Try again !");
           }
           String WantToExit;
           System.out.print("Wanna search another yummy dish, should Try (Yes/No) : ");
           WantToExit = sc.nextLine();
           SearchingDone = WantToExit.equalsIgnoreCase("yes");
       }
    }

    public void showDetails(FoodItems foodItem) {
        // Using printf for formatted output in a single line

        System.out.printf("%-20s - %4d INR (Available: %-3s) | Rating: %.1f / 5.0%n",
                foodItem.getName(),
                foodItem.getPrice(),
                (foodItem.isAvailable() ? "Yes" : "No"),
                foodItem.getRating());
    }


    public void showMenu(ByteMe obj_of_byte_me) {
        System.out.println("\n=== MENU ===");

        obj_of_byte_me.Panel.OnWhichScreenIAm = 1;
        obj_of_byte_me.Panel.I_started_with_panel = false;
        obj_of_byte_me.Panel.showMenuScreen();

        int index = 1; // Indexing for each item

        // Iterate over each category in MenuCategory
        for (Map.Entry<String, List<FoodItems>> category : MenuCategory.entrySet()) {
            String categoryName = category.getKey();
            List<FoodItems> items = category.getValue();

            // Print category name as a heading
            System.out.println("\n--- " + categoryName.toUpperCase() + " ---");

            // Display each item with index, price, and availability
            for (FoodItems item : items) {
                System.out.printf("%d. %-25s - %4d INR (Available: %-3s) | Rating: %.1f / 5.0%n",
                        index, item.getName(), item.getPrice(),
                        item.isAvailable() ? "Yes" : "No",
                        item.getRating());
                index++;
            }
        }
        System.out.println("\n=== END OF MENU ===");
    }


    public List<FoodItems> sortByCategory()
    {
        System.out.print("\nEnter the Category for FoodItems : ");
        String category = sc.nextLine();
        System.out.println("\n=================CATEGORY(" + category + ")=================");
        Set<String> keys  = MenuCategory.keySet();
        for(String key : keys)
        {
            if(key.equalsIgnoreCase(category))
            {
                category = key;
                break;
            }
        }
        return MenuCategory.get(category);
    }
    public List<FoodItems> sortByPriceAfterCategory(List<FoodItems> foodItems, int price)
    {
        return foodItems.stream()
                .filter(item -> item.getPrice() < price)  // Filter items with price less than maxPrice
                .sorted(Comparator.comparingInt(FoodItems::getPrice).reversed())  // Sort by price in descending order
                .collect(Collectors.toList());  // Collect into a list

    }

    public void updateItem() {
        boolean wantToChange = true;
        System.out.println("\n=== Update Menu Item ===");

        while (wantToChange) {
            System.out.print("Enter the name of the dish to update: ");
            String name = sc.nextLine();

            Set<String> key3 = MenuList.keySet();
            for(String key4 : key3)
            {
                if(key4.equalsIgnoreCase(name))
                {
                    name = key4;
                    break;
                }
            }
            if (MenuList.containsKey(name)) {
                FoodItems item = MenuList.get(name);
                item.updateDetails();  // Calling method within FoodItems class
            } else {
                System.out.println("Dish not found! Please try again.");
            }

            System.out.print("\nDo you want to update another item? (Yes/No): ");
            String ans = sc.nextLine();
            wantToChange = (ans.equalsIgnoreCase("yes"));
        }
        System.out.println("All updates completed.\n");
    }

    public TreeMap<String, FoodItems> getMenu() {
        return MenuList;
    }

    public TreeMap<Integer, List<FoodItems>> sortByPrice(int price) {
        // Get a view of the MenuListByPrice with keys less than the specified price
        NavigableMap<Integer, List<FoodItems>> filteredMenu = MenuListByPrice.headMap(price, false);

        // Return the resulting map
        return new TreeMap<>(filteredMenu);
    }

    public void removeItem() {
        boolean wantToChange = true;
        System.out.println("\n=== Remove Menu Item ===");

        while (wantToChange) {
            System.out.print("Enter the name of the dish to remove: ");
            String name = sc.nextLine();

            Set<String> key3 = MenuList.keySet();
            for(String key4 : key3)
            {
                if(key4.equalsIgnoreCase(name))
                {
                    name = key4;
                    break;
                }
            }
            if (MenuList.containsKey(name)) {
                // Remove from MenuList
                FoodItems itemToRemove = MenuList.remove(name);
                Set<String> keys = CustomerList.keySet();
                for(String key : keys)
                {
                    Customer customer = CustomerList.get(key);
                    TreeMap<String , String> orderStatus = customer.order.OrderStatus;
                    if(!(ByteMe.RefundOrders.contains(customer.order)))
                    {
                        ByteMe.RefundOrders.offer(customer.order);
                    }
                    orderStatus.replace(name,"denied");
                }
                // Remove from MenuListByPrice
                for (List<FoodItems> items : MenuListByPrice.values()) {
                    String finalName = name;
                    items.removeIf(item -> item.getName().equalsIgnoreCase(finalName));
                }
                // Remove from MenuCategory
                String finalName1 = name;
                MenuCategory.forEach((category, items) -> {
                    items.removeIf(item -> item.getName().equalsIgnoreCase(finalName1));
                });

                System.out.println(name + " has been removed from the menu.");
            } else {
                printDishNotFoundMessage();
            }

            System.out.print("\nDo you want to remove another item? (Yes/No): ");
            String ans = sc.nextLine();
            wantToChange = (ans.equalsIgnoreCase("yes"));
        }
        System.out.println("All removals completed.\n");
//        PendingOrder.updateCheckerCustomerList(ByteMe.CheckerCustomerList);
    }




    private void printDishNotFoundMessage() {
        System.out.println("Dish not found! Please try again.");
    }
}
