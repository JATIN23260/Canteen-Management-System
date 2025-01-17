Basic Assumption : Since we are Seprating the the sort by category and sort by price,
since I implement the another TreeMap in MenuCategory which seprates each Fooditems at the time of addition

2 -> for simplicity we assumed that you can only sort by Category but with category you are not allowed to
sort by price, if you do you will get all items below that price, whether they belong to that category or not

Customer Id is the hashcode of Customer Object, which makes seprate Id for every customer object

these are the assumption : I took care and further more I added in code while implementing every method, with the
help of comments

Code Flow and working :
1.--> ByteMe
Acts as the main entry point of the program.
Manages global data structures like menu, customerList, and lists of orders (PendingVipCustomer, RefundOrders, NonPendingOrders).
Provides methods to identify and authenticate customers and administrators.
Calls appropriate methods for order processing, menu management, and report generation.


2. -->  Order
Represents individual orders placed by customers.
Stores order details like CustomerId, DeliveryAddress, FinalAmount, and OrderStatus.
Includes methods for:
Viewing order history, cart, and total amount (viewCart(), viewTotal()).
Updating and checking out payments, canceling orders, and generating reports (PaymentCheckOut(), CancelOrder(), ReportGeneration()).
Handling order status updates and managing quantities.


3.--> Customer
Represents a customer in the system.
Stores customer information, including CustomerId, name, and isVip status.
Manages interactions with menus and orders, allowing customers to browse menus, place orders, and track order status.
Contains methods for:
Cart operations and order tracking (CartOperation(), OrderTracking()).
Sorting menu items, viewing subscription status, and providing reviews.


4.--> FoodItems
Represents individual food items available on the menu.
Stores information about each item, such as price, name, availability, and rating.
Provides methods to retrieve and update item details, including setting availability and updating ratings.


5.--> Menu
Manages the list of food items (MenuList), sorted categories, and customer-specific menus.
Contains methods for:
Adding and removing items, sorting items by price or category, and browsing the menu (addItem(), removeItem(), sortByPriceAfterCategory()).
Printing out menu options, searching items by name, and managing customer interactions with the menu.




6. --> Admin
Represents an administrator who manages menus and orders.
Stores admin details, including AdminId and AdminName.
Provides administrative functions such as:
Menu and order management (MenuManagement(), OrderManagement()).
Generating reports (ReportGeneration()).
Program Flow
Initialization: The ByteMe class initializes the main components and data structures, including menu, customerList, and order lists.

Customer Interaction:

A customer uses methods in the Customer class to browse the menu, view items, and add items to the cart.
The customer places an order, which creates an Order instance linked to their account.
Customers can view order status, update quantities, and check out payments through Order methods.
Order Processing:

Each order contains details such as item names, quantities, and statuses.
Order methods handle order history, payment, and refunds.
The OrderStatus attribute tracks the status of each order, and ReportGeneration() can generate daily summaries.
Admin Management:

An Admin can add or remove items from the Menu, sort items, and view or generate reports.
The OrderManagement() method in Admin provides control over pending and completed orders.
Report Generation:

At the end of each day, ReportGeneration() in Admin or Order can compile reports on all orders, including details on each item, quantity, price, and status.