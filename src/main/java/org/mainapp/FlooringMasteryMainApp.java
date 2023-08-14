package org.mainapp;

import controller.FlooringMasteryController;
import dao.OrderDaoImpl;
import dao.ProductDaoImpl;
import dao.TaxDaoImpl;
import service.OrderServiceImpl;
import service.ProductServiceImpl;
import service.TaxServiceImpl;
import view.MenuView;

/**
 * The FlooringMasteryMainApp class serves as the entry point for the Flooring Mastery application.
 * It initializes the necessary DAOs, services, and views, and then runs the main application loop
 * by invoking the controller.
 *
 * The main responsibilities of this class include:
 * - Instantiating the necessary implementations for data access (DAOs).
 * - Instantiating the necessary service layer implementations.
 * - Instantiating the view components.
 * - Providing the required dependencies to the main controller.
 * - Starting the application by invoking the controller's run method.
 */
public class FlooringMasteryMainApp {

    /**
     * The main method, which serves as the entry point for the Flooring Mastery application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Create DAO instances
        OrderDaoImpl orderDao = new OrderDaoImpl();
        ProductDaoImpl productDao = new ProductDaoImpl();
        TaxDaoImpl taxDao = new TaxDaoImpl();

        // Create service instances
        OrderServiceImpl orderService = new OrderServiceImpl(orderDao, productDao, taxDao);
        ProductServiceImpl productService = new ProductServiceImpl(productDao);
        TaxServiceImpl taxService = new TaxServiceImpl(taxDao);

        // Create the MenuView
        MenuView menuView = new MenuView();

        // Pass all required dependencies to the FlooringMasteryController
        FlooringMasteryController controller = new FlooringMasteryController(menuView, orderService, productService, taxService);

        // Run the controller to start the application
        controller.run();
    }
}
