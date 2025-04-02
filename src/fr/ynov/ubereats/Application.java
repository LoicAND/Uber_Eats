package fr.ynov.ubereats;
import fr.ynov.ubereats.configuration.Configuration;

/**
 * Main entry point for the Uber Eats application.
 * This class contains the main method that bootstraps the entire application.
 * It initializes the configuration and starts the application.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class Application {
    /**
     * Main method that launches the Uber Eats application.
     * Creates a Configuration instance and calls its initialize method
     * to set up all services, load demo data, and display the GUI.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.initialize();
    }
}
