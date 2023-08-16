package service;

import dao.ProductDao;
import modelDTO.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

/**
 * This class provides unit tests for the ProductServiceImpl class.
 * It ensures that the CRUD operations work as expected.
 */
public class ProductServiceImplTest {

    // Mock the data access object for products.
    @Mock
    private ProductDao productDao;

    // Automatically inject the mock object into the service implementation.
    @InjectMocks
    private ProductServiceImpl productService;

    /**
     * This method sets up the test environment. It is run before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize our mock objects
    }

    /**
     * Test case to verify fetching all products.
     * It ensures the returned list from the service matches the mock data source's output.
     */
    @Test
    public void testGetAllProducts() {
        Product dummyProduct = new Product("Tile", new BigDecimal("5.0"), new BigDecimal("6.0"));
        when(productDao.getAllProducts()).thenReturn(Arrays.asList(dummyProduct));

        List<Product> products = productService.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("Tile", products.get(0).getProductType());
        verify(productDao, times(1)).getAllProducts();
    }

    /**
     * Test case to verify fetching a product by its type.
     * It ensures the returned product from the service matches the mock data source's output.
     */
    @Test
    public void testGetProductByType() {
        Product dummyProduct = new Product("Tile", new BigDecimal("5.0"), new BigDecimal("6.0"));
        when(productDao.getProductByType("Tile")).thenReturn(dummyProduct);

        Product product = productService.getProductByType("Tile");

        assertNotNull(product);
        assertEquals("Tile", product.getProductType());
        verify(productDao, times(1)).getProductByType("Tile");
    }

    /**
     * Test case to verify adding a valid product.
     * It ensures the product is correctly added using the mock data source.
     */
    @Test
    public void testAddValidProduct() {
        Product validProduct = new Product("Wood", new BigDecimal("3.0"), new BigDecimal("4.0"));
        doNothing().when(productDao).addProduct(validProduct);

        productService.addProduct(validProduct);

        verify(productDao, times(1)).addProduct(validProduct);
    }

    /**
     * Test case to verify adding an invalid product.
     * It ensures the service throws an exception when trying to add an invalid product.
     */

    @Test
    public void testAddInvalidProduct() {
        Product invalidProduct = new Product(null, new BigDecimal("3.0"), new BigDecimal("4.0"));

        Exception exception = assertThrows(ServiceException.class, () -> {
            productService.addProduct(invalidProduct);
        });

        String expectedMessage = "Invalid product data provided.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test case to verify editing a valid product.
     * It ensures the product is correctly updated using the mock data source.
     */
    @Test
    public void testEditValidProduct() {
        Product validProduct = new Product("Wood", new BigDecimal("3.0"), new BigDecimal("4.0"));
        when(productDao.updateProduct(validProduct)).thenReturn(true);

        productService.editProduct(validProduct);

        verify(productDao, times(1)).updateProduct(validProduct);
    }

    /**
     * Test case to verify editing an invalid product.
     * It ensures the service throws an exception when trying to edit an invalid product.
     */

    @Test
    public void testEditInvalidProduct() {
        Product invalidProduct = new Product("", new BigDecimal("3.0"), new BigDecimal("4.0"));

        Exception exception = assertThrows(ServiceException.class, () -> {
            productService.editProduct(invalidProduct);
        });

        String expectedMessage = "Invalid product data provided.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test case to verify removing a product by its type.
     * It ensures the product is correctly removed using the mock data source.
     */
    @Test
    public void testRemoveProduct() {
        when(productDao.removeProductByType(anyString())).thenReturn(true);
        productService.removeProduct("Tile");

        verify(productDao, times(1)).removeProductByType("Tile");
    }

    /**
     * Test case to verify exception handling when fetching all products.
     * It ensures the service throws a ServiceException when the underlying DAO throws an exception.
     */

    @Test
    public void testDaoExceptionWhenFetching() {
        when(productDao.getAllProducts()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(ServiceException.class, () -> {
            productService.getAllProducts();
        });

        String expectedMessage = "Error getting products.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test case to verify exception handling when adding a product.
     * It ensures the service throws a ServiceException when the underlying DAO throws an exception.
     */
    @Test
    public void testDaoExceptionWhenAdding() {
        Product validProduct = new Product("Wood", new BigDecimal("3.0"), new BigDecimal("4.0"));
        doThrow(new RuntimeException("Database error")).when(productDao).addProduct(validProduct);

        Exception exception = assertThrows(ServiceException.class, () -> {
            productService.addProduct(validProduct);
        });

        String expectedMessage = "Error adding product.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test case to verify exception handling when editing a product.
     * It ensures the service throws a ServiceException when the underlying DAO throws an exception.
     */
    @Test
    public void testDaoExceptionWhenEditing() {
        Product validProduct = new Product("Wood", new BigDecimal("3.0"), new BigDecimal("4.0"));
        doThrow(new RuntimeException("Database error")).when(productDao).updateProduct(validProduct);

        Exception exception = assertThrows(ServiceException.class, () -> {
            productService.editProduct(validProduct);
        });

        String expectedMessage = "Error editing product.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test case to verify exception handling when removing a product.
     * It ensures the service throws a ServiceException when the underlying DAO throws an exception.
     */
    @Test
    public void testDaoExceptionWhenRemoving() {
        doThrow(new RuntimeException("Database error")).when(productDao).removeProductByType("Tile");

        Exception exception = assertThrows(ServiceException.class, () -> {
            productService.removeProduct("Tile");
        });

        String expectedMessage = "Error removing product.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
