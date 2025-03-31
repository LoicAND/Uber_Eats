package fr.ynov.ubereats.gui;

import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.restaurant.Restaurant;
import fr.ynov.ubereats.domain.user.Customers;
import fr.ynov.ubereats.service.OrderService;
import fr.ynov.ubereats.service.PaymentService;
import fr.ynov.ubereats.service.RestaurantService;
import fr.ynov.ubereats.service.UserService;
import fr.ynov.ubereats.configuration.Configuration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;
import java.util.List;

public class GraphicalInterface extends JFrame {
    private UserService userService;
    private RestaurantService restaurantService;
    private OrderService orderService;
    private PaymentService paymentService;

    // UI Components
    private JTabbedPane mainTabbedPane;
    private JPanel restaurantsPanel;
    private JPanel ordersPanel;
    private JPanel connectionPanel;

    public GraphicalInterface(UserService userService, RestaurantService restaurantService, OrderService orderService, PaymentService paymentService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
        this.paymentService = paymentService;

        // Set up the main frame
        setTitle("UberEats Clone");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main tabbed pane
        mainTabbedPane = new JTabbedPane();

        // Create tabs
        restaurantsPanel = createRestaurantsPanel();
        ordersPanel = createOrdersPanel();
        connectionPanel = createConnectionPanel();

        // Add tabs to tabbed pane
        mainTabbedPane.addTab("Restaurants", restaurantsPanel);
        mainTabbedPane.addTab("My Orders", ordersPanel);
        mainTabbedPane.addTab("Connection", connectionPanel);

        // Set the tabbed pane as the content pane
        setContentPane(mainTabbedPane);
    }

    private JPanel createRestaurantsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Restaurant list
        DefaultListModel<String> restaurantListModel = new DefaultListModel<>();
        JList<String> restaurantList = new JList<>(restaurantListModel);

        // Populate restaurant list
        List<Restaurant> restaurants = restaurantService.listRestaurants();
        restaurants.forEach(restaurant ->
                restaurantListModel.addElement(restaurant.getName() + " - " + restaurant.getAddress())
        );

        // Scroll pane for restaurant list
        JScrollPane listScrollPane = new JScrollPane(restaurantList);
        panel.add(listScrollPane, BorderLayout.CENTER);

        // Order button
        JButton orderButton = new JButton("Order");
        orderButton.addActionListener(e -> {
            if (!(userService.getConnectedUser() instanceof Customers)) {
                showAlert("Connection Required", "Please log in as a client to place an order.");
                return;
            }

            String selectedRestaurant = restaurantList.getSelectedValue();
            Restaurant restaurant = getSelectedRestaurant(selectedRestaurant);
            if (restaurant != null) {
                openOrderInterface(restaurant);
            }
        });

        panel.add(orderButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Orders table model
        String[] columnNames = {"Order ID", "Restaurant", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable ordersTable = new JTable(tableModel);

        // Scroll pane for orders table
        JScrollPane tableScrollPane = new JScrollPane(ordersTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        JButton detailsButton = new JButton("View Details");
        JButton refreshButton = new JButton("Refresh");

        detailsButton.addActionListener(e -> {
            if (!userService.isLoggedIn()) {
                showAlert("Connection Required", "Please log in to view your orders.");
                return;
            }

            if (!(userService.getConnectedUser() instanceof Customers)) {
                showAlert("Access Denied", "Only clients can view their orders.");
                return;
            }

            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow != -1) {
                String orderId = tableModel.getValueAt(selectedRow, 0).toString();
                displayOrderDetails(orderId);
            }
        });

        refreshButton.addActionListener(e -> refreshOrders(tableModel));

        buttonsPanel.add(detailsButton);
        buttonsPanel.add(refreshButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshOrders(DefaultTableModel tableModel) {
        new SwingWorker<Void, Order>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1000);

                if (!(userService.getConnectedUser() instanceof Customers connectedClient)) {
                    SwingUtilities.invokeLater(() -> showAlert("Error", "Please log in."));
                    return null;
                }

                List<Order> orders = orderService.listOrdersByClient(connectedClient);

                for (Order order : orders) {
                    publish(order);
                }
                return null;
            }

            @Override
            protected void process(List<Order> chunks) {
                for (Order order : chunks) {
                    Vector<String> row = new Vector<>();
                    row.add(String.valueOf(order.getId()));
                    row.add(order.getRestaurant().getName());
                    row.add(order.getStatus().name());

                    tableModel.addRow(row);
                }
            }

            @Override
            protected void done() {
                showAlert("Info", "Orders refreshed successfully!");
            }
        }.execute();
    }

    private void displayOrderDetails(String orderId) {

        Order order = orderService.getOrderById(Long.parseLong(orderId));

        if (order != null) {
            String details = String.format(
                    "Restaurant: %s\nStatus: %s\nCreation Date: %s",
                    order.getRestaurant().getName(),
                    order.getStatus(),
                    order.getCreationDate()
            );
            showAlert("Order Details", details);
        }
    }

    private Restaurant getSelectedRestaurant(String restaurantSelection) {
        if (restaurantSelection == null) {
            showAlert("Selection", "Please select a restaurant.");
            return null;
        }

        String restaurantName = restaurantSelection.split(" - ")[0];

        return restaurantService.listRestaurants().stream()
                .filter(restaurant -> restaurant.getName().equals(restaurantName))
                .findFirst()
                .orElse(null);
    }

    private void openOrderInterface(Restaurant restaurant) {
        showAlert("Order", "Order functionality to be developed for " + restaurant.getName());
    }

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createConnectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(usernameField.getPreferredSize());
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setText("Username");

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setText("Password");

        JButton loginButton = new JButton("Log In");
        JButton logoutButton = new JButton("Log Out");

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> {
            String email = usernameField.getText();
            String password = new String(passwordField.getPassword());

            boolean loginSuccessful = userService.login(email, password);
            if (loginSuccessful) {
                showAlert("Login", "Login successful!");
                usernameField.setText("");
                passwordField.setText("");
            } else {
                showAlert("Error", "Login failed");
            }
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(loginButton);
        panel.add(logoutButton);

        return panel;
    }
}

