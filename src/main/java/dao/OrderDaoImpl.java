package dao;

import modelDTO.Order;
import service.OrderNotFoundException;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderDaoImpl extends OrderDao {

    private final Map<Integer, Order> orders = new HashMap<>();
    private static final String BASE_PATH = "path_to_your_project_directory/OrdersFiles/";

    private String getFilePathForDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        return BASE_PATH + "Orders_" + sdf.format(date) + ".txt";
    }

    private String getBackupFilePathForDate(Date date) {
        return getFilePathForDate(date) + ".bak";
    }


    public OrderDaoImpl() {
        File folder = new File(BASE_PATH);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().startsWith("Orders_") && file.getName().endsWith(".txt")) {
                    loadOrdersFromFile(parseDateFromFileName(file.getName())).forEach(order -> orders.put(order.getOrderNumber(), order));
                }
            }
        }
    }


    @Override
    public Order addOrder(Order order) {
        int nextOrderId = getNextOrderId();
        order.setOrderNumber(nextOrderId);
        orders.put(nextOrderId, order);// Add order to in-memory storage
        saveOrdersToFile(order.getOrderDate());   // Save the updated orders back to the file
        return order;
    }
    @Override
    public void editOrder(Order order) throws OrderNotFoundException {
        if (orders.containsKey(order.getOrderNumber())) {
            orders.put(order.getOrderNumber(), order);  // Update order in in-memory storage
            saveOrdersToFile(order.getOrderDate()); // Save the updated orders back to the file
        } else {
            // Log an error
            System.err.println("Error: Order with ID " + order.getOrderNumber() + " not found!");
            // Throw a custom exception
            throw new OrderNotFoundException("Order with ID " + order.getOrderNumber() + " does not exist!");

        }
    }
    @Override
    public void removeOrder(int orderId) {
        Order order = orders.remove(orderId);
        if (order != null) {
            saveOrdersToFile(order.getOrderDate());  // Save the updated orders back to the file
        }
    }


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
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
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
    private List<Order> loadOrdersFromFile(Date date) {
        List<Order> fileOrders = new ArrayList<>();
        String filePath = getFilePathForDate(date);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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
        // Placeholder implementation, you need to replace with actual parsing logic
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            return sdf.parse(dateString);
        } catch (Exception e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }

        // Helper method to save orders to the file
        private void saveOrdersToFile(Date date) {
            String filePath = getFilePathForDate(date);
            backupFile(date);

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
                restoreBackup(date);  // if there's an error, restore from backup
            }
        }

    // Backup the current data file
    private void backupFile(Date date) {
        File sourceFile = new File(getFilePathForDate(date));
        File backupFile = new File(getBackupFilePathForDate(date));
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
        private void restoreBackup(Date date) {
            File backupFile = new File(getBackupFilePathForDate(date));
            File sourceFile = new File(getFilePathForDate(date));
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
    }