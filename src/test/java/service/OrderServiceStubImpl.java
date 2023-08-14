package service;

import modelDTO.Order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderServiceStubImpl implements OrderService{


    @Override
    public void addOrder(Order order) {

    }

    @Override
    public void editOrder(Order order) {

    }

    @Override
    public void removeOrder(int orderId) {

    }

    @Override
    public List<Order> getOrdersByDate(Date date) {
        return null;
    }

    @Override
    public Order getOrderById(int orderId) {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return null;
    }

    @Override
    public boolean validateOrderData(Order order) {
        return false;
    }

    @Override
    public List<Order> searchOrdersByName(String customerName) {
        return null;
    }

    @Override
    public List<Order> searchOrdersByState(String state) {
        return null;
    }

    @Override
    public List<Order> searchOrdersByProductType(String productType) {
        return null;
    }

    @Override
    public BigDecimal calculateTaxForOrder(Order order) {
        return null;
    }

    @Override
    public BigDecimal calculateCostForProductType(Order order) {
        return null;
    }
}
