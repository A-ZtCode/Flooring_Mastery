package service;

import dao.OrderDao;
import dao.ProductDao;
import dao.TaxDao;
import modelDTO.Order;
import modelDTO.Product;
import modelDTO.Tax;

import java.util.Date;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final TaxDao taxDao;

    // Explicit dependency injection through the constructor
    public OrderServiceImpl(OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    @Override
    public void addOrder(Order order) {
        validateProductType(order.getProductType());
        validateState(order.getState());
        calculateOrderCosts(order);
        orderDao.addOrder(order);
    }

    @Override
    public void editOrder(Order order) {
        Order existingOrder = orderDao.getOrderById(order.getOrderNumber());
        if (existingOrder == null) {
            throw new ServiceException("Order not found!");
        }
        validateProductType(order.getProductType());
        validateState(order.getState());
        calculateOrderCosts(order);
        orderDao.editOrder(order);
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
        return orderDao.getAllOrders();
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
            throw new ServiceException("Invalid state!");
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
