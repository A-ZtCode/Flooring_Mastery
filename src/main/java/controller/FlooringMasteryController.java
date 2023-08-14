package controller;

import modelDTO.Order;
import modelDTO.Product;
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
    private final OrderService orderService;
    private final ProductService productService;
    private final TaxService taxService;

    private Date convertLocalDateToDate(LocalDate localDate) {
        ZonedDateTime zdt = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
    private static final String EXPORT_FILE_PATH = "Backup/DataExport.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public FlooringMasteryController(MenuView menuView, OrderService orderService, ProductService productService, TaxService taxService) {
        this.menuView = menuView;
        this.orderService = orderService;
        this.productService = productService;
        this.taxService = taxService;
    }

    private LocalDate promptForDate() {
        String dateStr = menuView.getUserInputString("Please enter the order date (MM-dd-yyyy): ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return LocalDate.parse(dateStr, formatter);
    }


    private int promptForOrderId() {
        return menuView.getUserInputInt("Please enter the order ID: ");
    }

    private Order gatherOrderData() {
        // Start with collecting the Order Date
        LocalDate orderDate;
        do {
            orderDate = promptForDate();
            if (orderDate.isBefore(LocalDate.now())) {
                menuView.displayErrorMessage("Order date must be in the future.");
            }
        } while (orderDate.isBefore(LocalDate.now()));
        // Validation for Customer Name
        String customerName;
        do {
            customerName = menuView.getUserInputString("Enter customer name: ");
            if (!customerName.matches("[a-zA-Z0-9., ]+")) {
                menuView.displayErrorMessage("Invalid customer name. Only alphanumeric characters, periods, and commas are allowed.");
            }
        } while (!customerName.matches("[a-zA-Z0-9., ]+"));

        // Get valid state and associated tax details
        Tax selectedTax = getValidTax();
        if (selectedTax == null) {
            return null;
        }
        String state = selectedTax.getStateAbbreviation();
        BigDecimal taxRate = selectedTax.getTaxRate();

        // Get valid product type and associated product details
        Product selectedProduct = getValidProduct();
        if (selectedProduct == null) {
            return null;
        }
        String productType = selectedProduct.getProductType();

        // Validation for Area
        BigDecimal area;
        do {
            area = menuView.getUserInputDecimal("Enter area: ");
            if (area.compareTo(new BigDecimal("100")) < 0) {
                menuView.displayErrorMessage("Minimum order size is 100 sq ft.");
            }
        } while (area.compareTo(new BigDecimal("100")) < 0);

        BigDecimal costPerSquareFoot = selectedProduct.getCostPerSquareFoot();
        BigDecimal laborCostPerSquareFoot = selectedProduct.getLaborCostPerSquareFoot();
        BigDecimal materialCost = area.multiply(costPerSquareFoot);
        BigDecimal laborCost = area.multiply(laborCostPerSquareFoot);
        BigDecimal tax = materialCost.add(laborCost).multiply(taxRate.divide(new BigDecimal("100")));
        BigDecimal total = materialCost.add(laborCost).add(tax);
        Integer orderId = null;

        return new Order(orderId, customerName, state, taxRate, productType, area, costPerSquareFoot, laborCostPerSquareFoot, materialCost, laborCost, tax, total, convertLocalDateToDate(orderDate));
    }

    private Tax getValidTax() {
        List<Tax> availableTaxes = taxService.getAllTaxes();
        if (availableTaxes.isEmpty()) {
            menuView.displayErrorMessage("No states available for order placement.");
            return null;
        }
        menuView.displayAvailableStates(availableTaxes);
        while (true) {
            String stateInput = menuView.getUserInputString("Enter state abbreviation from the list: ").toUpperCase();
            Tax selectedTax = availableTaxes.stream()
                    .filter(tax -> tax.getStateAbbreviation().equalsIgnoreCase(stateInput))
                    .findFirst()
                    .orElse(null);
            if (selectedTax != null) {
                return selectedTax;
            } else {
                menuView.displayErrorMessage("Invalid state. Please select from the list.");
            }
        }
    }

    private Product getValidProduct() {
        List<Product> availableProducts = productService.getAllProducts();
        if (availableProducts.isEmpty()) {
            menuView.displayErrorMessage("No products available for order placement.");
            return null;
        }
        menuView.displayAvailableProducts(availableProducts);
        while (true) {
            String productInput = menuView.getUserInputString("Enter product type from the list: ");
            Product selectedProduct = availableProducts.stream()
                    .filter(product -> product.getProductType().equalsIgnoreCase(productInput))
                    .findFirst()
                    .orElse(null);
            if (selectedProduct != null) {
                return selectedProduct;
            } else {
                menuView.displayErrorMessage("Invalid product type. Please select from the list.");
            }
        }
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

                        if(orders.isEmpty()) {
                            menuView.displayErrorMessage("No orders exist for the provided date.");
                        } else {
                            menuView.displayOrders(orders);
      }
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
                if (!quit) {
                    // Pause before displaying the menu again
                    menuView.getUserInputString("Press Enter to continue...");
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
            Files.createDirectories(Paths.get("Backup"));

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
    } //
}
