
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ShowMenu {
    private TreeMap<String, List<FoodItems>> menulist;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public ShowMenu(CardLayout cardLayout, JPanel mainPanel,ByteMe byte_me) {
        menulist = byte_me.menu.MenuCategory;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    public JPanel createShowMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Restaurant Menu", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        for (Map.Entry<String, List<FoodItems>> entry : menulist.entrySet()) {
            String categoryName = entry.getKey();
            List<FoodItems> foodItems = entry.getValue();

            JPanel categoryPanel = new JPanel(new BorderLayout());
            String[] columnNames = {"Index", "Name", "Price (\u20B9)", "Available", "Rating"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            int index = 1;
            for (FoodItems item : foodItems) {
                Object[] row = {
                        index,
                        item.getName(),
                        "\u20B9 " + item.getPrice(),
                        item.isAvailable() ? "Yes" : "No",
                        String.format("%.1f / 5.0", item.getRating())
                };
                tableModel.addRow(row);
                index++;
            }

            JTable table = new JTable(tableModel);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

            JScrollPane scrollPane = new JScrollPane(table);
            categoryPanel.add(scrollPane, BorderLayout.CENTER);
            tabbedPane.addTab(categoryName, categoryPanel);
        }

        // Navigation buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton backButton = new JButton("Back");
        JButton nextButton = new JButton("Pending Orders");

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "EnterPanel"));
        nextButton.addActionListener(e -> cardLayout.show(mainPanel, "PendingOrder"));

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);

        // Assemble the panel
        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
}
