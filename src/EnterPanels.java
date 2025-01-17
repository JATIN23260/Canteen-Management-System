
import javax.swing.*;
import java.awt.*;

public class EnterPanels extends JFrame {
    public int OnWhichScreenIAm = 0;
    public boolean I_started_with_panel = false;
    public CardLayout cardLayout;
    public JPanel mainPanel;
    public ByteMe byteMe;

    public EnterPanels(ByteMe byteMe) {
        this.byteMe = byteMe;
        I_started_with_panel = true;

        setVisible(true);
        setTitle("Main Panel");
        setSize(800, 600); // Fixed size for consistent appearance
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the CardLayout container
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        initializePanels();

        // Add the main panel to the JFrame
        add(mainPanel, BorderLayout.CENTER);

        // Add a refresh button at the bottom
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 16));
        refreshButton.addActionListener(e -> {
            refreshPanels(); // Recreate all panels
        });

        // Add refresh button to a separate panel
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshPanel.add(refreshButton);
        add(refreshPanel, BorderLayout.SOUTH);

        // Show the initial panel
        cardLayout.show(mainPanel, "EnterPanel");

        setLocationRelativeTo(null); // Center the window
    }

    // Method to initialize the panels
    private void initializePanels() {
        mainPanel.removeAll(); // Clear any existing panels

        ShowMenu showMenu = new ShowMenu(cardLayout, mainPanel,byteMe);
        PendingOrder pendingOrder = new PendingOrder(cardLayout, mainPanel, byteMe);

        // Add panels to the CardLayout container
        mainPanel.add(createEnterPanel(cardLayout, mainPanel), "EnterPanel");
        mainPanel.add(showMenu.createShowMenuPanel(), "ShowMenu");
        mainPanel.add(pendingOrder.createPendingOrderPanel(), "PendingOrder");

        mainPanel.revalidate(); // Refresh the panel hierarchy
        mainPanel.repaint();
    }

    private JPanel createEnterPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel enterPanel = new JPanel(new BorderLayout());

        JLabel headerLabel = new JLabel("Welcome To ByteMe Choose Button to Navigate", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Buttons
        JButton menuButton = new JButton("Show Menu");
        JButton pendingOrderButton = new JButton("Pending Orders");

        // Set large button size
        Dimension buttonSize = new Dimension(200, 50);
        menuButton.setPreferredSize(buttonSize);
        pendingOrderButton.setPreferredSize(buttonSize);

        menuButton.setFont(new Font("Arial", Font.BOLD, 18));
        pendingOrderButton.setFont(new Font("Arial", Font.BOLD, 18));

        // Button actions to switch panels
        if (OnWhichScreenIAm == 1 && !I_started_with_panel) {
            menuButton.addActionListener(e -> cardLayout.show(mainPanel, "ShowMenu"));
        }
        if (OnWhichScreenIAm == 2 && !I_started_with_panel) {
            pendingOrderButton.addActionListener(e -> cardLayout.show(mainPanel, "PendingOrder"));
        }

        if (I_started_with_panel) {
            menuButton.addActionListener(e -> cardLayout.show(mainPanel, "ShowMenu"));
            pendingOrderButton.addActionListener(e -> cardLayout.show(mainPanel, "PendingOrder"));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(menuButton);
        buttonPanel.add(pendingOrderButton);

        enterPanel.add(headerLabel, BorderLayout.NORTH);
        enterPanel.add(buttonPanel, BorderLayout.CENTER);

        return enterPanel;
    }

    // Reinitialize all panels (called on refresh button click)
    private void refreshPanels() {
        initializePanels(); // Recreate all panels
        cardLayout.show(mainPanel, "EnterPanel");
        // Show the default panel
        if (OnWhichScreenIAm == 1) {
            cardLayout.show(mainPanel, "ShowMenu");
        }
        if (OnWhichScreenIAm == 2) {
            cardLayout.show(mainPanel, "EnterPanel");
        }
    }

    // Methods to show specific screens programmatically
    public void showMenuScreen() {
        cardLayout.show(mainPanel, "ShowMenu");
    }

    public void showPendingOrderScreen() {
        cardLayout.show(mainPanel, "PendingOrder");
    }
}
