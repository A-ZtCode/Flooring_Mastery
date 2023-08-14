package service;

import dao.OrderDao;
import dao.ProductDao;
import dao.TaxDao;
import modelDTO.Order;
import modelDTO.Product;
import modelDTO.Tax;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the concrete implementation of the OrderService interface.
 * It serves as the intermediary between the application's user interface and data access objects (DAOs).
 * The class is responsible for executing business logic, data validation, and other core functionality related to orders.
 */
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final TaxDao taxDao;

    // Map to convert state names to their respective abbreviations
    private Map<String, String> stateToAbbreviationMap;

    /**
     * Constructor initializes the DAOs and the state abbreviation map.
     */
    public OrderServiceImpl(OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
        initializeStateMapping(); // <-- Initialize the map in the constructor
    }

    /**
     * Initializes the state to abbreviation mapping.
     */
    private void initializeStateMapping() {
        stateToAbbreviationMap = new HashMap<>();
        stateToAbbreviationMap.put("Alabama", "AL");
        stateToAbbreviationMap.put("Alaska", "AK");
        stateToAbbreviationMap.put("Arizona", "AZ");
        stateToAbbreviationMap.put("Arkansas", "AR");
        stateToAbbreviationMap.put("California", "CA");
        stateToAbbreviationMap.put("Colorado", "CO");
        stateToAbbreviationMap.put("Connecticut", "CT");
        stateToAbbreviationMap.put("Delaware", "DE");
        stateToAbbreviationMap.put("Florida", "FL");
        stateToAbbreviationMap.put("Georgia", "GA");
        stateToAbbreviationMap.put("Hawaii", "HI");
        stateToAbbreviationMap.put("Idaho", "ID");
        stateToAbbreviationMap.put("Illinois", "IL");
        stateToAbbreviationMap.put("Indiana", "IN");
        stateToAbbreviationMap.put("Iowa", "IA");
        stateToAbbreviationMap.put("Kansas", "KS");
        stateToAbbreviationMap.put("Kentucky", "KY");
        stateToAbbreviationMap.put("Louisiana", "LA");
        stateToAbbreviationMap.put("Maine", "ME");
        stateToAbbreviationMap.put("Maryland", "MD");
        stateToAbbreviationMap.put("Massachusetts", "MA");
        stateToAbbreviationMap.put("Michigan", "MI");
        stateToAbbreviationMap.put("Minnesota", "MN");
        stateToAbbreviationMap.put("Mississippi", "MS");
        stateToAbbreviationMap.put("Missouri", "MO");
        stateToAbbreviationMap.put("Texas", "TX");
    }

    /**
     * Adds a new order to the data store.
     * This method ensures that the order data is valid, calculates relevant costs, and persists the order.
     * @param order The order object to be added.
     * @throws ServiceException if the order is null, state is invalid, or product type is invalid.
     */
    @Override
    public void addOrder(Order order) {
        if (order == null) {
            throw new ServiceException("Order object is null!");
        }
//        String stateAbbreviation = getStateAbbreviation(order.getState());
//        if (stateAbbreviation == null) {
//            throw new ServiceException("Invalid state name provided!");
//        }
//        order.setState(stateAbbreviation); // Set the order's state to the abbreviation
        validateProductType(order.getProductType());
        validateState(order.getState());
        calculateOrderCosts(order);
        orderDao.addOrder(order);
    }

    /**
     * Edits an existing order in the data store.
     * This method ensures that the order exists, validates the order data, calculates relevant costs, and updates the order.
     * @param order The order object with updated details to be saved.
     * @throws ServiceException if the order does not exist, state is invalid, or product type is invalid.
     */
    @Override
    public void editOrder(Order order) {
        try {
            Order existingOrder = orderDao.getOrderById(order.getOrderNumber());
            if (existingOrder == null) {
                throw new ServiceException("Order not found!");
            }
            validateProductType(order.getProductType());
            validateState(order.getState());
            calculateOrderCosts(order);
            orderDao.editOrder(order);
        } catch (OrderNotFoundException e) {
            throw new ServiceException("Failed to edit the order.", e);
        }
    }

    /**
     * Removes an order from the data store based on its ID.
     * This method ensures that the order exists before attempting to remove it.
     * @param orderId The ID of the order to be removed.
     * @throws ServiceException if the order does not exist.
     */
    @Override
    public void removeOrder(int orderId) {
        Order existingOrder = orderDao.getOrderById(orderId);
        if (existingOrder == null) {
            throw new ServiceException("Order not found!");
        }
        orderDao.removeOrder(orderId);
    }

    /**
     * Retrieves a list of orders from the data store based on a specific date.
     * @param date The date for which orders need to be retrieved.
     * @return List of orders corresponding to the provided date.
     */
    @Override
    public List<Order> getOrdersByDate(Date date) {
        return orderDao.getOrdersByDate(date);
    }

    /**
     * Retrieves a specific order from the data store based on its ID.
     * @param orderId The ID of the order to be retrieved.
     * @return The order corresponding to the provided ID, or null if not found.
     */
    @Override
    public Order getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }

    /**
     * Retrieves a list of all orders from the data store.
     * Also, logs the total number of orders retrieved.
     * @return List of all orders.
     */
    @Override
    public List<Order> getAllOrders() {
        List<Order> allOrders = orderDao.getAllOrders();
        System.out.println("Number of orders to export: " + allOrders.size());
        return allOrders;
    }

    /**
     * Validates the data for a given order.
     * This method ensures that the order's customer name, product type, and state are valid and not empty.
     * @param order The order object to be validated.
     * @return true if the order data is valid, false otherwise.
     * @throws ServiceException if any of the order's data is invalid.
     */
    @Override
    public boolean validateOrderData(Order order) {
        if (order == null) {
            throw new ServiceException("Order cannot be null!");
        }
        if (order.getCustomerName() == null || order.getCustomerName().isEmpty()) {
            throw new ServiceException("Customer name cannot be empty!");
        }
        if (order.getProductType() == null || order.getProductType().isEmpty()) {
            throw new ServiceException("Product type cannot be empty!");
        }
        if (order.getState() == null || order.getState().isEmpty()) {
            throw new ServiceException("State cannot be empty!");
        }
        return true;
    }

    /**
     * Validates the product type by checking if it exists in the data store.
     */
    private void validateProductType(String productType) {
        if (productDao.getProductByType(productType) == null) {
            throw new ServiceException("Invalid product type!");
        }
    }

    /**
     * Validates the state by checking if tax details for it exist in the data store.
     */
    private void validateState(String state) {
        if (taxDao.getTaxByState(state) == null) {
            throw new ServiceException("Invalid state provided. Tax details not found.");
        }
    }
    /**
     * Calculates and sets the various costs for a given order.
     */
    private void calculateOrderCosts(Order order) {
        Product product = productDao.getProductByType(order.getProductType());
        Tax tax = taxDao.getTaxByState(order.getState());

        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
        order.setMaterialCost(order.calculateMaterialCost());
        order.setLaborCost(order.calculateLaborCost());
        order.setTax(order.calculateTax(tax));
        order.setTotal(order.calculateTotal(tax));
    }


    /**
     * Calculates the tax amount for a given order based on its state.
     * @param order The order object for which the tax needs to be calculated.
     * @return The tax amount for the order.
     * @throws ServiceException if the state provided in the order is invalid.
     */
    @Override
    public BigDecimal calculateTaxForOrder(Order order) {
        // Get the tax details based on the order's state
        Tax tax = taxDao.getTaxByState(order.getState());
        if (tax == null) {
            throw new ServiceException("Invalid state for tax calculation!");
        }
        // Calculate and return the tax amount for the order
        return order.calculateTax(tax);
    }

    /**
     * Calculates the total cost for a given order based on its product type.
     * @param order The order object for which the cost needs to be calculated.
     * @return The total cost for the order based on product type.
     * @throws ServiceException if the product type provided in the order is invalid.
     */
    @Override
    public BigDecimal calculateCostForProductType(Order order) {
        // Get the product details based on the order's product type
        Product product = productDao.getProductByType(order.getProductType());
        if (product == null) {
            throw new ServiceException("Invalid product type for cost calculation!");
        }
        // Calculate and return the total cost based on product type
        return order.getArea().multiply(product.getCostPerSquareFoot());
    }

    /**
     * Validates a string input, throwing an exception with the provided error message if invalid.
     */
    private void validateStringInput(String input, String errorMessage) {
        if (input == null || input.trim().isEmpty()) {
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * Searches and retrieves a list of orders from the data store based on a specific customer's name.
     * @param customerName The name of the customer to search for.
     * @return List of orders corresponding to the provided customer's name.
     * @throws ServiceException if the customer name is empty or null.
     */
    @Override
    public List<Order> searchOrdersByName(String customerName) {
        validateStringInput(customerName, "Customer name cannot be empty!");
        return orderDao.searchOrdersByName(customerName);
    }

    /**
     * Searches and retrieves a list of orders from the data store based on a specific state.
     * @param state The state to search for.
     * @return List of orders corresponding to the provided state.
     * @throws ServiceException if the state is empty or null.
     */
    @Override
    public List<Order> searchOrdersByState(String state) {
        validateStringInput(state, "State cannot be empty!");
        return orderDao.searchOrdersByState(state);
    }

    /**
     * Searches and retrieves a list of orders from the data store based on a specific product type.
     * @param productType The product type to search for.
     * @return List of orders corresponding to the provided product type.
     * @throws ServiceException if the product type is empty or null.
     */
    @Override
    public List<Order> searchOrdersByProductType(String productType) {
        validateStringInput(productType, "Product type cannot be empty!");
        return orderDao.searchOrdersByProductType(productType);
    }
}