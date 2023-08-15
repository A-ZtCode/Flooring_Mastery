package dao;

import static org.junit.jupiter.api.Assertions.*;

import modelDTO.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class provides unit tests for the ProductDaoImpl.
 * It ensures basic CRUD operations on the ProductDao implementation work as expected.
 */
public class ProductDaoImplTest {

    private ProductDao productDao;

    /**
     * This method sets up the test environment. It is run before each test.
     */
    @BeforeEach
    public void setUp() {
        // Create an instance of the ProductDao implementation for testing
        productDao = new ProductDaoImpl();
    }
    /**
     * Test case to verify fetching a product by its type.
     * It checks if the retrieved product's attributes match expected values.
     */
    @Test
    public void testGetProductByType() {
        // Test fetching product information by product type
        Product product = productDao.getProductByType("Tile");
        // Assertions to ensure the retrieved product matches expected attributes
        assertNotNull(product);
        assertEquals("Tile", product.getProductType());
        assertEquals(new BigDecimal("3.75"), product.getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.30"), product.getLaborCostPerSquareFoot());
    }

    /**
     * Test case to verify fetching all products.
     * It ensures the retrieved list is not empty.
     */
    @Test
    public void testGetAllProducts() {
        // Test retrieving all product information
        List<Product> products = productDao.getAllProducts();
        // Assertions to ensure that the list of products is not empty
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    /**
     * Test case to verify adding a new product.
     * It checks if the new product is correctly stored and can be retrieved.
     */
    @Test
    public void testAddProduct() {
        // Test adding a new product entry
        Product newProduct = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));
        productDao.addProduct(newProduct);
        // Fetching the newly added product
        Product addedProduct = productDao.getProductByType("Carpet");

        // Assertions to verify that the retrieved product matches the added one
        assertNotNull(addedProduct);
        assertEquals("Carpet", addedProduct.getProductType());
        assertEquals(new BigDecimal("2.25"), addedProduct.getCostPerSquareFoot());
        assertEquals(new BigDecimal("2.10"), addedProduct.getLaborCostPerSquareFoot());
    }

    /**
     * Test case to verify updating an existing product.
     * It checks if the modifications on the product are correctly stored and can be retrieved.
     */
    @Test
    public void testUpdateProduct() {
        // Test updating an existing product entry
        Product updatedProduct = new Product("Tile", new BigDecimal("3.75"), new BigDecimal("4.30"));
        assertTrue(productDao.updateProduct(updatedProduct));
        // Test fetching the updated product
        Product retrievedProduct = productDao.getProductByType("Tile");
        // Assertions to verify that the retrieved product matches the updated attributes
        assertNotNull(retrievedProduct);
        assertEquals("Tile", retrievedProduct.getProductType());
        assertEquals(new BigDecimal("3.75"), retrievedProduct.getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.30"), retrievedProduct.getLaborCostPerSquareFoot());
    }
    /**
     * Test case to verify removing a product.
     * It ensures the product is correctly removed and can't be retrieved afterward.
     */
    @Test
    public void testRemoveProduct() {
        // Test removing a product entry by product type
        assertTrue(productDao.removeProductByType("Laminate"));
        assertNull(productDao.getProductByType("Laminate"));
    }

}
