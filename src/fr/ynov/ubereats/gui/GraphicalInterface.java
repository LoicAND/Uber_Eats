package fr.ynov.ubereats.gui;

import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.payment.Payment;
import fr.ynov.ubereats.domain.payment.PaymentStatus;
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

/**
 * Main graphical user interface for the Uber Eats application.
 * This class handles the display and interaction with all UI components,
 * providing access to all system functionalities through a tabbed interface.
 * <p>
 * The interface consists of three main panels:
 * - Restaurants panel: Displays available restaurants and allows placing orders
 * - Orders panel: Shows the user's orders and their status
 * - Connection panel: Handles user login and logout
 * <p>
 * The class coordinates with various services to perform business operations
 * like authentication, ordering, payment processing, and delivery tracking.
 *
 * @author Loïc ANDRIANARIVONY
 */

public class GraphicalInterface extends JFrame {

    /**
     * Service for user authentication and profile management.
     * Service for restaurant and menu management.
     * Service for order creation and tracking.
     * Service for payment processing.
     * Service for delivery assignment and coordination.
     */
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final DeliveryService deliveryService;

    /**
     * Constructs a new GraphicalInterface with all required services.
     * Initializes the main window and sets up all UI components.
     *
     * @param userService Service for user authentication and management
     * @param restaurantService Service for restaurant data
     * @param orderService Service for order management
     * @param paymentService Service for payment processing
     * @param deliveryService Service for delivery coordination
     */
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

        JTabbedPane mainTabbedPane = new JTabbedPane();

        JPanel restaurantsPanel = createRestaurantsPanel();
        JPanel ordersPanel = createOrdersPanel();
        JPanel connectionPanel = createConnectionPanel();

        mainTabbedPane.addTab("Restaurants", restaurantsPanel);
        mainTabbedPane.addTab("Mes commandes", ordersPanel);
        mainTabbedPane.addTab("Connexion", connectionPanel);

