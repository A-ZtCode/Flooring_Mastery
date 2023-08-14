package dao;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import modelDTO.Order;
import service.OrderNotFoundException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Test class for OrderDaoImpl. It includes unit tests to verify the basic CRUD operations
 * on the OrderDao implementation.
 */
public class OrderDaoImplTest {

    private OrderDao orderDao;

    /**
     * Set up the test environment. This method is run before each test.
     */
    @Before
    public void setUp() {
        orderDao = new OrderDaoImpl(); // Initialize OrderDao
    }

    /**
     * Test case to verify adding an order and then retrieving orders by date.
     * It checks if the added order is correctly stored and can be retrieved using its date.
     */
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
        assertEquals(order.getOrderNumber(), retrievedOrder.getOrderNumber());
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

    /**
     * Test case to verify editing an existing order and then retrieving it by ID.
     * It checks if the modifications on the order are correctly stored and can be retrieved using its ID.
     */
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
        Order retrievedOrder = orderDao.getOrderById((int) order.getOrderNumber());

        // Assertions
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

}
