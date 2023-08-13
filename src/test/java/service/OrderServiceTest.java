package service;

import dao.ProductDao;
import dao.TaxDao;
import modelDTO.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import dao.OrderDao;
import modelDTO.Order;
import service.OrderService;
import service.OrderServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    private ProductDao productDao;

    private TaxDao taxDao;
    @InjectMocks
    private OrderService orderService = new OrderServiceImpl(orderDao, productDao, taxDao);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize our mock objects

        // Set up some dummy data for our mock DAO
        Order dummyOrder = new Order(1, "Test Customer", "CA", new BigDecimal("6.25"), "Wood", new BigDecimal("500"), new BigDecimal("5.5"), new BigDecimal("4.5"), new BigDecimal("2750"), new BigDecimal("2250"), new BigDecimal("312.5"), new BigDecimal("5312.5"), new Date());
        when(orderDao.getOrdersByDate(any(Date.class))).thenReturn(Arrays.asList(dummyOrder));
    }

    @Test
    public void testGetOrdersByDate() {
        Date testDate = new Date(); // Use current date for this test
        List<Order> orders = orderService.getOrdersByDate(testDate);

        // Assertions
        assertNotNull(orders, "Orders should not be null");
        assertEquals(1, orders.size(), "Should return 1 order");
        assertEquals("Test Customer", orders.get(0).getCustomerName(), "Customer name should match");
    }

    @Test
    public void testAddOrder() {
        Order newOrder = new Order(null, "New Customer", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("300"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), new Date());

        // Mock the behavior for adding an order
        doNothing().when(orderDao).addOrder(any(Order.class));

        // Execute the addOrder method
        orderService.addOrder(newOrder);

        // Verify the interaction with the mock
        verify(orderDao, times(1)).addOrder(newOrder);
    }

    // ... Add more test methods for editOrder, removeOrder, etc. ...

}
