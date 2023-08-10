package service;

import modelDTO.Order;
import modelDTO.Product;
import modelDTO.Tax;

import java.util.Date;
import java.util.List;

public interface OrderService {

    // Basic CRUD
    void addOrder(Order order);
    void editOrder(Order order);
    void removeOrder(int orderId);
    List<Order> getOrdersByDate(Date date);

    // Additional functionality
    Order getOrderById(int orderId);
    List<Order> getAllOrders();
    boolean validateOrderData(Order order);

    // Searching and filtering
    List<Order> searchOrdersByName(String customerName);
    List<Order> searchOrdersByState(String state);
    List<Order> searchOrdersByProductType(String productType);
}




