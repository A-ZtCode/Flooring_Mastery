package view;

import modelDTO.Order;
import modelDTO.Product;
import modelDTO.Tax;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class MenuView {

    private final Scanner scanner = new Scanner(System.in);

    // Display the main menu options
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

    // Display a list of orders
    public void displayOrders(List<Order> orders) {
        System.out.println("Orders:");
        for (Order order : orders) {
            System.out.println(orderToString(order));
        }
    }

    // Display a list of available products
    public void displayAvailableProducts(List<Product> availableProducts) {
        System.out.println("Available Products:");
        for (Product product : availableProducts) {
            System.out.println(product.getProductType() + " - Cost per sq ft: " + product.getCostPerSquareFoot() + " - Labor cost per sq ft: " + product.getLaborCostPerSquareFoot());
        }
    }

    // Display a list of available taxes
    public void displayAvailableStates(List<Tax> availableTaxes) {
        System.out.println("Available States:");
        for (Tax tax : availableTaxes) {
            System.out.println(tax.getStateAbbreviation());
        }
    }

    // Display an order summary
    public void displayOrderSummary(Order order) {
        System.out.println("Order Summary:");
        System.out.println(orderToString(order));
    }

    // Display an error message
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

    // Get user input for a decimal number
    public BigDecimal getUserInputDecimal(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        return new BigDecimal(input);
    }

    // Get user input for a string
    public String getUserInputString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Get user input for an integer
    public int getUserInputInt(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        return Integer.parseInt(input);
    }

    public void displayMessage(String s) {
        System.out.println(s);
    }
}
