package dao;

import static org.junit.jupiter.api.Assertions.*;

import modelDTO.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class ProductDaoTest {

    private ProductDao productDao;

    @BeforeEach
    public void setUp() {
        // Create an instance of the ProductDao implementation for testing
        productDao = new ProductDaoImpl();
    }

    @Test
    public void testGetProductByType() {
        // Test fetching product information by product type
        Product product = productDao.getProductByType("Tile");
        assertNotNull(product);
        assertEquals("Tile", product.getProductType());
        assertEquals(new BigDecimal("3.50"), product.getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.15"), product.getLaborCostPerSquareFoot());
    }

    @Test
    public void testGetAllProducts() {
        // Test retrieving all product information
        List<Product> products = productDao.getAllProducts();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    public void testAddProduct() {
        // Test adding a new product entry
        Product newProduct = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));
        productDao.addProduct(newProduct);

        Product addedProduct = productDao.getProductByType("Carpet");
        assertNotNull(addedProduct);
        assertEquals("Carpet", addedProduct.getProductType());
        assertEquals(new BigDecimal("2.25"), addedProduct.getCostPerSquareFoot());
        assertEquals(new BigDecimal("2.10"), addedProduct.getLaborCostPerSquareFoot());
    }

    @Test
    public void testUpdateProduct() {
        // Test updating an existing product entry
        Product updatedProduct = new Product("Tile", new BigDecimal("3.75"), new BigDecimal("4.30"));
        assertTrue(productDao.updateProduct(updatedProduct));

        Product retrievedProduct = productDao.getProductByType("Tile");
        assertNotNull(retrievedProduct);
        assertEquals("Tile", retrievedProduct.getProductType());
        assertEquals(new BigDecimal("3.75"), retrievedProduct.getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.30"), retrievedProduct.getLaborCostPerSquareFoot());
    }

    @Test
    public void testRemoveProduct() {
        // Test removing a product entry by product type
        assertTrue(productDao.removeProductByType("Laminate"));
        assertNull(productDao.getProductByType("Laminate"));
    }

}
