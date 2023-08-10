package service;

import modelDTO.Order;
import modelDTO.Product;
import modelDTO.Tax;

import java.util.Date;
import java.util.List;

public interface OrderService {
    void addOrder(Order order);
    void editOrder(Order order);
    void removeOrder(int orderId);
    List<Order> getOrdersByDate(Date date);
}




