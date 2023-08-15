package service;

import dao.OrderDao;
import dao.ProductDao;
import dao.TaxDao;
import modelDTO.Order;
import modelDTO.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private TaxDao taxDao;

    @InjectMocks
    private OrderServiceImpl  orderService;

    private Order sampleOrder;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize our mock objects

        // Initialize the sampleOrder
        sampleOrder = new Order(1, "Jane Doe", "TX", new BigDecimal("6.25"), "Tile",
                new BigDecimal("300"), new BigDecimal("5.0"), new BigDecimal("6.0"),
                new BigDecimal("1500"), new BigDecimal("1800"), new BigDecimal("100"),
                new BigDecimal("3400"), new Date()); // fill in other parameters

        // Set up some dummy data for our mock DAO
        Order dummyOrder = new Order(1, "Jane Doe", "TX", new BigDecimal("6.25"), "Wood", new BigDecimal("500"), new BigDecimal("5.5"), new BigDecimal("4.5"), new BigDecimal("2750"), new BigDecimal("2250"), new BigDecimal("312.5"), new BigDecimal("5312.5"), new Date());
        when(orderDao.getOrdersByDate(any(Date.class))).thenReturn(Arrays.asList(dummyOrder));
    }

    // Test methods for GetOrdersByDate,
    @Test
    public void testGetOrdersByDate() {
        Date testDate = new Date(); // Use current date for this test
        Order dummyOrder = new Order(1, "Jane Doe", "TX", new BigDecimal("6.25"), "Wood", new BigDecimal("300"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), testDate);
        when(orderDao.getOrdersByDate(testDate)).thenReturn(Collections.singletonList(dummyOrder));

        List<Order> result = orderService.getOrdersByDate(testDate);

        assertEquals(1, result.size());
        assertEquals(testDate, result.get(0).getOrderDate());
        verify(orderDao, times(1)).getOrdersByDate(testDate);
    }

    // Test methods for GetOrdersById.
    @Test
    public void testGetOrderById() {
        Order dummyOrder = new Order(1, "Jane Doe", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("300"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), new Date());
        when(orderDao.getOrderById(1)).thenReturn(dummyOrder);

        Order result = orderService.getOrderById(1);

        assertNotNull(result);
        assertEquals(1, result.getOrderNumber().intValue());
        verify(orderDao, times(1)).getOrderById(1);
    }

    // Test methods for AddOrder,
    @Test
    public void testAddOrder() {
        Order newOrder = new Order(null, "Jane Doe", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("300"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), new Date());

        // Mock the behavior for adding an order
        when(orderDao.addOrder(any(Order.class))).thenReturn(sampleOrder);
        // Execute the addOrder method
        orderService.addOrder(newOrder);

        // Verify the interaction with the mock
        verify(orderDao, times(1)).addOrder(newOrder);
    }

    // Test methods for editOrder
    @Test
    public void testEditOrder() throws OrderNotFoundException {
        Order existingOrder = new Order(1, "Existing Customer", "TX", new BigDecimal("6.25"), "Wood", new BigDecimal("400"), new BigDecimal("5.0"), new BigDecimal("6.0"), new BigDecimal("2000"), new BigDecimal("2400"), new BigDecimal("150"), new BigDecimal("4550"), new Date());
        Order updatedOrder = new Order(1, "Updated Customer", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("500"), new BigDecimal("6.0"), new BigDecimal("7.0"), new BigDecimal("3000"), new BigDecimal("3500"), new BigDecimal("200"), new BigDecimal("6700"), new Date());

        when(orderDao.getOrderById(1)).thenReturn(existingOrder);
        doNothing().when(orderDao).editOrder(updatedOrder);

        orderService.editOrder(updatedOrder);

        verify(orderDao, times(1)).editOrder(updatedOrder);
    }

    // Test methods for removeOrder,
    @Test
    public void testRemoveOrder() {
        Order dummyOrder = new Order(1, "Jane Doe", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("300"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), new Date());
        when(orderDao.getOrderById(1)).thenReturn(dummyOrder);

        doNothing().when(orderDao).removeOrder(1);

        orderService.removeOrder(1);

        verify(orderDao, times(1)).removeOrder(1);
    }

    // Test methods for SearchOrdersByName,
    @Test
    public void testSearchOrdersByName() {
        Order dummyOrder = new Order(1, "Jane Doe", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("300"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), new Date());
        when(orderDao.searchOrdersByName("Jane Doe")).thenReturn(Collections.singletonList(dummyOrder));

        List<Order> result = orderService.searchOrdersByName("Jane Doe");

        assertEquals(1, result.size());
        assertEquals("Jane Doe", result.get(0).getCustomerName());
        verify(orderDao, times(1)).searchOrdersByName("Jane Doe");
    }

    // Test methods for SearchOrdersByState,
    @Test
    public void testSearchOrdersByState() {
        Order dummyOrder = new Order(1, "Jane Doe", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("300"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), new Date());
        when(orderDao.searchOrdersByState("TX")).thenReturn(Collections.singletonList(dummyOrder));

        List<Order> result = orderService.searchOrdersByState("TX");

        assertEquals(1, result.size());
        assertEquals("TX", result.get(0).getState());
        verify(orderDao, times(1)).searchOrdersByState("TX");
    }

    // Test methods for SearchOrdersByProductType,
    @Test
    public void testSearchOrdersByProductType() {
        Order dummyOrder = new Order(1, "Jane Doe", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("300"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), new Date());
        when(orderDao.searchOrdersByProductType("Tile")).thenReturn(Collections.singletonList(dummyOrder));

        List<Order> result = orderService.searchOrdersByProductType("Tile");

        assertEquals(1, result.size());
        assertEquals("Tile", result.get(0).getProductType());
        verify(orderDao, times(1)).searchOrdersByProductType("Tile");
    }

    // Test methods for validateOrderData
    @Test
    public void testValidateNullOrder() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.validateOrderData(null);
        });

        String expectedMessage = "Order cannot be null!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testValidateOrderWithEmptyCustomerName() {
        Order orderWithEmptyName = new Order();
        orderWithEmptyName.setCustomerName("");
        orderWithEmptyName.setArea(new BigDecimal("100"));  // Set a default area

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.validateOrderData(orderWithEmptyName);
        });

        String expectedMessage = "Customer name cannot be empty!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testValidateOrderWithNullState() {
        Order orderWithNullState = new Order();
        orderWithNullState.setCustomerName("Jane");
        orderWithNullState.setState(null);
        orderWithNullState.setArea(new BigDecimal("100"));  // Set a default area

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.validateOrderData(orderWithNullState);
        });

        String expectedMessage = "State cannot be empty!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testValidateOrderWithEmptyProductType() {
        Order orderWithEmptyProductType = new Order();
        orderWithEmptyProductType.setCustomerName("Jane");
        orderWithEmptyProductType.setState("TX");
        orderWithEmptyProductType.setProductType("");
        orderWithEmptyProductType.setArea(new BigDecimal("100"));  // Set a default area

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.validateOrderData(orderWithEmptyProductType);
        });

        String expectedMessage = "Product type cannot be empty!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    // Test methods for addOrder
    @Test
    public void testAddNullOrder() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.addOrder(null);
        });

        String expectedMessage = "Order cannot be null!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Test methods for editOrder
    @Test
    public void testEditNullOrder() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.editOrder(null);
        });

        String expectedMessage = "Order cannot be null!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testEditNonExistentOrder() {
        Order nonExistentOrder = new Order();
        nonExistentOrder.setOrderNumber(999); // Assuming this ID does not exist in the DAO
        when(orderDao.getOrderById(999)).thenReturn(null);

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.editOrder(nonExistentOrder);
        });

        String expectedMessage = "Order not found!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Test methods for removeOrder
    @Test
    public void testRemoveNonExistentOrder() {
        int nonExistentOrderId = 999; // Assuming this ID does not exist in the DAO
        when(orderDao.getOrderById(999)).thenReturn(null);

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.removeOrder(nonExistentOrderId);
        });

        String expectedMessage = "Order not found!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    /**
     * Test the behavior of the addOrder method when provided an order with a zero area.
     * It should throw a ServiceException with an appropriate error message.
     */
    @Test
    public void testAddOrderWithZeroArea() {
        Order newOrder = new Order(null, "Jane Doe", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("0"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), new Date());

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.addOrder(newOrder);
        });

        String expectedMessage = "Area cannot be zero or negative!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test the behavior of the addOrder method when provided an order with a negative area.
     * It should throw a ServiceException with an appropriate error message.
     */
    @Test
    public void testAddOrderWithNegativeArea() {
        Order newOrder = new Order(null, "Jane Doe", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("-10"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), new Date());

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.addOrder(newOrder);
        });

        String expectedMessage = "Area cannot be zero or negative!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test the behavior of the addOrder method when provided an order with an empty customer name.
     * It should throw a ServiceException with an appropriate error message.
     */
    @Test
    public void testAddOrderWithEmptyCustomerName() {
        Order newOrder = new Order(null, "", "TX", new BigDecimal("6.25"), "Tile", new BigDecimal("300"), new BigDecimal("4.0"), new BigDecimal("5.0"), new BigDecimal("1200"), new BigDecimal("1500"), new BigDecimal("168.75"), new BigDecimal("2868.75"), new Date());

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.addOrder(newOrder);
        });

        String expectedMessage = "Customer name cannot be empty!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    /**
     * Test the behavior of the searchOrdersByName method when provided a null customer name.
     * It should throw a ServiceException with an appropriate error message.
     */
    @Test
    public void testSearchOrdersByNameWithNull() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.searchOrdersByName(null);
        });

        String expectedMessage = "Customer name cannot be empty!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test the behavior of the searchOrdersByState method when provided a null state.
     * It should throw a ServiceException with an appropriate error message.
     */
    @Test
    public void testSearchOrdersByStateWithNull() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.searchOrdersByState(null);
        });

        String expectedMessage = "State cannot be empty!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test the behavior of the searchOrdersByProductType method when provided a null product type.
     * It should throw a ServiceException with an appropriate error message.
     */
    @Test
    public void testSearchOrdersByProductTypeWithNull() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.searchOrdersByProductType(null);
        });

        String expectedMessage = "Product type cannot be empty!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test the behavior of the getOrdersByDate method when there are no orders for the specified date.
     * It should return an empty list.
     */
    @Test
    public void testGetOrdersByDateWithNoOrders() {
        // Mock the DAO to return an empty list
        when(orderDao.getOrdersByDate(any(Date.class))).thenReturn(Collections.emptyList());

        List<Order> result = orderService.getOrdersByDate(new Date());

        assertTrue(result.isEmpty());
    }

    /**
     * Test the behavior of the getOrderById method when there is no matching order for the provided ID.
     * It should return null.
     */
    @Test
    public void testGetOrderByIdWithNoMatchingOrder() {
        // Mock the DAO to return null for the specified order ID
        when(orderDao.getOrderById(99)).thenReturn(null);

        Order result = orderService.getOrderById(99);

        assertNull(result);
    }

    // Assuming TaxDao might return null for a particular state
    @Test
    public void testNullTaxForState() {
        when(taxDao.getTaxByState("TX")).thenReturn(null);

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.calculateTaxForOrder(sampleOrder);
        });

        String expectedMessage = "Tax information not available for TX!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Assuming ProductDao might return null for a particular product type
    @Test
    public void testNullProductInfo() {
        // Mock the ProductDao to return a product with null or default values.
        Product dummyProduct = new Product("Tile", null, null);  // Assuming the Product constructor allows null values for cost and labor cost.
        when(productDao.getProductByType(anyString())).thenReturn(dummyProduct);

        // Now, when the calculateCostForProductType method tries to use this product,
        // it might encounter null values for the product's properties, leading to a different exception or outcome.
        // Adjust the test to expect this new outcome.

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.calculateCostForProductType(sampleOrder);
        });

        // Update the expected message based on the new outcome.
        // For example, if the service method throws an exception when the product's cost is null, adjust accordingly:
        String expectedMessage = "Product cost information not available for Tile!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    // Exception during DAO operations
    @Test
    public void testDaoException() {
        when(orderDao.getOrdersByDate(any(Date.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderService.getOrdersByDate(new Date());
        });

        String expectedMessage = "Error fetching orders.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }



}
