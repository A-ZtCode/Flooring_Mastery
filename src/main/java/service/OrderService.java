package service;

import modelDTO.Order;
import modelDTO.Product;
import modelDTO.Tax;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * The OrderService interface defines the contract for order management operations.
 * This includes basic CRUD operations, search and filtering capabilities, and order-specific calculations.
 * Implementing classes should provide concrete implementations for each of these operations.
 */
public interface OrderService {

    /**
     * Adds a new order to the system.
     *
     * @param order The order object to be added.
     */
    void addOrder(Order order);

    /**
     * Edits an existing order in the system.
     *
     * @param order The order object containing updated information.
     */
    void editOrder(Order order);

    /**
     * Removes an order from the system based on its ID.
     *
     * @param orderId The ID of the order to be removed.
     */
    void removeOrder(int orderId);

    /**
     * Retrieves a list of orders placed on a specific date.
     *
     * @param date The date for which orders are to be retrieved.
     * @return A list of orders placed on the provided date.
     */
    List<Order> getOrdersByDate(Date date);

    /**
     * Retrieves a specific order by its ID.
     *
     * @param orderId The ID of the desired order.
     * @return The order matching the provided ID, or null if not found.
     */
    Order getOrderById(int orderId);

    /**
     * Retrieves a list of all orders in the system.
     *
     * @return A list of all orders.
     */
    List<Order> getAllOrders();

    /**
     * Validates the provided order data for correctness and completeness.
     *
     * @param order The order object to be validated.
     * @return true if the order data is valid, false otherwise.
     */
    boolean validateOrderData(Order order);

    /**
     * Searches for and retrieves orders based on the customer's name.
     *
     * @param customerName The name of the customer.
     * @return A list of orders associated with the provided customer name.
     */
    List<Order> searchOrdersByName(String customerName);

    /**
     * Searches for and retrieves orders based on the state.
     *
     * @param state The state to filter orders by.
     * @return A list of orders from the provided state.
     */
    List<Order> searchOrdersByState(String state);

    /**
     * Searches for and retrieves orders based on the product type.
     *
     * @param productType The product type to filter orders by.
     * @return A list of orders containing the provided product type.
     */
    List<Order> searchOrdersByProductType(String productType);

    /**
     * Calculates the tax amount for a given order.
     *
     * @param order The order for which tax needs to be calculated.
     * @return The calculated tax amount.
     */
    BigDecimal calculateTaxForOrder(Order order);

    /**
     * Calculates the total cost for the product type in a given order.
     *
     * @param order The order for which the product cost needs to be calculated.
     * @return The calculated product cost.
     */
    BigDecimal calculateCostForProductType(Order order);

}