        setContentPane(mainTabbedPane);
    }

    /**
     * Creates and configures the restaurants panel.
     * This panel displays a list of available restaurants and allows users to place orders.
     *
     * @return A configured JPanel for the restaurants tab
     */
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

        JButton orderButton = new JButton("Commander");
        orderButton.addActionListener(_ -> {
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

    /**
     * Creates and configures the orders panel.
     * This panel shows the user's current and past orders with their status.
     *
     * @return A configured JPanel for the orders tab
     */
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
        JButton cancelButton = new JButton("Annuler commande");

        detailsButton.addActionListener(_ -> {
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

        refreshButton.addActionListener(_ -> refreshOrders(tableModel));

        cancelButton.addActionListener(_ -> {
            if (!userService.isLoggedIn()) {
                showAlert("Connexion requise", "Veuillez vous connecter pour annuler une commande.");
                return;
            }

            if (!(userService.getConnectedUser() instanceof Customers)) {
                showAlert("Accès refusé", "Réservé aux clients.");
                return;
            }

            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow != -1) {
                String orderId = tableModel.getValueAt(selectedRow, 0).toString();
                cancelOrder(orderId, tableModel);
            } else {
                showAlert("Sélection", "Veuillez sélectionner une commande à annuler.");
            }
        });

        buttonsPanel.add(detailsButton);
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(cancelButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Refreshes the orders table with current data from the service.
     * Uses a SwingWorker to perform the operation asynchronously.
     *
     * @param tableModel The table model to update with order data
     */
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

    /**
     * Displays detailed information about a specific order.
     * Shows restaurant information, order items, quantities, prices and status.
     *
     * @param orderId The ID of the order to display
     */
    private void displayOrderDetails(String orderId) {
        Order order = orderService.getOrderById(orderId);

        if (order != null) {
            StringBuilder details = new StringBuilder();
            details.append("Restaurant: ").append(order.getRestaurant().getName()).append("\n");
            details.append("Statut: ").append(order.getStatus()).append("\n");
            details.append("Date de création: ").append(order.getCreationDate()).append("\n\n");

            details.append("Articles commandés:\n");
            for (fr.ynov.ubereats.domain.order.CartLine line : order.getLines()) {
                details.append("- ").append(line.getQuantity()).append(" x ")
                        .append(line.getDish().getName()).append(": ")
                        .append(line.getTotalPrice()).append("€\n");
            }

            details.append("\nSous-total: ").append(order.getTotalPrice() - order.getDeliveryFees()).append("€\n");
            details.append("Frais de livraison: ").append(order.getDeliveryFees()).append("€\n");
            details.append("Total: ").append(order.getTotalPrice()).append("€");

            showAlert("Détails de la commande", details.toString());
        }
    }

    /**
     * Retrieves a Restaurant object based on the selected list item.
     *
     * @param restaurantSelection The selected restaurant string from the list
     * @return The corresponding Restaurant object, or null if none was selected
     */
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

    /**
     * Updates the cart display in the order dialog.
     * Shows the current items, quantities, prices, and totals.
     *
     * @param order The order containing cart items
     * @param orderDialog The dialog to update
     */
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
        double subtotalPrice = 0;

        for (fr.ynov.ubereats.domain.order.CartLine line : order.getLines()) {
            cartModel.addRow(new Object[]{
                    line.getDish().getName(),
                    line.getQuantity(),
                    line.getDish().getPrice() + "€",
                    line.getTotalPrice() + "€"
            });
            subtotalPrice += line.getTotalPrice();
        }

        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(3, 2));
        JLabel subtotalLabel = new JLabel("Sous-total: ");
        JLabel subtotalValueLabel = new JLabel(subtotalPrice + "€");
        JLabel deliveryLabel = new JLabel("Frais de livraison: ");
        JLabel deliveryValueLabel = new JLabel(order.getDeliveryFees() + "€");
        JLabel totalLabel = new JLabel("Total: ");
        JLabel totalValueLabel = new JLabel(order.getTotalPrice() + "€");

        subtotalValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        deliveryValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalValueLabel.setFont(totalValueLabel.getFont().deriveFont(Font.BOLD));

        summaryPanel.add(subtotalLabel);
        summaryPanel.add(subtotalValueLabel);
        summaryPanel.add(deliveryLabel);
        summaryPanel.add(deliveryValueLabel);
        summaryPanel.add(totalLabel);
        summaryPanel.add(totalValueLabel);

        cartPanel.add(summaryPanel, BorderLayout.SOUTH);
        orderDialog.getContentPane().add(cartPanel, BorderLayout.EAST);

        orderDialog.setSize(700, 400);
        orderDialog.revalidate();
        orderDialog.repaint();
    }

    /**
     * Starts the automated order tracking process.
     * Uses timers to simulate the progression of an order through different stages.
     *
     * @param orderId The ID of the order to track
     */
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

    /**
     * Displays a dialog showing the real-time status of an order.
     * Updates automatically as the order progresses through different stages.
     *
     * @param orderId The ID of the order to display
     * @param parentFrame The parent frame for the dialog
     */
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

                    switch (currentOrder.getStatus()) {
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

    /**
     * Converts a PaymentMethod enum to a user-friendly display string.
     *
     * @param method The PaymentMethod to convert
     * @return A localized string representing the payment method
     */
    private String getMethodLabel(PaymentMethod method) {
        return switch (method) {
            case CREDIT_CARD -> "Carte de crédit";
            case PAYPAL -> "PayPal";
            case TICKET -> "Ticket restaurant";
        };
    }

    /**
     * Opens the order interface for a specific restaurant.
     * Allows users to browse the menu, add items to cart, and complete the order.
     *
     * @param restaurant The restaurant from which to order
     */
    private void openOrderInterface(Restaurant restaurant) {
        Customers customer = (Customers) userService.getConnectedUser();

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

        addButton.addActionListener(_ -> {
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

        orderButton.addActionListener(_ -> {
            if (order.getLines().isEmpty()) {
                JOptionPane.showMessageDialog(orderDialog,
                        "Votre panier est vide. Veuillez ajouter au moins un plat.",
                        "Panier vide",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JPanel paymentPanel = new JPanel(new GridLayout(0, 1));
            paymentPanel.setBorder(BorderFactory.createTitledBorder("Choisissez votre méthode de paiement"));

            ButtonGroup methodGroup = new ButtonGroup();
            JRadioButton[] methodButtons = new JRadioButton[PaymentMethod.values().length];

            int i = 0;
            for (PaymentMethod method : PaymentMethod.values()) {
                methodButtons[i] = new JRadioButton(getMethodLabel(method));
                if (method == PaymentMethod.CREDIT_CARD) {
                    methodButtons[i].setSelected(true);
                }
                methodGroup.add(methodButtons[i]);
                paymentPanel.add(methodButtons[i]);
                i++;
            }

            int result = JOptionPane.showConfirmDialog(
                    orderDialog,
                    paymentPanel,
                    "Paiement",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                PaymentMethod selectedMethod = PaymentMethod.CREDIT_CARD;
                for (i = 0; i < methodButtons.length; i++) {
                    if (methodButtons[i].isSelected()) {
                        selectedMethod = PaymentMethod.values()[i];
                        break;
                    }
                }

                JOptionPane.showMessageDialog(
                        orderDialog,
                        "Traitement du paiement en cours...",
                        "Paiement",
                        JOptionPane.INFORMATION_MESSAGE
                );

                Payment payment = paymentService.createPayment(order, selectedMethod);

                if (payment.getStatus() == PaymentStatus.ACCEPTED) {
                    String[] options = {"Pas de pourboire", "5%", "10%", "15%", "Montant personnalisé"};
                    int tipChoice = JOptionPane.showOptionDialog(
                            orderDialog,
                            "Souhaitez-vous ajouter un pourboire pour le livreur?",
                            "Ajouter un pourboire",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    double tipAmount = 0.0;
                    if (tipChoice > 0 && tipChoice < 4) {
                        int percentage = (tipChoice == 1) ? 5 : (tipChoice == 2) ? 10 : 15;
                        tipAmount = order.getTotalPrice() * percentage / 100.0;
                    } else if (tipChoice == 4) {
                        String input = JOptionPane.showInputDialog(
                                orderDialog,
                                "Veuillez entrer le montant du pourboire (€):",
                                "Pourboire personnalisé",
                                JOptionPane.PLAIN_MESSAGE
                        );

                        if (input != null && !input.isEmpty()) {
                            try {
                                tipAmount = Double.parseDouble(input.replace(',', '.'));
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(
                                        orderDialog,
                                        "Montant invalide. Aucun pourboire ne sera ajouté.",
                                        "Erreur",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }
                    }

                    if (tipAmount > 0) {
                        payment.addTip(tipAmount);
                        JOptionPane.showMessageDialog(
                                orderDialog,
                                String.format("Merci! Un pourboire de %.2f€ a été ajouté.", tipAmount),
                                "Pourboire ajouté",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                    JTextArea receiptArea = new JTextArea(payment.receiptOrder());
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
                    JOptionPane.showMessageDialog(
                            orderDialog,
                            "Le paiement a été refusé. Veuillez réessayer.",
                            "Échec du paiement",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                        orderDialog,
                        "Paiement annulé",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(orderButton);

        orderDialog.add(dishPanel, BorderLayout.CENTER);
        orderDialog.add(buttonPanel, BorderLayout.SOUTH);

        orderDialog.setLocationRelativeTo(this);
        orderDialog.setVisible(true);
    }

    /**
     * Displays an information alert dialog with the specified title and message.
     *
     * @param title The title of the alert
     * @param message The message to display
     */
    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates the visibility of login/logout buttons based on user authentication status.
     *
     * @param loginButton The login button to update
     * @param logoutButton The logout button to update
     */
    private void updateButtonsVisibility(JButton loginButton, JButton logoutButton) {
        boolean isLoggedIn = userService.isLoggedIn();
        loginButton.setVisible(!isLoggedIn);
        logoutButton.setVisible(isLoggedIn);
    }

    /**
     * Creates and configures the user connection panel.
     * Provides fields for email and password entry, and buttons for login/logout.
     *
     * @return A configured JPanel for the connection tab
     */
    private JPanel createConnectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(usernameField.getPreferredSize());
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setText("utilisateurs@exemple.com");

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setText("");

        JButton loginButton = new JButton("Connexion");
        JButton logoutButton = new JButton("Déconnexion");

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        updateButtonsVisibility(loginButton, logoutButton);

        loginButton.addActionListener(_ -> {
            String email = usernameField.getText();
            String password = new String(passwordField.getPassword());

            boolean loginSuccessful = userService.login(email, password);
            if (loginSuccessful) {
                showAlert("Connexion", "Connexion réussie!");
                usernameField.setText("");
                passwordField.setText("");
                updateButtonsVisibility(loginButton, logoutButton);
            } else {
                showAlert("Erreur", "Échec de connexion. Vérifiez vos identifiants.");
            }
        });

        logoutButton.addActionListener(_ -> {
            if (userService.isLoggedIn()) {
                userService.logout();
                showAlert("Déconnexion", "Déconnexion réussie!");
                // Mettre à jour la visibilité des boutons après déconnexion
                updateButtonsVisibility(loginButton, logoutButton);
            } else {
                showAlert("Erreur", "Utilisateur non connecté.");
            }
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Identifiant"));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Mot de passe"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(loginButton);
        panel.add(logoutButton);

        return panel;
    }

    /**
     * Cancels an order after confirmation.
     * Only allows cancellation for orders that are not delivered or already canceled.
     *
     * @param orderId The ID of the order to cancel
     * @param tableModel The table model to update after cancellation
     */
    private void cancelOrder(String orderId, DefaultTableModel tableModel) {
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            showAlert("Erreur", "Commande introuvable.");
            return;
        }

        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            showAlert("Impossible d'annuler", STR."Cette commande ne peut plus être annulée car elle est déjà \{order.getStatus() == OrderStatus.DELIVERED ? "livrée." : "annulée."}");
            return;
        }

        int response = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir annuler cette commande ?",
                "Confirmation d'annulation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            if (orderService.cancelOrder(orderId)) {
                showAlert("Succès", "Votre commande a été annulée avec succès.");

                tableModel.setRowCount(0);
                refreshOrders(tableModel);
            } else {
                showAlert("Erreur", "Impossible d'annuler cette commande.");
            }
        }
    }
}