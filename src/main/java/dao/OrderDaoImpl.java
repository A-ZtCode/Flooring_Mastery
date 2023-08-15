package dao;

import modelDTO.Order;
import service.OrderNotFoundException;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class provides an implementation for the data access operations for Orders.
 * It uses an in-memory map and file storage to manage orders.
 */
public class OrderDaoImpl extends OrderDao {

    private final Map<Integer, Order> orders = new HashMap<>();
    private static final String BASE_PATH = "src/main/java/OrdersFiles/";


    /**
     * Returns the file path for a given date.
     *
     * @param date The date to get the file path for.
     * @return The file path.
     */
    private String getFilePathForDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        return BASE_PATH + "Orders_" + sdf.format(date) + ".txt";
    }

    /**
     * Returns the backup file path for a given date.
     *
     * @param date The date to get the backup file path for.
     * @return The backup file path.
     */
//    private String getBackupFilePathForDate(Date date) {
//        return getFilePathForDate(date) + ".bak";
//    }

    /**
     * Constructs an instance and initializes it by loading all orders from the files.
     */
    public OrderDaoImpl() {
        File folder = new File(BASE_PATH);
        if (!folder.exists()){
            boolean dirCreated = folder.mkdirs();
            if (dirCreated) {
                System.out.println("Directory created successfully.");
            } else {
                System.err.println("Failed to create directory.");
            }
        }
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().startsWith("Orders_") && file.getName().endsWith(".txt")) {
                    loadOrdersFromFile(parseDateFromFileName(file.getName())).forEach(order -> orders.put(order.getOrderNumber(), order));
                }
            }
        }
    }

    /**
     * Adds a new order to the in-memory storage and saves to the file.
     *
     * @param order The order to be added.
     * @return The added order with its assigned order number.
     */
    @Override
    public Order addOrder(Order order) {
        int nextOrderId = getNextOrderId();
        order.setOrderNumber(nextOrderId);
        orders.put(nextOrderId, order);// Add order to in-memory storage
        saveOrdersToFile(order.getOrderDate());   // Save the updated orders back to the file
        return order;
    }

    /**
     * Edits an existing order in the in-memory storage and saves the updated order to the file.
     *
     * @param order The updated order.
     * @throws OrderNotFoundException If the order to be edited does not exist.
     */
    @Override
    public void editOrder(Order order) throws OrderNotFoundException {
        if (orders.containsKey(order.getOrderNumber())) {
            orders.put(order.getOrderNumber(), order);  // Update order in in-memory storage
            saveOrdersToFile(order.getOrderDate()); // Save the updated orders back to the file
        } else {
              // Throw a custom exception
            throw new OrderNotFoundException("Order with ID " + order.getOrderNumber() + " does not exist!");

        }
    }

    /**
     * Removes an order from the in-memory storage and updates the file.
     *
     * @param orderId The ID of the order to be removed.
     */
    @Override
    public void removeOrder(int orderId) {
        Order order = orders.remove(orderId);
        if (order != null) {
            saveOrdersToFile(order.getOrderDate());  // Save the updated orders back to the file
        }
    }

    /**
     * Retrieves orders by a specific date.
     *
     * @param date The date to retrieve orders for.
     * @return A list of orders for the specified date.
     */
    public List<Order> getOrdersByDate(Date date) {
        List<Order> ordersByDate = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getOrderDate().equals(date)) {
                ordersByDate.add(order);
            }
        }
        return ordersByDate;
    }

    /**
     * Retrieves an order by its order ID.
     *
     * @param orderId The ID of the order to retrieve.
     * @return The order if found, otherwise null.
     */
    @Override
    public Order getOrderById(int orderId) {
        return orders.get(orderId);
    }

    /**
     * Searches orders by a customer's name.
     *
     * @param customerName The customer's name to search by.
     * @return A list of orders that match the customer's name.
     */
    @Override
    public List<Order> searchOrdersByName(String customerName) {
        List<Order> matchingOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getCustomerName().equalsIgnoreCase(customerName)) {
                matchingOrders.add(order);
            }
        }
        return matchingOrders;
    }

    /**
     * Retrieves all orders from the in-memory storage.
     *
     * @return A list of all orders.
     */
    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    /**
     * Searches orders by product type.
     * @param  productType The product type to search by.
     * @return A list of orders that match the product type.
     */
    @Override
    public List<Order> searchOrdersByProductType(String productType) {
        List<Order> matchingOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getProductType().equalsIgnoreCase(productType)) {
                matchingOrders.add(order);
            }
        }
        return matchingOrders;
    }

    /**
     * Searches orders by state.
     *
     * @param state The state to search by.
     * @return A list of orders that match the state.
     */
    @Override
    public List<Order> searchOrdersByState(String state) {
        List<Order> matchingOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getState().equalsIgnoreCase(state)) {
                matchingOrders.add(order);
            }
        }
        return matchingOrders;
    }
    /**
     * Generates the next order ID.
     *
     * @return The next order ID.
     */
    private int getNextOrderId() {
        // Find the maximum order ID and increment by 1 for the next order
        return orders.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Loads orders from a file for a specific date.
     *
     * @param date The date to load orders for.
     * @return A list of orders loaded from the file.
     */
    private List<Order> loadOrdersFromFile(Date date) {
        List<Order> fileOrders = new ArrayList<>();
        String filePath = getFilePathForDate(date);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();  // Skip the header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 13) {
                    int orderId = Integer.parseInt(parts[0].trim());
                    String customerName = parts[1].trim();
                    String state = parts[2].trim();
                    BigDecimal taxRate = new BigDecimal(parts[3].trim());
                    String productType = parts[4].trim();
                    BigDecimal area = new BigDecimal(parts[5].trim());
                    BigDecimal costPerSquareFoot = new BigDecimal(parts[6].trim());
                    BigDecimal laborCostPerSquareFoot = new BigDecimal(parts[7].trim());
                    BigDecimal materialCost = new BigDecimal(parts[8].trim());
                    BigDecimal laborCost = new BigDecimal(parts[9].trim());
                    BigDecimal tax = new BigDecimal(parts[10].trim());
                    BigDecimal total = new BigDecimal(parts[11].trim());
                    Date orderDate = parseDate(parts[12].trim());
                    fileOrders.add(new Order(orderId, customerName, state, taxRate, productType, area,
                            costPerSquareFoot, laborCostPerSquareFoot, materialCost, laborCost, tax, total, orderDate));
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading orders from file: " + ex.getMessage());
        }
        return fileOrders;
    }

    /**
     * Parses a date from a string.
     *
     * @param dateString The string representation of the date.
     * @return The parsed date.
     */
    private Date parseDate(String dateString) {
        // Implement date parsing logic
        // Placeholder implementation, you need to replace with actual parsing logic
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            return sdf.parse(dateString);
        } catch (Exception e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }

    /**
     * Saves all orders to a file for a specific date.
     *
     * @param date The date for which to save orders.
     */
        private void saveOrdersToFile(Date date) {
            String filePath = getFilePathForDate(date);

//            backupFile(date);

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

            try (FileWriter writer = new FileWriter(filePath, false)) {  // false means do not append, overwrite the file
                writer.write("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate\n"); // updated header with OrderDate
                for (Order order : orders.values()) {
                    writer.write(String.join(",",
                            order.getOrderNumber().toString(),
                            order.getCustomerName(),
                            order.getState(),
                            order.getTaxRate().toString(),
                            order.getProductType(),
                            order.getArea().toString(),
                            order.getCostPerSquareFoot().toString(),
                            order.getLaborCostPerSquareFoot().toString(),
                            order.getMaterialCost().toString(),
                            order.getLaborCost().toString(),
                            order.getTax().toString(),
                            order.getTotal().toString(),
                            sdf.format(order.getOrderDate())
                    ) + "\n");
                }
            } catch (IOException ex) {
                System.err.println("Error writing orders to file: " + ex.getMessage());
//                restoreBackup(date);  // if there's an error, restore from backup
            }
        }

