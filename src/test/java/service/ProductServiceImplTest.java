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

public class ProductServiceImplTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService = new ProductServiceImpl(productDao);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize our mock objects
    }

    @Test
    public void testGetAllProducts() {
        Product dummyProduct = new Product("Tile", new BigDecimal("5.0"), new BigDecimal("6.0"));
        when(productDao.getAllProducts()).thenReturn(Arrays.asList(dummyProduct));

        List<Product> products = productService.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("Tile", products.get(0).getProductType());
        verify(productDao, times(1)).getAllProducts();
    }

    @Test
    public void testGetProductByType() {
        Product dummyProduct = new Product("Tile", new BigDecimal("5.0"), new BigDecimal("6.0"));
        when(productDao.getProductByType("Tile")).thenReturn(dummyProduct);

        Product product = productService.getProductByType("Tile");

        assertNotNull(product);
        assertEquals("Tile", product.getProductType());
        verify(productDao, times(1)).getProductByType("Tile");
    }

    @Test
    public void testAddValidProduct() {
        Product validProduct = new Product("Wood", new BigDecimal("3.0"), new BigDecimal("4.0"));
        doNothing().when(productDao).addProduct(validProduct);

        productService.addProduct(validProduct);

        verify(productDao, times(1)).addProduct(validProduct);
    }

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

    @Test
    public void testEditValidProduct() {
        Product validProduct = new Product("Wood", new BigDecimal("3.0"), new BigDecimal("4.0"));
        doNothing().when(productDao).updateProduct(validProduct);

        productService.editProduct(validProduct);

        verify(productDao, times(1)).updateProduct(validProduct);
    }

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

    @Test
    public void testRemoveProduct() {
        doNothing().when(productDao).removeProductByType("Tile");

        productService.removeProduct("Tile");

        verify(productDao, times(1)).removeProductByType("Tile");
    }

    @Test
    public void testAddProductWithNegativeCosts() {
        Product invalidProduct = new Product("Tile", new BigDecimal("-3.0"), new BigDecimal("-4.0"));

        Exception exception = assertThrows(ServiceException.class, () -> {
            productService.addProduct(invalidProduct);
        });

        String expectedMessage = "Invalid product data provided.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddProductWithZeroCosts() {
        Product invalidProduct = new Product("Tile", BigDecimal.ZERO, BigDecimal.ZERO);

        Exception exception = assertThrows(ServiceException.class, () -> {
            productService.addProduct(invalidProduct);
        });

        String expectedMessage = "Invalid product data provided.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

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
