package controller;

import modelDTO.Order;
import modelDTO.Tax;
import service.OrderService;
import service.ProductService;
import service.TaxService;
import view.MenuView;
import service.ServiceException;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FlooringMasteryController {

    private final MenuView menuView;
    private OrderService orderService = null;
    private ProductService productService = null;
    private TaxService taxService = null;

    private Date convertLocalDateToDate(LocalDate localDate) {
        ZonedDateTime zdt = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
    private static final String EXPORT_FILE_PATH = "controller/Backup/DataExport.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public FlooringMasteryController(MenuView menuView, OrderService orderService, ProductService productService, TaxService taxService) {
        this.menuView = menuView;
        this.orderService = orderService;
        this.productService = productService;
        this.taxService = taxService;
    }

    private LocalDate promptForDate() {
        String dateStr = menuView.getUserInputString("Please enter the date (MM-dd-yyyy): ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    private int promptForOrderId() {
        return menuView.getUserInputInt("Please enter the order ID: ");
    }

    private Order gatherOrderData() {
        String customerName = menuView.getUserInputString("Enter customer name: ");
        String state = menuView.getUserInputString("Enter state: ");
        BigDecimal taxRate = menuView.getUserInputDecimal("Enter tax rate: ");
        String productType = menuView.getUserInputString("Enter product type: ");
        BigDecimal area = menuView.getUserInputDecimal("Enter area: ");
        BigDecimal costPerSquareFoot = menuView.getUserInputDecimal("Enter cost per square foot: ");
        BigDecimal laborCostPerSquareFoot = menuView.getUserInputDecimal("Enter labor cost per square foot: ");
        BigDecimal materialCost = area.multiply(costPerSquareFoot); // Calculated using Order's method
        BigDecimal laborCost = area.multiply(laborCostPerSquareFoot); // Calculated using Order's method
        // Fetching the Tax object for the provided state to calculate tax and total
        Tax taxObject = taxService.getTaxByState(state);
        if (taxObject == null) {
            menuView.displayErrorMessage("Invalid state provided. Tax details not found.");
            return null; // Return null or handle appropriately
        }
        BigDecimal tax = materialCost.add(laborCost).multiply(taxObject.getTaxRate().divide(new BigDecimal("100")));
        BigDecimal total = materialCost.add(laborCost).add(tax);
        Date orderDate = new Date(); // Current date.

        // Assuming an auto-incremented ID or some other method to generate a unique ID for the order
        // For now, setting it to null. You might want to generate it appropriately
        Integer orderId = null;

        return new Order(orderId, customerName, state, taxRate, productType, area, costPerSquareFoot, laborCostPerSquareFoot, materialCost, laborCost, tax, total, orderDate);
    }


    public void run() {
        boolean quit = false;

        while (!quit) {
            menuView.displayMenu();
            int choice = menuView.getUserInputInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1: // Display Orders
                        LocalDate localDateToDisplay = promptForDate();
                        Date dateToDisplay = convertLocalDateToDate(localDateToDisplay);
                        List<Order> orders = orderService.getOrdersByDate(dateToDisplay);
                        menuView.displayOrders(orders);
                        break;
                    case 2: // Add an Order
                        Order orderToAdd = gatherOrderData();
                        orderService.addOrder(orderToAdd);
                        menuView.displayOrderSummary(orderToAdd);
                        break;
                    case 3: // Edit an Order
                        int orderIdToEdit = promptForOrderId();
                        Order orderToEdit = orderService.getOrderById(orderIdToEdit);
                        if (orderToEdit == null) {
                            menuView.displayErrorMessage("Order not found!");
                            break;
                        }
                        Order updatedOrder = gatherOrderData();
                        updatedOrder.setOrderNumber(orderIdToEdit); // Ensure we keep the same order ID
                        orderService.editOrder(updatedOrder);
                        menuView.displayOrderSummary(updatedOrder);
                        break;
                    case 4: // Remove an Order
                        int orderIdToRemove = promptForOrderId();
                        orderService.removeOrder(orderIdToRemove);
                        menuView.displayErrorMessage("Order removed successfully.");
                        break;
                    case 5: // Export All Data
                        exportAllData();
                        break;
                    case 6: // Quit
                        quit = true;
                        break;
                    default:
                        menuView.displayErrorMessage("Invalid choice. Please try again.");
                }
            } catch (ServiceException e) {
                menuView.displayErrorMessage(e.getMessage());
            }
        }
        System.out.println("Exiting the program.");
    }

    // Exporting Data to File in Backup Folder
    private void exportAllData() {
        try {
            // Ensure the Backup directory exists
            Files.createDirectories(Paths.get("controller/Backup"));

            // Get all the orders
            List<Order> allOrders = orderService.getAllOrders();

            // Convert the orders to a string format
            StringBuilder dataToExport = new StringBuilder("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate\n");
            for (Order order : allOrders) {
                dataToExport.append(orderToString(order)).append("\n");
            }

            // Write the data to the file
            try (FileWriter writer = new FileWriter(EXPORT_FILE_PATH)) {
                writer.write(dataToExport.toString());
            }
            menuView.displayMessage("Data exported successfully to " + EXPORT_FILE_PATH);
        } catch (ServiceException | IOException e) {
            menuView.displayErrorMessage("Error exporting data: " + e.getMessage());
        }
    }

    private String orderToString(Order order) {
        return String.format("%d,%s,%s,%.2f,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%s",
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getState(),
                order.getTaxRate(),
                order.getProductType(),
                order.getArea(),
                order.getCostPerSquareFoot(),
                order.getLaborCostPerSquareFoot(),
                order.getMaterialCost(),
                order.getLaborCost(),
                order.getTax(),
                order.getTotal(),
                DATE_FORMATTER.format(order.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        );
    }
}
