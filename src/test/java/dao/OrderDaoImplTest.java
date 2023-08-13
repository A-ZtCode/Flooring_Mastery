package dao;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dao.OrderDao;
import dao.OrderDaoImpl;
import modelDTO.Order;
import modelDTO.Tax;
import service.OrderNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDaoImplTest {

    private OrderDao orderDao;

    @Before
    public void setUp() {
        orderDao = new OrderDaoImpl(); // Initialize OrderDao
    }

    @Test
    public void testAddOrderAndGetOrdersByDate() {
        // Create a sample order
        Order order = new Order(1, "Customer 1", "TX", new BigDecimal("4.45"), "Tile", new BigDecimal("200"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("700"), new BigDecimal("830"),
                new BigDecimal("66.25"), new BigDecimal("1596.25"), new Date());

        // Add the order
        orderDao.addOrder(order);

        // Get orders by date
        List<Order> orders = orderDao.getOrdersByDate(order.getOrderDate());

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());

        // Verify order details
        Order retrievedOrder = orders.get(0);
        assertEquals(order.getOrderId(), retrievedOrder.getOrderId());
        assertEquals(order.getCustomerName(), retrievedOrder.getCustomerName());
        assertEquals(order.getState(), retrievedOrder.getState());
        assertEquals(order.getTaxRate(), retrievedOrder.getTaxRate());
        assertEquals(order.getProductType(), retrievedOrder.getProductType());
        assertEquals(order.getArea(), retrievedOrder.getArea());
        assertEquals(order.getCostPerSquareFoot(), retrievedOrder.getCostPerSquareFoot());
        assertEquals(order.getLaborCostPerSquareFoot(), retrievedOrder.getLaborCostPerSquareFoot());
        assertEquals(order.getMaterialCost(), retrievedOrder.getMaterialCost());
        assertEquals(order.getLaborCost(), retrievedOrder.getLaborCost());
        assertEquals(order.getTax(), retrievedOrder.getTax());
        assertEquals(order.getTotal(), retrievedOrder.getTotal());
    }

    @Test
    public void testEditOrderAndGetOrderById() throws OrderNotFoundException {
        // Create a sample order
        Order order = new Order(1, "Customer 1", "TX", new BigDecimal("4.45"), "Tile", new BigDecimal("200"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("700"), new BigDecimal("830"),
                new BigDecimal("66.25"), new BigDecimal("1596.25"), new Date());

        // Add the order
        orderDao.addOrder(order);

        // Edit the order
        order.setCustomerName("Updated Customer");
        order.setState("NY");
        orderDao.editOrder(order);

        // Get the edited order by ID
        Order retrievedOrder = orderDao.getOrderById((int) order.getOrderId());

        assertNotNull(retrievedOrder);
        assertEquals("Updated Customer", retrievedOrder.getCustomerName());
        assertEquals("NY", retrievedOrder.getState());
        assertEquals(order.getTaxRate(), retrievedOrder.getTaxRate());
        assertEquals(order.getProductType(), retrievedOrder.getProductType());
        assertEquals(order.getArea(), retrievedOrder.getArea());
        assertEquals(order.getCostPerSquareFoot(), retrievedOrder.getCostPerSquareFoot());
        assertEquals(order.getLaborCostPerSquareFoot(), retrievedOrder.getLaborCostPerSquareFoot());
        assertEquals(order.getMaterialCost(), retrievedOrder.getMaterialCost());
        assertEquals(order.getLaborCost(), retrievedOrder.getLaborCost());
        assertEquals(order.getTax(), retrievedOrder.getTax());
        assertEquals(order.getTotal(), retrievedOrder.getTotal());
    }

    // Add more test methods for other operations

}
