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
     *
     * @param order The order to be added.
     * @return The added order.
     */
    public abstract Order addOrder(Order order);
    /**
     * Edits an existing order.
     *
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
    public abstract Order getOrderById(int orderId);
    public abstract List<Order> getAllOrders();
    public abstract List<Order> searchOrdersByName(String customerName);
    public abstract List<Order> searchOrdersByProductType(String productType);
    public abstract List<Order> searchOrdersByState(String state);
}
