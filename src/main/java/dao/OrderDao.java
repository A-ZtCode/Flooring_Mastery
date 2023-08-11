package dao;

import modelDTO.Order;

import java.util.Date;
import java.util.List;

public abstract class OrderDao {

    public Order addOrder(Order order) {
        // Code to add order
        return order;
    }

    public void editOrder(Order order) {
        // Code to edit order
    }

    public abstract void updateOrder(Order order);

    public void removeOrder(int orderId) {
        // Code to remove order by ID
    }

    public List<Order> getOrdersByDate(Date date) {
        // Code to retrieve orders by date
        return null;
    }

    public Order getOrderById(int orderId) {
        // Code to retrieve order by ID
        return null;
    }

    public List<Order> searchOrdersByName(String customerName) {
        return null;
    }

    public List<Order> searchOrdersByProductType(String productType) {
        return null;
    }

    public List<Order> searchOrdersByState(String state) {
        return null;
    }
}