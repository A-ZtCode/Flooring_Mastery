package dao;

import modelDTO.Order;
import service.OrderNotFoundException;

import java.util.Date;
import java.util.List;

public abstract class OrderDao {
    public abstract Order addOrder(Order order);
    public abstract void editOrder(Order order) throws OrderNotFoundException;
    public abstract void removeOrder(int orderId);
    public abstract List<Order> getOrdersByDate(Date date);
    public abstract Order getOrderById(int orderId);
    public abstract List<Order> getAllOrders();
    public abstract List<Order> searchOrdersByName(String customerName);
    public abstract List<Order> searchOrdersByProductType(String productType);
    public abstract List<Order> searchOrdersByState(String state);
}
