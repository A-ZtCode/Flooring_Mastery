package view;

import modelDTO.Order;
import modelDTO.Product;
import modelDTO.Tax;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * MenuView class handles the user interface operations of the flooring program.
 * This class provides methods to display menu options, orders, products, taxes, and errors to the user.
 * It also contains methods to capture user input for various data types.
 */
public class MenuView {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the main menu options to the user.
     */
    public void displayMenu() {
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("* <<Flooring Program>>");
        System.out.println("* 1. Display Orders");
        System.out.println("* 2. Add an Order");
        System.out.println("* 3. Edit an Order");
        System.out.println("* 4. Remove an Order");
        System.out.println("* 5. Export All Data");
        System.out.println("* 6. Quit");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    /**
     * Displays a list of orders.
     * @param orders The list of orders to be displayed.
     */
    public void displayOrders(List<Order> orders) {
        System.out.println("Orders:");
        for (Order order : orders) {
            System.out.println(orderToString(order));
        }
    }

    /**
     * Displays a list of available products.
     * @param availableProducts The list of available products.
     */
    public void displayAvailableProducts(List<Product> availableProducts) {
        System.out.println("Available Products:");
        for (Product product : availableProducts) {
            System.out.println(product.getProductType() + " - Cost per sq ft: " + product.getCostPerSquareFoot() + " - Labor cost per sq ft: " + product.getLaborCostPerSquareFoot());
        }
    }

    /**
     * Displays a list of available states and their tax rates.
     * @param availableTaxes The list of available taxes by state.
     */
    public void displayAvailableStates(List<Tax> availableTaxes) {
        System.out.println("Available States:");
        for (Tax tax : availableTaxes) {
            System.out.println(tax.getStateAbbreviation());
        }
    }

    /**
     * Displays a summary of an order.
     * @param order The order whose details need to be displayed.
     */
    public void displayOrderSummary(Order order) {
        System.out.println("Order Summary:");
        System.out.println(orderToString(order));
    }

    /**
     * Displays an error message.
     * @param errorMessage The error message to be displayed.
     */
    public void displayErrorMessage(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }

    // Format an order for display
    private String orderToString(Order order) {
        return String.format("Order #%d - Customer: %s, State: %s, Total: $%.2f",
                order.getOrderNumber(), order.getCustomerName(), order.getState(), order.getTotal());
    }

    // Format a product for display
    private String productToString(Product product) {
        return String.format("Product: %s, Cost/SqFt: $%.2f, Labor Cost/SqFt: $%.2f",
                product.getProductType(), product.getCostPerSquareFoot(), product.getLaborCostPerSquareFoot());
    }

    // Format a tax for display
    private String taxToString(Tax tax) {
        return String.format("State: %s, Tax Rate: %s%%", tax.getStateName(), tax.getTaxRate());
    }

    /**
     * Captures user input for a decimal number.
     * @param prompt The message or question to be displayed to the user.
     * @return The captured BigDecimal input from the user.
     */
    public BigDecimal getUserInputDecimal(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        return new BigDecimal(input);
    }

    /**
     * Captures user input as a string.
     * @param prompt The message or question to be displayed to the user.
     * @return The captured string input from the user.
     */
    public String getUserInputString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Captures user input for an integer.
     * @param prompt The message or question to be displayed to the user.
     * @return The captured integer input from the user.
     */
    public int getUserInputInt(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        return Integer.parseInt(input);
    }

    /**
     * Displays a generic message to the user.
     * @param s The message to be displayed.
     */
    public void displayMessage(String s) {
        System.out.println(s);
    }
}
