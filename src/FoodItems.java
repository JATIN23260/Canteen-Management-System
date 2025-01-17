import java.io.Serializable;
import java.util.Scanner;

public class FoodItems implements Serializable{
    protected String name;
    protected int price;
    protected boolean availability = true;
    protected float rating = 4.5f;
    protected boolean checkOut = false;
    public FoodItems(String name, int price) {
        this.name = name;
        this.price = price;
    }


    protected void updateDetails() {
        Scanner sc = new Scanner(System.in);
        boolean wantToExit = true;
        System.out.println("\n=== Update Food Item Details ===");

        while (wantToExit) {
            System.out.println("\nChoose an attribute to update:");
            System.out.println("1. Price");
            System.out.println("2. Availability");
            System.out.println("3. Name");
            System.out.print("Enter your choice (1/2/3): ");
            int number = sc.nextInt();
            sc.nextLine();  // Consume newline

            switch (number) {
                case 1 -> {
                    System.out.print("Enter updated price: ");
                    int newPrice = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    updatePrice(newPrice);
                    System.out.println("Price updated successfully.");
                }
                case 2 -> {
                    System.out.print("Is the dish available? (Yes/No): ");
                    String available = sc.nextLine();
                    updateAvailability(available.equalsIgnoreCase("yes"));
                    System.out.println("Availability updated successfully.");
                }
                case 3 -> {
                    System.out.print("Enter new name: ");
                    String newName = sc.nextLine();
                    updateName(newName);
                    System.out.println("Name updated successfully.");
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }

            System.out.print("\nAre you done with changes? (Yes/No): ");
            String ans = sc.nextLine();
            wantToExit = !ans.equalsIgnoreCase("yes");
        }
        System.out.println("Update process completed.\n");
    }

    protected void updatePrice(int price) {
        this.price = price;
    }

    protected void updateAvailability(boolean availability) {
        this.availability = availability;
    }

    protected void updateName(String name) {
        this.name = name;
    }

    protected int getPrice()
    {
        return price;
    }
    protected String getName()
    {
        return name;
    }
    protected boolean isAvailable()
    {
        return availability;
    }
    protected float getRating()
    {
        return this.rating;
    }

    public void setAvailability(boolean b) {
        this.availability = b;
    }

    public void setRating(float v) {
        this.rating = v;
    }
}

