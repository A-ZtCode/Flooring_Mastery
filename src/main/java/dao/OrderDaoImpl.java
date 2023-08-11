package dao;

import modelDTO.Order;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class OrderDaoImpl implements OrderDao {

    private final Map<Integer, Order> orders = new HashMap<>(); // in-memory storage
    private static final String BASE_PATH = "path_to_your_OrderFiles_folder/"; // Path to the file that contains order data
    private final String BACKUP_PATH = BASE_PATH + ".bak"; // Backup

    public OrderDaoImpl() {
        // Load orders into the in-memory map during instantiation
        loadOrdersFromFile().forEach(order -> orders.put(order.getOrderById(), order));
    }

    @Override
    public Order addOrder(Order order) {
        int nextOrderId = getNextOrderId();
        order.setOrderId(nextOrderId);
        orders.put(nextOrderId, order); // Add order to in-memory storage
        saveOrdersToFile(); // Save the updated orders back to the file
        return order;
    }

    @Override
    public void updateOrder(Order order) {
        orders.put(order.getOrderId(), order); // Update order in in-memory storage
        saveOrdersToFile(); // Save the updated orders back to the file
    }

    @Override
    public void removeOrder(int orderId) {
        orders.remove(orderId);
        saveOrdersToFile(); // Save the updated orders back to the file
    }

    @Override
    public List<Order> getOrdersByDate(Date date) {
        List<Order> ordersByDate = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getOrderDate().equals(date)) {
                ordersByDate.add(order);
            }
        }
        return ordersByDate;
    }

    @Override
    public Order getOrderById(int orderId) {
        return orders.get(orderId);
    }

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

    private int getNextOrderId() {
        // Find the maximum order ID and increment by 1 for the next order
        return orders.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }

    // Helper method to load orders from the file to in-memory storage
    private List<Order> loadOrdersFromFile() {
        List<Order> fileOrders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
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

    // Helper method to parse a date from string (format: MM-DD-YYYY)
    private Date parseDate(String dateString) {
        // Implement date parsing logic
        return new Date(); // Placeholder
    }

        // Helper method to save orders to the file
        private void saveOrdersToFile(Date orderDate, List<Order> orders) {
            backupFile();
            String filePath = BASE_PATH + generateOrderFileName(orderDate);
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total\n"); // header
                for (Order order : orders) {
                            order.getOrderId(), order.getCustomerName(), order.getState(), order.getTaxRate(),
                            order.getProductType(), order.getArea(), order.getCostPerSquareFoot(),
                            order.getLaborCostPerSquareFoot(), order.getMaterialCost(), order.getLaborCost(),
                            order.getTax(), order.getTotal(), formatDate((Date) order.getOrderDate()));
                }
            } catch (IOException ex) {
                throw new DaoException("Error writing orders to file.", ex);
                restoreBackup();  // if there's an error, restore from backup
            }
        }

        // Backup the current data file
        private void backupFile() {
            File sourceFile = new File(FILE_PATH);
            File backupFile = new File(BACKUP_PATH);
            try (FileInputStream fis = new FileInputStream(sourceFile);
                 FileOutputStream fos = new FileOutputStream(backupFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            } catch (IOException ex) {
                System.err.println("Error creating backup: " + ex.getMessage());
            }
        }

        // Restore from the backup file
        private void restoreBackup() {
            File backupFile = new File(BACKUP_PATH);
            File sourceFile = new File(FILE_PATH);
            try (FileInputStream fis = new FileInputStream(backupFile);
                 FileOutputStream fos = new FileOutputStream(sourceFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            } catch (IOException ex) {
                System.err.println("Error restoring backup: " + ex.getMessage());
            }
        }

        // Helper method to format a date into string (format: MM-DD-YYYY)
        private String formatDate(Date date) {
            // Implement date formatting logic
            return "MM-DD-YYYY"; // Placeholder
        }
    }
}