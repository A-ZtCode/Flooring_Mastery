package org.example;

import controller.FlooringMasteryController;
import view.MenuView;

public class FlooringMasteryMainApp {
    public static void main(String[] args) {
        MenuView menuView = new MenuView();
        FlooringMasteryController controller = new FlooringMasteryController(menuView);
        controller.run();
    }
}