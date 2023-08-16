package dao;

import modelDTO.Order;
import service.OrderNotFoundException;

import java.util.Date;
import java.util.List;

/**
 * This abstract class defines the data access operations for Orders.
 * Any class implementing this will need to provide concrete implementations
 * for all the methods defined here.
 */
public abstract class OrderDao {
    /**
     * Adds a new order.
     * @param order The order to be added.
     * @return The added order.
     */
    public abstract Order addOrder(Order order);

    /**
     * Edits an existing order.
     * @param order The order with updated details.
     * @throws OrderNotFoundException If the order to be edited does not exist.
     */
    public abstract void editOrder(Order order) throws OrderNotFoundException;

    /**
     * Removes an order by its ID.
     * @param orderId The ID of the order to be removed.
     */
    public abstract void removeOrder(int orderId);

    /**
     * Retrieves orders by a specific date.
     * @param date The date for which orders are to be fetched.
     * @return A list of orders for the specified date.
     */
    public abstract List<Order> getOrdersByDate(Date date);

    /**
     * Retrieves a specific order from the data source based on its unique ID number.
     * @param orderId The unique ID of the order to be retrieved.
     * @return The order that matches the specified ID.
     */
    public abstract Order getOrderById(int orderId);

    /**
     * Retrieves all orders from the data source.
     * @return A list of all orders.
     */
    public abstract List<Order> getAllOrders();

    /**
     * Searches and retrieves orders from the data source based on a customer's name.
     * @param customerName The name of the customer to search for.
     * @return A list of orders that match the specified customer name.
     */
    public abstract List<Order> searchOrdersByName(String customerName);

    /**
     * Searches and retrieves orders from the data source based on a product type.
     * @param productType The type of product to search for.
     * @return A list of orders that have the specified product type.
     */
    public abstract List<Order> searchOrdersByProductType(String productType);

    /**
     * Searches and retrieves orders from the data source based on a state.
     * @param state The state to search for.
     * @return A list of orders that are from the specified state.
     */
    public abstract List<Order> searchOrdersByState(String state);
}
