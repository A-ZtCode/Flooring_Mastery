package controller;

import view.MenuView;

public class FlooringMasteryController {

    private final MenuView menuView;

    public FlooringMasteryController(MenuView menuView) {
        this.menuView = menuView;
    }

    public void run() {
        boolean quit = false;

        while (!quit) {
            menuView.displayMenu(); // Display the main menu
            int choice = menuView.getUserInputInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    // Handle Display Orders
                    break;
                case 2:
                    // Handle Add an Order
                    break;
                case 3:
                    // Handle Edit an Order
                    break;
                case 4:
                    // Handle Remove an Order
                    break;
                case 5:
                    // Handle Export All Data
                    break;
                case 6:
                    quit = true; // Quit the program
                    break;
                default:
                    menuView.displayErrorMessage("Invalid choice. Please try again.");
            }
        }

        System.out.println("Exiting the program.");
    }
}
