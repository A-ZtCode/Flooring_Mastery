package org.example;

import controller.FlooringMasteryController;
import dao.OrderDaoImpl;
import dao.ProductDaoImpl;
import dao.TaxDaoImpl;
import service.OrderServiceImpl;
import service.ProductServiceImpl;
import service.TaxServiceImpl;
import view.MenuView;

public class FlooringMasteryMainApp {
    public static void main(String[] args) {
        // Create DAO instances (assuming you have these implementations)
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

        //Run the controller
        controller.run();
    }
}