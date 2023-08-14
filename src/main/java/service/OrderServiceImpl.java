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

public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final TaxDao taxDao;
    private Map<String, String> stateToAbbreviationMap;  // <-- Declare the map here

    // Constructor
    public OrderServiceImpl(OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
        initializeStateMapping(); // <-- Initialize the map in the constructor
    }

    // Helper method to populate the state map
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

    // Helper method to fetch abbreviation from state name
    private String getStateAbbreviation(String stateName) {
        return stateToAbbreviationMap.getOrDefault(stateName, null);
    }
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


    @Override
    public void removeOrder(int orderId) {
        Order existingOrder = orderDao.getOrderById(orderId);
        if (existingOrder == null) {
            throw new ServiceException("Order not found!");
        }
        orderDao.removeOrder(orderId);
    }

    @Override
    public List<Order> getOrdersByDate(Date date) {
        return orderDao.getOrdersByDate(date);
    }

    @Override
    public Order getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }
    @Override
    public List<Order> getAllOrders() {
        List<Order> allOrders = orderDao.getAllOrders();
        System.out.println("Number of orders to export: " + allOrders.size());
        return allOrders;
    }

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

    // Private helper methods for better modularity and readability
    private void validateProductType(String productType) {
        if (productDao.getProductByType(productType) == null) {
            throw new ServiceException("Invalid product type!");
        }
    }

    private void validateState(String state) {
        if (taxDao.getTaxByState(state) == null) {
            throw new ServiceException("Invalid state provided. Tax details not found.");
        }
    }

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


    private void validateStringInput(String input, String errorMessage) {
        if (input == null || input.trim().isEmpty()) {
            throw new ServiceException(errorMessage);
        }
    }
    @Override
    public List<Order> searchOrdersByName(String customerName) {
        validateStringInput(customerName, "Customer name cannot be empty!");
        return orderDao.searchOrdersByName(customerName);
    }

    @Override
    public List<Order> searchOrdersByState(String state) {
        validateStringInput(state, "State cannot be empty!");
        return orderDao.searchOrdersByState(state);
    }

    @Override
    public List<Order> searchOrdersByProductType(String productType) {
        validateStringInput(productType, "Product type cannot be empty!");
        return orderDao.searchOrdersByProductType(productType);
    }
}