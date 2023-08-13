package dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dao.ProductDao;
import modelDTO.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ProductDaoImplTest {

    private ProductDao productDao;

    @BeforeEach
    public void setUp() {
        productDao = new ProductDaoImpl();
    }

    @Test
    public void testGetProductByType() {
        Product product = productDao.getProductByType("Tile");
        assertNotNull(product);
        assertEquals("Tile", product.getProductType());
        assertEquals(new BigDecimal("3.50"), product.getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.15"), product.getLaborCostPerSquareFoot());
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = productDao.getAllProducts();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    public void testAddProduct() {
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
        assertTrue(productDao.removeProductByType("Laminate"));
        assertNull(productDao.getProductByType("Laminate"));
    }

    // Add more test cases as needed

}
