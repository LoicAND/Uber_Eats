package fr.ynov.ubereats.configuration;

import fr.ynov.ubereats.domain.restaurant.Dish;
import fr.ynov.ubereats.domain.restaurant.Restaurant;
import fr.ynov.ubereats.domain.user.Customers;
import fr.ynov.ubereats.domain.user.Deliver;
import fr.ynov.ubereats.gui.GraphicalInterface;
import fr.ynov.ubereats.service.OrderService;
import fr.ynov.ubereats.service.PaymentService;
import fr.ynov.ubereats.service.RestaurantService;
import fr.ynov.ubereats.service.UserService;

public class Configuration {

    private UserService userService;
    private RestaurantService restaurantService;
    private OrderService orderService;
    private PaymentService paymentService;

    public void InitialConfiguration() {
        this.userService = new UserService();
        this.restaurantService = new RestaurantService();
        this.orderService = new OrderService();
        this.paymentService = new PaymentService();
    }

    public void initialize(){
        InitialConfiguration();
        initializeUser();
        initializeRestaurant();
        launchInterface();
    }

    private void initializeUser() {
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

    public void initializeRestaurant() {
            Restaurant pizzeriaNapoli = new Restaurant(
                    "Pizzeria ",
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
                    "Cheeseburger",
                    "Burger avec steak et fromages accompagné de frites",
                    11.00,
                    burgerAmericain
            );

            Dish CheeseBacon = new Dish(
                    "Classique",
                    "CheeseBacon",
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

    public void launchInterface() {
        GraphicalInterface userInterface = new GraphicalInterface(userService, restaurantService, orderService, paymentService);
        userInterface.setVisible(true);
    }
}



