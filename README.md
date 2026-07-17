# 🍽️ Byte Me! — College Canteen Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-22-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.2-blue?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-5.10-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)
![Tests](https://img.shields.io/badge/Tests-24%20Passed-brightgreen?style=for-the-badge)

**A full-featured, dual-interface food ordering and canteen management platform**  
**built for college environments using Java 17+, JavaFX 21, and JUnit 5.**

[Features](#-features) · [Architecture](#-architecture) · [Getting Started](#-getting-started) · [Usage Guide](#-usage-guide) · [Testing](#-testing) · [File Structure](#-file-structure)

</div>

---

## 📋 Table of Contents

1. [Project Overview](#-project-overview)
2. [Target Users](#-target-users)
3. [Use Cases](#-use-cases)
4. [Features](#-features)
5. [Architecture](#-architecture)
6. [Java Collections Used](#-java-collections-used)
7. [I/O Stream Management](#-io-stream-management)
8. [Prerequisites](#-prerequisites)
9. [Getting Started](#-getting-started)
10. [Usage Guide](#-usage-guide)
    - [Admin Interface](#admin-interface)
    - [Customer Interface](#customer-interface)
    - [GUI Interface](#gui-interface)
11. [Order Status Flow](#-order-status-flow)
12. [Testing](#-testing)
13. [File Structure](#-file-structure)
14. [Data Persistence](#-data-persistence)
15. [Design Decisions](#-design-decisions)
16. [Known Limitations](#-known-limitations)

---

## 📖 Project Overview

**Byte Me!** is a command-line-driven food ordering system backed by a read-only JavaFX graphical user interface, designed specifically for college canteens. The system enables students to browse the canteen menu, manage a shopping cart, place and track orders, and leave reviews — all from the comfort of their hostel rooms. Canteen staff (administrators) can manage menu items, monitor and process orders, generate daily sales reports, and handle refunds through a fully interactive CLI.

The project fulfils a two-phase execution model mandated by the assignment:

| Phase | Interface | Interaction |
|-------|-----------|-------------|
| **Phase 1 — CLI** | Terminal / Console | Full read-write access; all business logic |
| **Phase 2 — GUI** | JavaFX Window | Read-only display of menu and pending orders |

Data is exchanged between the two phases via plain-text files written by the CLI and read by the JavaFX GUI, ensuring a clean separation of concerns.

---

## 🎯 Target Users

| Role | Description |
|------|-------------|
| **Student / Customer** | Browses the menu, manages a cart, places orders, tracks delivery, leaves reviews, and can optionally upgrade to VIP status for order priority |
| **Canteen Admin** | Manages the full food menu, processes and updates order statuses, issues refunds, handles special requests, and generates daily sales reports |
| **VIP Customer** | A premium tier of student who pays a one-time fee for prioritised order processing over regular customers |

---

## 💡 Use Cases

### Customer Use Cases

```
UC-01  Register a new account
UC-02  Log in with existing credentials
UC-03  Browse the complete canteen menu
UC-04  Search menu items by name or keyword
UC-05  Filter menu items by category (Breakfast, Meals, Snacks, Beverages, Desserts)
UC-06  Sort menu items by price (ascending or descending)
UC-07  Add items to shopping cart
UC-08  Modify item quantity in cart
UC-09  Remove items from cart
UC-10  View cart total before checkout
UC-11  Checkout with optional special request (e.g., "extra spicy", "no onions")
UC-12  Track order status in real time
UC-13  Cancel a pending order (RECEIVED status only)
UC-14  View complete order history
UC-15  Re-order a previous meal
UC-16  Submit a review for an ordered item (1–5 stars + comment)
UC-17  View reviews from other customers for any item
UC-18  Upgrade to VIP membership for priority processing
```

### Admin Use Cases

```
UC-A01  Log in with admin credentials
UC-A02  View the complete canteen menu
UC-A03  Add a new food item (name, category, price, availability)
UC-A04  Update an existing food item (price, category, availability)
UC-A05  Remove a food item (auto-denies all pending orders containing it)
UC-A06  View all pending orders sorted by VIP priority, then FIFO
UC-A07  Update order status (PREPARING → OUT_FOR_DELIVERY → DELIVERED / DENIED)
UC-A08  Process a refund for a canceled or problematic order
UC-A09  View and acknowledge special customer requests
UC-A10  Generate a daily sales report (revenue, order counts, top items)
```

---

## ✨ Features

### 🖥️ CLI — Admin Interface

| Feature | Description |
|---------|-------------|
| **Menu Management** | Add, update (price/category/availability), and remove food items via guided prompts |
| **Auto-Denial on Remove** | Removing a menu item automatically sets all pending orders containing that item to `DENIED` |
| **Pending Order Queue** | View all pending orders sorted by VIP priority first, then FIFO (first-in, first-out) |
| **Status Updates** | Advance orders through: `RECEIVED → PREPARING → OUT_FOR_DELIVERY → DELIVERED` or set to `DENIED` |
| **Special Requests** | Customer notes (e.g., "no onions") are displayed alongside each order |
| **Refund Processing** | Process monetary refunds for canceled or problematic orders |
| **Daily Sales Report** | Summary of total orders, revenue (delivered only), order counts by status, and top-5 most ordered items |

### 👤 CLI — Customer Interface

| Feature | Description |
|---------|-------------|
| **Menu Browsing** | View all items grouped by category, with name, price, and availability |
| **Keyword Search** | Search by item name or category keyword (case-insensitive) |
| **Category Filter** | Filter items by category (Breakfast, Meals, Snacks, Beverages, Desserts) |
| **Price Sorting** | Sort the full menu ascending or descending by price |
| **Cart Management** | Add, modify quantity, and remove items with live running total |
| **Checkout** | Confirm order, deduct balance, attach a special request note |
| **Order Tracking** | View real-time status of all your orders |
| **Order Cancellation** | Cancel an order only while it is still in `RECEIVED` status |
| **Order History** | Complete log of all past orders with status and totals |
| **Re-order** | Instantly re-add all items from a previous order to your cart |
| **Item Reviews** | Submit a star rating + comment for any item you have ordered |
| **View Reviews** | Read community reviews and average ratings for any menu item |
| **VIP Upgrade** | Pay Rs.100 once to unlock priority processing on all future orders |

### 🖥️ GUI — JavaFX Read-Only Display

| Feature | Description |
|---------|-------------|
| **Menu Tab** | Interactive table with live search, category dropdown, and availability toggle |
| **Orders Tab** | Pending orders table with summary cards (total, VIP count, revenue) |
| **Filtering** | Filter orders by status, VIP type, or free-text search |
| **Order Detail Pane** | Click any order row to see full details in the detail panel below |
| **Dark Theme** | Professional dark UI with red accent colours, styled in `styles.css` |
| **Read-Only Enforcement** | The GUI makes no writes; all mutations go through the CLI only |

---

## 🏗️ Architecture

The system is organised into five distinct layers following standard layered architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                       │
│  ┌─────────────────────────┐  ┌─────────────────────────┐   │
│  │   CLI (Phase 1)          │  │   JavaFX GUI (Phase 2)   │   │
│  │  CLIMain → AdminCLI      │  │  GUIMain → MenuTab       │   │
│  │         → CustomerCLI   │  │         → OrdersTab      │   │
│  └─────────────┬───────────┘  └──────────┬──────────────┘   │
└────────────────│────────────────────────────│────────────────┘
                 │ calls                      │ reads files
┌────────────────▼────────────────────────────▼────────────────┐
│                      SERVICE LAYER                            │
│  MenuService · OrderService · CartService                     │
│  UserService · ReportService                                  │
└─────────────────────────┬───────────────────────────────────┘
                           │ uses
┌─────────────────────────▼───────────────────────────────────┐
│                       MODEL LAYER                             │
│  FoodItem · Review · OrderItem · OrderStatus · Order         │
│  User (abstract) · Customer · Admin · Cart                   │
└─────────────────────────┬───────────────────────────────────┘
                           │ persisted by
┌─────────────────────────▼───────────────────────────────────┐
│                    I/O / PERSISTENCE LAYER                    │
│  FileManager · DataPaths                                      │
│  data/users.dat · data/menu.txt · data/pending_orders.txt    │
│  data/orders_CUST-XXXX.txt · data/cart_CUST-XXXX.tmp        │
└─────────────────────────────────────────────────────────────┘
```

### Execution Flow

```
java Main
   │
   ├─► Phase 1: CLIMain.run()  ← blocking (reads stdin)
   │         │
   │         ├─ Admin login  → AdminCLI.run()
   │         ├─ Customer login → CustomerCLI.run()
   │         ├─ Register
   │         └─ Exit (option 4)
   │               │
   │               └─ Exports data/menu.txt
   │                          data/pending_orders.txt
   │
   └─► Phase 2: GUIMain.launchGUI()  ← JavaFX window
               │
               ├─ MenuTab  reads data/menu.txt
               └─ OrdersTab reads data/pending_orders.txt
```

---

## 📦 Java Collections Used

| Collection | Location | Purpose |
|-----------|----------|---------|
| `TreeMap<String, FoodItem>` | `MenuService` | Menu items sorted alphabetically by name (case-insensitive); provides O(log n) lookup, insert, and delete |
| `TreeMap<String, TreeSet<String>>` | `MenuService` | Category-to-item-name index; both maps and sets are sorted, enabling efficient category filtering |
| `PriorityQueue<Order>` | `OrderService` | Pending order queue with VIP-first natural ordering via `Order.compareTo()`; FIFO within the same tier using timestamp |
| `LinkedHashMap<String, OrderItem>` | `Cart` | Preserves item insertion order so the cart displays items in the sequence the customer added them |
| `LinkedHashMap<String, User>` | `UserService` | Registered users stored in registration order for consistent iteration |
| `HashMap<String, User>` | `UserService` | Secondary username-to-user index for O(1) login lookup |
| `HashMap<String, List<Order>>` | `OrderService` | Per-user order history (userId → list of orders) for fast retrieval |
| `List<Order>` (ArrayList) | `OrderService`, `Customer` | Complete order log and per-customer history |
| `List<Review>` (ArrayList) | `FoodItem` | Per-item review list in chronological order |

### Priority Queue Ordering (VIP-First FIFO)

The `Order` class implements `Comparable<Order>` as follows:

```java
@Override
public int compareTo(Order other) {
    // Rule 1: VIP orders always rank above regular orders
    if (this.vip != other.vip) return this.vip ? -1 : 1;
    // Rule 2: Among equal VIP status, earlier timestamp ranks first (FIFO)
    return this.timestamp.compareTo(other.timestamp);
}
```

This means a `PriorityQueue<Order>` automatically serves VIP customers before regular ones, and within each group processes orders in the sequence they were placed.

---

## 📂 I/O Stream Management

Two of three available I/O features are implemented using Java's `BufferedReader` / `BufferedWriter` for efficient line-by-line file operations.

### Feature 1 — User Management (`data/users.dat`)

**Format (pipe-delimited, one record per line):**
```
CUSTOMER|CUST-0001|alice|pass123|Alice Sharma|false|450.00
CUSTOMER|CUST-0002|bob|secure99|Bob Kumar|true|900.00
```

| Event | File Operation |
|-------|---------------|
| New user registers | Record appended; entire file rewritten |
| User logs in | Full file read; records loaded into `LinkedHashMap` |
| VIP upgrade / balance change | File rewritten with updated record |
| Admin login | No file I/O; credentials are hard-coded in `Admin` singleton |

### Feature 2 — Order History Per User (`data/orders_CUST-XXXX.txt`)

One human-readable text file per customer is maintained. It is **overwritten** every time the customer places, cancels, or completes an order, and also on logout.

**Sample file (`data/orders_CUST-0001.txt`):**
```
══════════════════════════════════════════════════════════════
 ORDER HISTORY  –  Alice Sharma  [CUST-0001]
══════════════════════════════════════════════════════════════
┌─ ORD-1000  [Delivered]
│  Customer : Alice Sharma
│  Time     : 17-07-2026 10:45
│  Items:
│    • Masala Dosa            x2    Rs.120.00
│    • Cold Coffee            x1    Rs.50.00
└─ Total: Rs.170.00
──────────────────────────────────────────────────────────────
```

### Temporary Cart Storage (`data/cart_CUST-XXXX.tmp`)

The cart file is updated in real time on every cart mutation (add, update, remove) and deleted on successful checkout.

### GUI Data Export (`data/menu.txt`, `data/pending_orders.txt`)

Written by the CLI just before it exits (option 4); immediately read by the JavaFX GUI on startup.

---

## 🔧 Prerequisites

| Requirement | Minimum Version | Notes |
|-------------|----------------|-------|
| **JDK** | Java 17 | Java 22 is installed and supported |
| **Maven** | 3.8+ | IntelliJ IDEA's bundled Maven is used by `run.bat` |
| **IntelliJ IDEA** | 2024.2 (Community or Ultimate) | Bundled Maven required for `run.bat` |
| **Internet** | Required on first run | Maven downloads dependencies from Maven Central |
| **OS** | Windows 10 / 11 | `run.bat` is Windows-specific; Maven commands work cross-platform |

### Dependencies (auto-downloaded by Maven)

| Dependency | Version | Scope |
|-----------|---------|-------|
| `org.openjfx:javafx-controls` | 21.0.2 | compile |
| `org.openjfx:javafx-fxml` | 21.0.2 | compile |
| `org.junit.jupiter:junit-jupiter-api` | 5.10.1 | test |
| `org.junit.jupiter:junit-jupiter-engine` | 5.10.1 | test |
| `org.junit.jupiter:junit-jupiter-params` | 5.10.1 | test |

---

## 🚀 Getting Started

### Step 1 — Clone or extract the project

Place the project in any directory. This guide assumes it is located at:
```
C:\Users\ADMIN\Downloads\Canteen_Management_Platform\
```

### Step 2 — Verify Java installation

Open a terminal and run:
```powershell
java -version
```
Expected output (version 17 or higher):
```
java version "22.0.1" 2024-04-16
```

### Step 3 — Run the application

#### Option A — Using the convenience batch script (Recommended)
```bat
# Run the full application (CLI → GUI)
run.bat

# Run JUnit tests only
run.bat test

# Compile only
run.bat compile
```

#### Option B — Using Maven directly
```powershell
# With IntelliJ's bundled Maven
$mvn = "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2024.2.1\plugins\maven\lib\maven3\bin\mvn.cmd"

& $mvn javafx:run        # Run full application
& $mvn test              # Run tests
& $mvn compile           # Compile only
```

#### Option C — Using IntelliJ IDEA IDE
1. Open IntelliJ IDEA
2. Select **File → Open** and navigate to `Canteen_Management_Platform/`
3. IntelliJ auto-detects the Maven project
4. Run `Main.java` using the green ▶ button, or use the Maven tool window

### Step 4 — First-time data directory

The `data/` directory is created automatically on the first run. No manual setup is required.

---

## 📘 Usage Guide

### Admin Interface

**Login credentials:**
```
Username : admin
Password : admin123
```

Once logged in, the Admin Dashboard presents the following options:

```
╔════════════════════════════════════════════════╗
║            🛠️   ADMIN DASHBOARD                ║
╠════════════════════════════════════════════════╣
║  ── MENU MANAGEMENT ──────────────────────     ║
║   1.  View Full Menu                           ║
║   2.  Add New Item                             ║
║   3.  Update Existing Item                     ║
║   4.  Remove Item                              ║
║  ── ORDER MANAGEMENT ─────────────────────     ║
║   5.  View Pending Orders                      ║
║   6.  Update Order Status                      ║
║   7.  Process Refund                           ║
║  ── REPORTS ───────────────────────────────    ║
║   8.  Generate Daily Sales Report              ║
║  ── SESSION ───────────────────────────────    ║
║   9.  Logout                                   ║
╚════════════════════════════════════════════════╝
```

**Key admin workflows:**

| Task | Steps |
|------|-------|
| Add menu item | `2` → enter name, category, price, availability |
| Mark item unavailable | `3` → enter name → press Enter for price/category → `y` to toggle |
| Remove item | `4` → enter name → confirm with `y` (pending orders auto-denied) |
| Process order | `6` → choose order ID → select new status |
| Generate report | `8` → report prints immediately |

**Sample daily sales report output:**
```
══════════════════════════════════════════════════════════
      📊  DAILY SALES REPORT  –  BYTE ME! CANTEEN
      Date: 17 July 2026
══════════════════════════════════════════════════════════
  Total Orders Today              : 12
    ✅ Delivered                   : 8
    ⏳ Pending/Active              : 2
    ❌ Canceled                    : 1
    🚫 Denied                      : 1
──────────────────────────────────────────────────────────
  💰 Total Revenue                : Rs. 1,240.00
──────────────────────────────────────────────────────────
  🏆 TOP ITEMS ORDERED TODAY:
   1.  Masala Dosa                          x14
   2.  Cold Coffee                          x11
   3.  Veg Biryani                          x9
   4.  Samosa                               x8
   5.  Tea                                  x7
══════════════════════════════════════════════════════════
```

---

### Customer Interface

**Registration:**
```
Select 3 from the main menu → enter name, username, password, initial balance
```

**Login:**
```
Select 2 from the main menu → enter username and password
```

The Customer Dashboard:
```
╔══════════════════════════════════════════════════╗
║  👤 Alice Sharma                        ⭐ VIP   ║
║  💰 Balance: Rs.850.00                           ║
╠══════════════════════════════════════════════════╣
║   1.  View Full Menu                             ║
║   2.  Search Items by Keyword                    ║
║   3.  Filter by Category                         ║
║   4.  Sort by Price                              ║
║   5.  Add Item to Cart                           ║
║   6.  View / Modify Cart                         ║
║   7.  Checkout                                   ║
║   8.  View My Orders & History                   ║
║   9.  Cancel an Order                            ║
║  10.  Re-order a Previous Meal                   ║
║  11.  Leave a Review                             ║
║  12.  View Item Reviews                          ║
║  13.  Upgrade to VIP  (Rs.100)                   ║
║  14.  Logout                                     ║
╚══════════════════════════════════════════════════╝
```

**Typical order workflow:**

```
1. View menu (option 1) or search (option 2)
2. Add items to cart (option 5) — repeat for each item
3. View cart and verify total (option 6)
4. Checkout (option 7) — optionally add a special request
5. Track your order status (option 8)
6. Cancel if needed before PREPARING (option 9)
```

**Special request example:**
```
Special request (Enter to skip): extra spicy, no onions
```

**VIP upgrade:**
- Cost: **Rs. 100** (one-time, deducted from balance)
- Benefit: All your subsequent orders are placed at the **front** of the processing queue, ahead of all regular orders

---

### GUI Interface

The JavaFX GUI launches automatically after you exit the CLI (option 4 on the main menu).

#### Menu Tab
- **Live search bar** — type any keyword to filter items instantly
- **Category dropdown** — select a specific category or "All Categories"
- **Availability toggle** — show all items or available-only items
- **Row colour coding** — green tint for available items, red tint for unavailable

#### Orders Tab
- **Summary cards** — total orders, VIP orders, regular orders, combined order value
- **Status filter** — filter by RECEIVED, PREPARING, OUT_FOR_DELIVERY, etc.
- **VIP filter** — show all, VIP only, or regular only
- **Detail pane** — click any row to expand full order details below the table

> **Important:** The GUI is strictly read-only. It reflects the state of the canteen at the moment you exited the CLI. To refresh the data, exit the GUI and run the CLI again.

---

## 🔄 Order Status Flow

```
                    ┌─────────────┐
                    │  RECEIVED   │ ← Order placed by customer
                    └──────┬──────┘
                           │
              ┌────────────┼─────────────────┐
              │            │                 │
              ▼            ▼                 ▼
        ┌──────────┐  ┌─────────┐      ┌──────────┐
        │PREPARING │  │ DENIED  │      │ CANCELED │
        └────┬─────┘  └─────────┘      └──────────┘
             │          ▲                    ▲
             │          │ (on menu           │ (customer action,
             │          │  item removal)     │  RECEIVED only)
             ▼          │
      ┌─────────────────┐
      │ OUT_FOR_DELIVERY│
      └────────┬────────┘
               │
               ▼
          ┌──────────┐
          │ DELIVERED│
          └──────────┘
```

| Transition | Who | Condition |
|-----------|-----|-----------|
| `RECEIVED → PREPARING` | Admin | Manual update via option 6 |
| `RECEIVED → CANCELED` | Customer | Only while status is RECEIVED |
| `Any pending → DENIED` | System (auto) | When admin removes a menu item |
| `PREPARING → OUT_FOR_DELIVERY` | Admin | Manual update via option 6 |
| `OUT_FOR_DELIVERY → DELIVERED` | Admin | Manual update via option 6 |
| `Any pending → DENIED` | Admin | Manual update via option 6 |

---

## 🧪 Testing

All tests are written using **JUnit 5 (Jupiter)** and test the model layer directly (no file I/O, no singletons), making them fast, isolated, and deterministic.

```
mvn test
```

**Results:**
```
[INFO] Running byteme.CartOperationsTest
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running byteme.OutOfStockTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Test Suite 1 — `OutOfStockTest` (9 tests)

Tests that the system correctly prevents ordering of unavailable items.

| Test | Description |
|------|-------------|
| `addUnavailableItemReturnsError` | `Cart.addItem()` returns `ERROR` for unavailable item |
| `cartRemainsEmptyAfterFailedAdd` | Cart remains empty and total stays Rs.0.00 |
| `addAvailableItemSucceeds` | Available item is added and total is correct |
| `mixedAddOnlyAvailableEntersCart` | Only available items enter the cart in a mixed scenario |
| `toggleAvailabilityPreventsFurtherAdds` | Re-adding an item after marking it unavailable is blocked |
| `anyQuantityOfUnavailableItemIsRejected` | Parameterised: quantities 1, 2, 5, 100 all rejected |

### Test Suite 2 — `CartOperationsTest` (15 tests)

Tests comprehensive cart lifecycle operations.

**Scenario 1 — Adding items updates total accurately**

| Test | Description |
|------|-------------|
| `addSingleItemUpdatesTotalCorrectly` | 2 × Rs.80 = Rs.160 |
| `addMultipleItemsAccumulatesTotal` | 2×80 + 3×50 = Rs.310 |
| `addSameItemTwiceMergesQuantity` | No duplicate entries; qty merges correctly |
| `addThreeItemsProducesCorrectTotal` | 80 + 100 + 120 = Rs.300 |

**Scenario 2 — Modifying quantity recalculates total**

| Test | Description |
|------|-------------|
| `increaseQuantityRecalculatesTotal` | 4 × Rs.80 = Rs.320 after update |
| `decreaseQuantityRecalculatesTotal` | 2 × Rs.120 = Rs.240 after decrease |
| `changingOneItemDoesNotAffectOthers` | Other cart items are unaffected |
| `updateNonExistentItemReturnsError` | Non-existent cart item returns ERROR |

**Scenario 3 — Negative/zero quantity is prevented**

| Test | Description |
|------|-------------|
| `setNegativeQuantityReturnsError` | `updateQuantity(-1)` returns ERROR; total unchanged |
| `setZeroQuantityReturnsError` | `updateQuantity(0)` returns ERROR; use `removeItem()` instead |
| `addItemWithZeroQuantityRejected` | `addItem(item, 0)` returns ERROR; cart stays empty |
| `addItemWithNegativeQuantityRejected` | `addItem(item, -5)` returns ERROR |

**Bonus — Remove operations**

| Test | Description |
|------|-------------|
| `removeItemUpdatesTotal` | Total decreases correctly after item removal |
| `removeNonExistentItemReturnsError` | ERROR returned for item not in cart |
| `cartEmptyAfterRemovingAllItems` | Cart is empty and total is Rs.0.00 |

---

## 📁 File Structure

```
Canteen_Management_Platform/
│
├── pom.xml                                    Maven build configuration
├── run.bat                                    Convenience run script (Windows)
├── README.md                                  This document
│
├── data/                                      Runtime data (auto-created)
│   ├── users.dat                              Registered customer records
│   ├── menu.txt                               Menu export for GUI
│   ├── pending_orders.txt                     Orders export for GUI
│   ├── orders_CUST-XXXX.txt                   Per-user order history files
│   └── cart_CUST-XXXX.tmp                     Temporary cart files (session)
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── byteme/
    │   │       │
    │   │       ├── Main.java                  Entry point: CLI → GUI
    │   │       │
    │   │       ├── model/
    │   │       │   ├── FoodItem.java          Menu item (Comparable)
    │   │       │   ├── Review.java            Customer review (rating + comment)
    │   │       │   ├── OrderItem.java         FoodItem + quantity line item
    │   │       │   ├── OrderStatus.java       Enum of all order states
    │   │       │   ├── Order.java             Order (Comparable, VIP-priority)
    │   │       │   ├── User.java              Abstract user base class
    │   │       │   ├── Customer.java          Customer (extends User)
    │   │       │   ├── Admin.java             Admin singleton (extends User)
    │   │       │   └── Cart.java              LinkedHashMap-backed shopping cart
    │   │       │
    │   │       ├── service/
    │   │       │   ├── MenuService.java       TreeMap menu + TreeSet category index
    │   │       │   ├── OrderService.java      PriorityQueue pending orders
    │   │       │   ├── CartService.java       Cart wrapper with file persistence
    │   │       │   ├── UserService.java       LinkedHashMap users + HashMap index
    │   │       │   └── ReportService.java     Daily sales report generator
    │   │       │
    │   │       ├── cli/
    │   │       │   ├── CLIMain.java           Root CLI menu (login/register/exit)
    │   │       │   ├── AdminCLI.java          Admin operations menu loop
    │   │       │   └── CustomerCLI.java       Customer operations menu loop
    │   │       │
    │   │       ├── gui/
    │   │       │   ├── GUIMain.java           JavaFX Application (TabPane root)
    │   │       │   ├── MenuTab.java           Page 1: Menu TableView + filters
    │   │       │   └── OrdersTab.java         Page 2: Orders TableView + detail
    │   │       │
    │   │       └── io/
    │   │           ├── FileManager.java       All file read/write operations
    │   │           └── DataPaths.java         Centralised file-path constants
    │   │
    │   └── resources/
    │       └── styles.css                     JavaFX dark theme stylesheet
    │
    └── test/
        └── java/
            └── byteme/
                ├── OutOfStockTest.java        9 tests: availability guard
                └── CartOperationsTest.java    15 tests: cart operations
```

---

## 💾 Data Persistence

### Persistence Summary

| Data | File | When Written | When Read |
|------|------|-------------|-----------|
| Customer accounts | `users.dat` | Register, VIP upgrade, balance change | Every application start |
| Order history | `orders_CUST-XXXX.txt` | On order placed, canceled, and on logout | Manual inspection only |
| Cart session | `cart_CUST-XXXX.tmp` | On every cart mutation | Session only; deleted on checkout |
| Menu (GUI export) | `menu.txt` | On CLI exit (option 4) | GUI startup |
| Orders (GUI export) | `pending_orders.txt` | On CLI exit (option 4) | GUI startup |

### Data Format

**`users.dat` — pipe-delimited:**
```
CUSTOMER|<userId>|<username>|<password>|<name>|<isVip>|<balance>
```

**`menu.txt` — pipe-delimited:**
```
<name>|<category>|<price>|<isAvailable>
```

**`pending_orders.txt` — pipe-delimited:**
```
<orderId>|<customerName>|<items>|<status>|<isVip>|<total>|<specialRequest>
```

---

## 🎨 Design Decisions

| Decision | Rationale |
|----------|-----------|
| **`Cart` returns `String` results** | All cart mutations return `"SUCCESS"` or `"ERROR: ..."`, enabling direct JUnit assertions without mocking or output capture |
| **`Order implements Comparable`** | Enables the `PriorityQueue` to sort orders natively by VIP status then timestamp, with no external comparator needed |
| **Singleton services** | `MenuService`, `OrderService`, and `UserService` are singletons so all CLI classes share the same in-memory state throughout a session |
| **GUI reads files, not singletons** | The GUI is launched in the same JVM but is architecturally isolated; it reads exported files rather than calling services, enforcing the read-only constraint |
| **`Admin` as a hard-coded Singleton** | Only one admin account exists; no file persistence is needed for credentials |
| **`LinkedHashMap` for Cart** | Cart insertion order is preserved so customers see items in the order they added them, improving usability |
| **`TreeMap` for Menu** | Items are always returned in alphabetical order without explicit sorting, making the menu display consistent |
| **Text files over Java serialization** | Plain-text pipe-delimited files are human-readable, debuggable, and immune to Java class version incompatibilities |
| **Phase 1 CLI, Phase 2 GUI** | Sequential execution (not parallel threads) matches the assignment requirement and avoids concurrency complexity |

---

## ⚠️ Known Limitations

| Limitation | Description |
|-----------|-------------|
| **Session-only in-memory state** | Order history and the order counter reset when the JVM exits. Persistent loading of order history on startup is not implemented |
| **No password hashing** | Passwords are stored in plain text in `users.dat`. This is acceptable for a college assignment but is not production-safe |
| **GUI data is a snapshot** | The GUI shows data as of the moment the CLI exited. It does not auto-refresh |
| **Single-admin system** | Only one admin account exists (`admin / admin123`); multi-admin support is not implemented |
| **No payment gateway** | Balance is a simple double field; no real payment integration is present |
| **Windows-only `run.bat`** | The convenience script uses Windows path conventions. Linux/macOS users should invoke Maven directly |

---
<div align="center">

**Built with ☕ Java · 🎨 JavaFX · 🧪 JUnit 5 · 📦 Maven**

*Byte Me! — Because every late-night coding session deserves a good meal.* 🍽️

</div>
