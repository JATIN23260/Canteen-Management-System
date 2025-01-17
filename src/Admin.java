import java.util.LinkedList;
import java.util.Scanner;

public class Admin {
    protected int AdminId;
    protected String AdminName;
    protected Menu menu;
    private transient final Scanner sc = new Scanner(System.in);
    private LinkedList<Order> checkCustomerList;
    public ByteMe byte_me_app;
    public void AdminWelcome(ByteMe byteMe) {
        this.byte_me_app = byteMe;
        this.menu = byte_me_app.menu;
        checkCustomerList = byteMe.CheckerCustomerList;
        System.out.println("=================================");
        System.out.println("      Welcome, Admin of Byte Me! ");
        System.out.println("=================================");
        System.out.print("Please enter your name: ");
        this.AdminName = sc.nextLine();
        System.out.println("Your Admin ID (please remember it): " + this.AdminId);

        boolean continueSession = true;
        while (continueSession) {
            System.out.println("\n=========== Your Tasks ===========");
            System.out.println("1. Menu Management");
            System.out.println("2. Order Management");
            System.out.println("3. Report Generation");
            System.out.println("4. Exit Admin Panel");
            System.out.print("Select a task by entering its number: ");

            int taskNumber;
            try {
                taskNumber = sc.nextInt();
                sc.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("\nInvalid input! Please enter a valid number.");
                sc.nextLine(); // clear invalid input
                continue;
            }

            switch (taskNumber) {
                case 1 -> MenuManagement();
                case 2 -> OrderManagement();
                case 3 -> ReportGeneration();
                case 4 -> {
                    System.out.println("\nExiting Admin Panel. Goodbye, " + AdminName + "!");
                    continueSession = false;
                }
                default -> System.out.println("\nInvalid task number! Please try again.");
            }
        }
    }
    public Order orders = new Order(byte_me_app);
    public void setAdminId(int id) {
        this.AdminId = id;
    }

    public int getAdminId() {
        return this.AdminId;
    }

    public void MenuManagement() {
        System.out.println("\n--- Menu Management ---");
        System.out.println("1. Add new item");
        System.out.println("2. Update existing item");
        System.out.println("3. Remove item");
        System.out.println("4. Go Back To Main menu");
        boolean WantToGOBack = true;
        while(WantToGOBack)
        {
            System.out.print("Select an action by entering its number: ");
            int action = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (action) {
                case 1 -> menu.addItem();
                case 2 -> menu.updateItem();
                case 3 -> menu.removeItem();
                case 4 -> {
                    WantToGOBack = false;
                }
            }
        }

    }

    public void OrderManagement() {
        System.out.println("\n--- Order Management ---");
        System.out.println("1. View Pending Orders");
        System.out.println("2. Update Order Status");
        System.out.println("3. Process Refund");
        System.out.println("4. Handle Special Requests");
        System.out.println("5. GO Back to Main Menu");

        boolean WantToGOBack = true;
        while(WantToGOBack) {
            System.out.print("Select an action by entering its number: ");
            int action = sc.nextInt();
            sc.nextLine(); // consume newline




                switch (action) {
                    case 1 -> orders.Pending_Orders(byte_me_app);
                    case 2 -> orders.UpdateOrderStatus(byte_me_app);
                    case 3 -> orders.ProcessRefunds();
                    case 4 -> orders.ProvideSpecialReview();
                    case 5 -> {
                        WantToGOBack = false;
                    }

            }

        }


        // Future order management functionalities would go here.
    }

    public void ReportGeneration() {
        System.out.println("\n--- Report Generation ---");

        orders.ReportGeneration(byte_me_app);

        // Future report generation functionalities would go here.
    }
}
