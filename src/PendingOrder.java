
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PendingOrder {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private LinkedList<Order> checkerCustomerList;
    private DefaultTableModel tableModel;
    private JTable ordersTable;
    private final ByteMe byteMe;

    public PendingOrder(CardLayout cardLayout, JPanel mainPanel, ByteMe byteMe) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.byteMe = byteMe;
        this.checkerCustomerList = byteMe.CheckerCustomerList;
    }

    public JPanel createPendingOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Pending Orders", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "ShowMenu"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(backButton);

        // Create the table for pending orders
        String[] columnNames = {"Item Name", "Delivered", "Preparing", "Cancelled", "Out for Delivery"};
        tableModel = new DefaultTableModel(columnNames, 0); // Initialize the model
        ordersTable = new JTable(tableModel); // Initialize the table
        ordersTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Populate the table initially
        refreshTable();

        return panel;
    }

    // Helper method to refresh the table data
    public void refreshTable() {
        // Clear existing data
        tableModel.setRowCount(0);

        // Map to track item counts by statuses
        Map<String, int[]> itemStatusCounts = new TreeMap<>();

        // Populate the status counts from the order list
        for (Order order : checkerCustomerList) {
            for (Map.Entry<String, List<FoodItems>> entry : order.CartOrderList.entrySet()) {
                String status = order.OrderStatus.getOrDefault(entry.getKey(), "Unknown");

                for (FoodItems item : entry.getValue()) {
                    String itemName = item.name;

                    // Get or initialize the counts array: [Delivered, Preparing, Cancelled, Out for Delivery]
                    itemStatusCounts.putIfAbsent(itemName, new int[4]);
                    int[] counts = itemStatusCounts.get(itemName);

                    // Increment the appropriate status count
                    switch (status) {
                        case "delivered" -> counts[0]++;
                        case "preparing" -> counts[1]++;
                        case "cancelled" -> counts[2]++;
                        case "out for delivery" -> counts[3]++;
                    }
                }
            }
        }

        // Populate the table with summarized data
        for (Map.Entry<String, int[]> entry : itemStatusCounts.entrySet()) {
            String itemName = entry.getKey();
            int[] counts = entry.getValue();

            // Add a row for each item
            tableModel.addRow(new Object[]{itemName, counts[0], counts[1], counts[2], counts[3]});
        }

        // Notify the table that data has changed
        tableModel.fireTableDataChanged();
    }
}




