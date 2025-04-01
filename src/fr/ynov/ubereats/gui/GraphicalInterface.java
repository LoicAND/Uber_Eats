package fr.ynov.ubereats.gui;

import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.payment.Payment;
import fr.ynov.ubereats.domain.restaurant.Restaurant;
import fr.ynov.ubereats.domain.user.Customers;
import fr.ynov.ubereats.domain.user.Deliver;
import fr.ynov.ubereats.service.*;
import fr.ynov.ubereats.domain.order.OrderStatus;
import fr.ynov.ubereats.domain.payment.PaymentMethod;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.List;

public class GraphicalInterface extends JFrame {
    private UserService userService;
    private RestaurantService restaurantService;
    private OrderService orderService;
    private PaymentService paymentService;
    private DeliveryService deliveryService;

    private JTabbedPane mainTabbedPane;
    private JPanel restaurantsPanel;
    private JPanel ordersPanel;
    private JPanel connectionPanel;

    public GraphicalInterface(
            UserService userService,
            RestaurantService restaurantService,
            OrderService orderService,
            PaymentService paymentService,
            DeliveryService deliveryService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.deliveryService = deliveryService;

        setTitle("UberEats Clone");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainTabbedPane = new JTabbedPane();

        restaurantsPanel = createRestaurantsPanel();
        ordersPanel = createOrdersPanel();
        connectionPanel = createConnectionPanel();

        mainTabbedPane.addTab("Restaurants", restaurantsPanel);
        mainTabbedPane.addTab("Mes commandes", ordersPanel);
        mainTabbedPane.addTab("Connexion", connectionPanel);

        setContentPane(mainTabbedPane);
    }

    private JPanel createRestaurantsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<String> restaurantListModel = new DefaultListModel<>();
        JList<String> restaurantList = new JList<>(restaurantListModel);

        List<Restaurant> restaurants = restaurantService.listRestaurants();
        restaurants.forEach(restaurant ->
                restaurantListModel.addElement(restaurant.getName() + " - " + restaurant.getAddress())
        );

        JScrollPane listScrollPane = new JScrollPane(restaurantList);
        panel.add(listScrollPane, BorderLayout.CENTER);

        JButton orderButton = new JButton("Commande");
        orderButton.addActionListener(e -> {
            if (!(userService.getConnectedUser() instanceof Customers)) {
                showAlert("Connexion requise", "Pour commander, veuillez vous connecter.");
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

        String[] columnNames = {"Order ID", "Restaurant", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable ordersTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(ordersTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton detailsButton = new JButton("Voir Details");
        JButton refreshButton = new JButton("Rafraichir");

        detailsButton.addActionListener(e -> {
            if (!userService.isLoggedIn()) {
                showAlert("Connexion requise", "Veuillez vous connecter pour voir vos détails.");
                return;
            }

            if (!(userService.getConnectedUser() instanceof Customers)) {
                showAlert("Accès refusé", "Réservé aux clients.");
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
                    SwingUtilities.invokeLater(() -> showAlert("Erreur", "Connectez-vous."));
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
                showAlert("Info", "Commandes mises à jour.");
            }
        }.execute();
    }

    private void displayOrderDetails(String orderId) {

        Order order = orderService.getOrderById(orderId);

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
            showAlert("Selection", "Veuillez sélectionner un restaurant.");
            return null;
        }

        String restaurantName = restaurantSelection.split(" - ")[0];

        return restaurantService.listRestaurants().stream()
                .filter(restaurant -> restaurant.getName().equals(restaurantName))
                .findFirst()
                .orElse(null);
    }

    private void updateCartDisplay(Order order, JDialog orderDialog) {
        Component[] components = orderDialog.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel && "cartPanel".equals(component.getName())) {
                orderDialog.getContentPane().remove(component);
                break;
            }
        }

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setName("cartPanel");

        DefaultTableModel cartModel = new DefaultTableModel(
                new String[]{"Plat", "Quantité", "Prix unitaire", "Total"}, 0);

        JTable cartTable = new JTable(cartModel);
        double totalPrice = 0;

        for (fr.ynov.ubereats.domain.order.CartLine line : order.getLines()) {
            cartModel.addRow(new Object[]{
                    line.getDish().getName(),
                    line.getQuantity(),
                    line.getDish().getPrice() + "€",
                    line.getTotalPrice() + "€"
            });
            totalPrice += line.getTotalPrice();
        }

        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        JLabel totalLabel = new JLabel("Total: " + totalPrice + "€");
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        cartPanel.add(totalLabel, BorderLayout.SOUTH);

        orderDialog.getContentPane().add(cartPanel, BorderLayout.EAST);

        orderDialog.setSize(700, 400);

        orderDialog.revalidate();
        orderDialog.repaint();
    }

    public void startOrderTracking(String orderId) {
        Order order = orderService.getOrderById(orderId);

        if (order == null || order.getStatus() != OrderStatus.ACCEPTED) {
            return;
        }

        int preparationDelay = 10000;
        int deliveryDelay = 20000;
        int completeDelay = 30000;

        Timer orderTimer = new Timer(true);

        orderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                orderService.updateOrderStatus(orderId, OrderStatus.IN_PREPARATION);
            }
        }, preparationDelay);

        orderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Deliver assignedDeliverer = deliveryService.autoAssignDeliverer(orderId);

                if (assignedDeliverer != null) {
                    orderService.updateOrderStatus(orderId, OrderStatus.IN_DELIVERY);
                }
            }
        }, deliveryDelay);

        orderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                orderService.updateOrderStatus(orderId, OrderStatus.DELIVERED);
                deliveryService.completeDelivery(orderId);
            }
        }, completeDelay);
    }

    public void showOrderStatusTracker(String orderId, JFrame parentFrame) {
        JDialog trackerDialog = new JDialog(parentFrame, "Suivi de commande", false);
        trackerDialog.setLayout(new BorderLayout());

        JPanel statusPanel = new JPanel(new GridLayout(2, 1));
        JLabel statusLabel = new JLabel("Statut: " + orderService.getOrderById(orderId).getStatus());
        JLabel delivererLabel = new JLabel("Livreur: Non assigné");
        statusPanel.add(statusLabel);
        statusPanel.add(delivererLabel);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        trackerDialog.add(statusPanel, BorderLayout.NORTH);
        trackerDialog.add(progressBar, BorderLayout.CENTER);

        Timer uiTimer = new Timer(true);
        uiTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    Order currentOrder = orderService.getOrderById(orderId);
                    statusLabel.setText("Statut: " + currentOrder.getStatus());

                    if (currentOrder.getStatus() == OrderStatus.IN_DELIVERY) {
                        Deliver deliverer = deliveryService.getDelivererForOrder(orderId);
                        if (deliverer != null) {
                            delivererLabel.setText("Deliver: " + deliverer.getName() +
                                    " (" + deliverer.getVehicleType() + ")");
                        }
                    }

                    switch(currentOrder.getStatus()) {
                        case ACCEPTED:
                            progressBar.setValue(20);
                            break;
                        case IN_PREPARATION:
                            progressBar.setValue(40);
                            break;
                        case IN_DELIVERY:
                            progressBar.setValue(80);
                            break;
                        case DELIVERED:
                            progressBar.setValue(100);
                            uiTimer.cancel();
                            break;
                    }
                });
            }
        }, 0, 5000);

        trackerDialog.setSize(300, 150);
        trackerDialog.setLocationRelativeTo(parentFrame);
        trackerDialog.setVisible(true);
    }

    private void openOrderInterface(Restaurant restaurant) {
        Customers customer = (Customers)userService.getConnectedUser();

        String orderId = java.util.UUID.randomUUID().toString();
        Order order = orderService.createOrder(orderId, customer, restaurant);

        JDialog orderDialog = new JDialog(this, "Commander chez " + restaurant.getName(), true);
        orderDialog.setSize(400, 300);
        orderDialog.setLayout(new BorderLayout());

        JPanel dishPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> dishListModel = new DefaultListModel<>();
        JList<String> dishList = new JList<>(dishListModel);

        restaurant.getMenu().forEach(dish ->
                dishListModel.addElement(dish.getName() + " - " + dish.getPrice() + "€")
        );

        JScrollPane dishScrollPane = new JScrollPane(dishList);
        dishPanel.add(dishScrollPane, BorderLayout.CENTER);

        JPanel quantityPanel = new JPanel();
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        quantityPanel.add(new JLabel("Quantité:"));
        quantityPanel.add(quantitySpinner);
        dishPanel.add(quantityPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Ajouter au panier");
        JButton orderButton = new JButton("Commander");

        addButton.addActionListener(e -> {
            String selectedDish = dishList.getSelectedValue();
            if (selectedDish != null) {
                int quantity = (int) quantitySpinner.getValue();
                String dishName = selectedDish.split(" - ")[0];

                restaurant.getMenu().stream()
                        .filter(dish -> dish.getName().equals(dishName))
                        .findFirst()
                        .ifPresent(dish -> {
                            orderService.addDishToOrder(orderId, dish, quantity);

                            JOptionPane.showMessageDialog(
                                    orderDialog,
                                    quantity + " x " + dishName + " ajouté au panier",
                                    "Ajout au panier",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            updateCartDisplay(order, orderDialog);
                        });
            } else {
                JOptionPane.showMessageDialog(
                        orderDialog,
                        "Veuillez sélectionner un plat",
                        "Sélection requise",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        orderButton.addActionListener(e -> {
            if (order.getLines().isEmpty()) {
                JOptionPane.showMessageDialog(orderDialog,
                        "Votre panier est vide. Veuillez ajouter au moins un plat.",
                        "Panier vide",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String paymentId = java.util.UUID.randomUUID().toString();
            Payment payment = new Payment(paymentId, order, order.getTotalPrice(), PaymentMethod.CREDIT_CARD);

            boolean paymentSuccess = payment.makePayment();

            if (paymentSuccess) {
                String receipt = payment.genereRecu();

                JTextArea receiptArea = new JTextArea(receipt);
                receiptArea.setEditable(false);
                receiptArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

                JScrollPane scrollPane = new JScrollPane(receiptArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(
                        orderDialog,
                        scrollPane,
                        "Reçu de commande",
                        JOptionPane.INFORMATION_MESSAGE
                );

                orderService.updateOrderStatus(orderId, OrderStatus.ACCEPTED);

                startOrderTracking(orderId);
                showOrderStatusTracker(orderId, this);

                orderDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(orderDialog,
                        "Échec du paiement. Veuillez réessayer.",
                        "Erreur de paiement",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(orderButton);

        orderDialog.add(dishPanel, BorderLayout.CENTER);
        orderDialog.add(buttonPanel, BorderLayout.SOUTH);

        orderDialog.setLocationRelativeTo(this);
        orderDialog.setVisible(true);
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
        usernameField.setText("ynov@.com");

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setText("*****");

        JButton loginButton = new JButton("Connexion");
        JButton logoutButton = new JButton("Deconnexion");

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> {
            String email = usernameField.getText();
            String password = new String(passwordField.getPassword());

            boolean loginSuccessful = userService.login(email, password);
            if (loginSuccessful) {
                showAlert("connexion", "Connexion réussie!");
                usernameField.setText("");
                passwordField.setText("");
            } else {
                showAlert("Erreur", "Echec de connexion. Vérifiez vos identifiants.");
            }
        });


        logoutButton.addActionListener(e -> {
            if (userService.isLoggedIn()) {
                userService.logout();
                showAlert("Déconnexion", "Déconnexion réussie!");
            } else {
                showAlert("Erreur", "utilisateur non connecté.");
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

