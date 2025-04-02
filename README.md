# Uber_Eats

## Overview
This Java application simulates a food delivery service similar to Uber Eats. Users can browse restaurants, order meals, make payments, and track their deliveries through a comprehensive user interface.

## Features
- Restaurant and menu browsing by category (Pizza, Burger, Sushi, Chicken)
- Order creation and management system
- Multiple payment methods (Credit Card, PayPal, Meal Vouchers)
- Driver tipping option
- Delivery tracking and logistics
- User management for both customers and delivery personnel
- User-friendly GUI for easy navigation

## Project Structure
The application follows a service-oriented architecture:
- `fr.ynov.ubereats`: Application entry point
- `fr.ynov.ubereats.configuration`: Application setup and initialization
- `fr.ynov.ubereats.model`: Data classes (Restaurant, Dish, Order, User, etc.)
- `fr.ynov.ubereats.service`: Business services
  - `UserService`: Manages user accounts and authentication
  - `RestaurantService`: Handles restaurant and menu data
  - `OrderService`: Processes and tracks orders
  - `PaymentService`: Handles payment processing
  - `DeliveryService`: Coordinates delivery assignments and logistics
- `fr.ynov.ubereats.ui`: Graphical user interface with Swing

## Sample Data
The application is pre-loaded with demo data:
- **Restaurants**: Pizzeria Napoli, À l'Américaine (Burgers), Sushi Shop, Poulet Frit
- **Food Categories**: Pizza, Burger, Sushi, Chicken
- **Menu Items**: Various dishes for each restaurant with descriptions and prices
- **Users**: Sample customers (Jean Dupont, Marie Martin) and delivery personnel (Pierre Legrand, Louis Georges)

## Running the Application
1. Ensure you have Java installed (version 11 or higher).
2. Run the main class `fr.ynov.ubereats.ubereats.Application`.
3. The graphical interface will launch automatically after initialization.

## Implementation Details
- Service initialization happens in a predefined sequence
- Demo data is loaded during application startup
- The application uses Java Swing for the graphical interface
- Services follow dependency injection patterns for better maintainability

## Technologies Used
- Java 11+
- Java Swing for GUI development
- Object-oriented Programming principles
- Service-oriented architecture
- Dependency injection patterns
- MVC (Model-View-Controller) architecture


## Author
Loïc ANDRIANARIVONY
