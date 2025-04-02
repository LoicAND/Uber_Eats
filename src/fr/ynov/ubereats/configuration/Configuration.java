package fr.ynov.ubereats.configuration;

import fr.ynov.ubereats.domain.restaurant.Dish;
import fr.ynov.ubereats.domain.restaurant.Restaurant;
import fr.ynov.ubereats.domain.user.Customers;
import fr.ynov.ubereats.domain.user.Deliver;
import fr.ynov.ubereats.gui.GraphicalInterface;
import fr.ynov.ubereats.service.*;

/**
 * Main configuration class for the Uber Eats application.
 * Manages service initialization, demo data creation and GUI launch.
 * This class is responsible for bootstrapping the entire application
 * by setting up all necessary services and initial data.
 *
 * @author Loïc ANDRIANARIVONY
 */

public class Configuration {
    /**
     * Service managing user accounts, authentication and profile information.
     * Service managing restaurant data, menus and dish information.
     * Service managing order creation, updates and tracking.
     * Service handling payment processing and transaction records.
     * Service coordinating delivery assignments and logistics.
     */
    private UserService userService;
    private RestaurantService restaurantService;
    private OrderService orderService;
    private PaymentService paymentService;
    private DeliveryService deliveryService;


    /**
     * Initializes the configuration by creating instances of all services.
     * Sets up the core services needed for the application to function.
     */
    public void initialConfiguration() {
        this.userService = new UserService();
        this.restaurantService = new RestaurantService();
        this.orderService = new OrderService();
        this.paymentService = new PaymentService();
        this.deliveryService = new DeliveryService(orderService, userService);
    }

    /**
     * Main initialization method that orchestrates the complete application setup.
     * Calls other initialization methods in the proper sequence to ensure
     * all components are ready before the GUI launches.
     */
    public void initialize(){
        initialConfiguration();
        initializeUser();
        initializeRestaurant();
        launchInterface();
    }

    /**
     * Initializes demo users and adds them to the user service.
     * Creates sample customers and delivery personnel with realistic test data.
     */
    private void initializeUser() {
        // Create demo users
        Customers client1 = new Customers(
                "Jean",
                "Jean Dupont",
                "jean.dupont@email.com",
                "0612345678",
                "motdepasse123",
                "123 Rue de Paris, 75001 Paris"
        );

        Customers client2 = new Customers(
                "Marie",
                "Marie Martin",
                "marie.martin@email.com",
                "0687654321",
                "securite456",

                "45 Avenue des Champs, 75008 Paris"
        );

        Deliver livreur1 = new Deliver(
                "Pierre",
                "Pierre Legrand",
                "pierre.legrand@gmail.com",
                "0679450306",
                "Vélo"

        );

        Deliver livreur2 = new Deliver(
                "Louis",
                "Louis Georges",
                "louis.georges@gmail.com",
                "0708090102",
                "Scooter"

        );

        userService.addUser(client1);
        userService.addUser(client2);
        userService.addUser(livreur1);
        userService.addUser(livreur2);
    }

    /**
     * Initializes demo restaurants and dishes and adds them to the restaurant service.
     * Creates sample restaurants with menus containing various dishes for testing.
     */
    private void initializeRestaurant() {
        // Create demo restaurants and dishes
            Restaurant pizzeriaNapoli = new Restaurant(
                    "Pizzeria",
                    "Pizzeria Napoli",
                    "12 Rue de la Pizza"
            );

            Dish pizzaMargherita = new Dish(
                    "Pizza classique",
                    "Margherita",
                    "Pizza classique à la tomate mozzarella",
                    12.50,
                    pizzeriaNapoli
            );

            Dish pizzaQuattroFormaggi = new Dish(
                    "Pizza classique",
                    "Quatro fromagi",
                    "Pizza aux quatre fromages",
                    14.50,
                    pizzeriaNapoli
            );

            Restaurant burgerAmericain = new Restaurant(
                    "Burger",
                    "A l'Americaine",
                    "57 Avenue des Burgers"
            );

            Dish CheeseBurger = new Dish(
                    "Classique",
                    "cheeseBurger",
                    "Burger avec steak et fromages accompagné de frites",
                    11.00,
                    burgerAmericain
            );

            Dish CheeseBacon = new Dish(
                    "Classique",
                    "cheeseBacon",
                    "Burger avec steak, fromage et bacon accommpagné de frites",
                    12.00,
                    burgerAmericain
            );


            pizzeriaNapoli.addDish(pizzaMargherita);
            pizzeriaNapoli.addDish(pizzaQuattroFormaggi);

            restaurantService.addRestaurant(pizzeriaNapoli);

            burgerAmericain.addDish(CheeseBurger);
            burgerAmericain.addDish(CheeseBacon);

            restaurantService.addRestaurant(burgerAmericain);
        }

    /**
     * Launches the graphical user interface with all initialized services.
     * This method is called after all other initialization steps are complete.
     */
    public void launchInterface() {
        GraphicalInterface userInterface = new GraphicalInterface(
                userService, restaurantService, orderService, paymentService, deliveryService);
        userInterface.setVisible(true);
    }
}