//    /**
//     * Creates a backup of the data file for a specific date.
//     *
//     * @param date The date for which to create a backup.
//     */
//    private void backupFile(Date date) {
//        File sourceFile = new File(getFilePathForDate(date));
//
//        // Check if the source file exists. If not, create an empty one.
//        if (!sourceFile.exists()) {
//            try {
//                boolean created = sourceFile.createNewFile();
//                if (!created) {
//                    System.err.println("Failed to create new file: " + sourceFile.getAbsolutePath());
//                    return; // return if file creation fails
//                }
//            } catch (IOException ex) {
//                System.err.println("Error creating new file: " + sourceFile.getAbsolutePath());
//                ex.printStackTrace();
//                return;
//            }
//        }
//
//        File backupFile = new File(getBackupFilePathForDate(date));
//        try (FileInputStream fis = new FileInputStream(sourceFile);
//             FileOutputStream fos = new FileOutputStream(backupFile)) {
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = fis.read(buffer)) != -1) {
//                fos.write(buffer, 0, bytesRead);
//            }
//        } catch (IOException ex) {
//            System.err.println("Error creating backup: " + ex.getMessage());
//        }
//    }

    /**
     * Restores data from a backup for a specific date.
     *
     * @param date The date for which to restore data.
     */
//        private void restoreBackup(Date date) {
//            File backupFile = new File(getBackupFilePathForDate(date));
//            File sourceFile = new File(getFilePathForDate(date));
//            try (FileInputStream fis = new FileInputStream(backupFile);
//                 FileOutputStream fos = new FileOutputStream(sourceFile)) {
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = fis.read(buffer)) != -1) {
//                    fos.write(buffer, 0, bytesRead);
//                }
//            } catch (IOException ex) {
//                System.err.println("Error restoring backup: " + ex.getMessage());
//            }
//        }

    /**
     * Parses a date from a file name.
     *
     * @param fileName The name of the file.
     * @return The parsed date.
     */
        private Date parseDateFromFileName(String fileName) {
            try {
                String datePart = fileName.replace("Orders_", "").replace(".txt", "");
                SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
                return sdf.parse(datePart);
            } catch (Exception e) {
                System.err.println("Error parsing date from file name: " + e.getMessage());
                return null;
            }
        }

    /**
     * Specify Backup Folder Path
     */
    private static final String EXPORT_PATH = "src/main/java/Backup/DataExport.txt";


    /**
     * Exports all order data to a file.
     */
    public void exportAllData() {
        File exportFile = new File(EXPORT_PATH);
        File parentDir = exportFile.getParentFile();

        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(exportFile, false)) {
            writer.write("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate\n");

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            for (Order order : orders.values()) {
                writer.write(String.join(",",
                        order.getOrderNumber().toString(),
                        order.getCustomerName(),
                        order.getState(),
                        order.getTaxRate().toString(),
                        order.getProductType(),
                        order.getArea().toString(),
                        order.getCostPerSquareFoot().toString(),
                        order.getLaborCostPerSquareFoot().toString(),
                        order.getMaterialCost().toString(),
                        order.getLaborCost().toString(),
                        order.getTax().toString(),
                        order.getTotal().toString(),
                        sdf.format(order.getOrderDate())
                ) + "\n");
            }
        } catch (IOException ex) {
            System.err.println("Error exporting data: " + ex.getMessage());
        }
    }
}