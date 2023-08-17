package dao;

import static org.junit.jupiter.api.Assertions.*;

import modelDTO.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides unit tests for the ProductDaoImpl.
 * It ensures basic CRUD operations on the ProductDao implementation work as expected.
 */
public class ProductDaoImplTest {

    private ProductDao productDao;
    @TempDir
    Path tempDir;  // This provides a temporary directory for the duration of the test.

    /**
     * This method sets up the test environment. It is run before each test.
     */
    @BeforeEach
    public void setUp() throws IOException {
        // Create a temporary directory for our tests
        Path tempDir = Files.createTempDirectory("junit");
        Path tempFile = tempDir.resolve("Products.txt");

        // Populate the temporary file with some sample data (including the header)
        List<String> sampleProducts = Arrays.asList(
                "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot",
                "Tile,3.50,4.15",
                "Wood,5.15,4.75",
                "Carpet,2.25,2.10",
                "Terrazzo,6.50,7.10",
                "Marble,5.95,6.25",
                "Quartz,6.15,6.75",
                "Stone,5.60,6.70",
                "Granite,6.70,7.30",
               " Pebble,5.00,5.60",
                "Porcelain,3.45,4.55",
                "Resin,4.25,4.85",
                "Brick,3.95,4.50",
                "Travertine,5.90,6.50",
                "Terracotta,3.80,4.40",
                "Ceramic,3.15,4.20",
                "Concrete,3.25,4.35",
                "Rubber,2.50,3.10",
                "Bamboo,5.15,5.65",
                "Slate,5.80,6.40",
                "Sandstone,5.35,6.00"
        );
        Files.write(tempFile, sampleProducts);

        // Create an instance of the ProductDao implementation for testing, using the temporary file
        productDao = new ProductDaoImpl(tempFile.toString());
    }

    /**
     * Test case to verify fetching a product by its type.
     * It checks if the retrieved product's attributes match expected values.
     */
    @Test
    public void testGetProductByType() {
        // Test fetching product information by product type
        Product product = productDao.getProductByType("Tile");

        // Add a diagnostic output
        if (product == null) {
            System.out.println("Product 'Tile' not found. Current products:");
            for (String key : ((ProductDaoImpl) productDao).getProductKeys()) {
                System.out.println(" - " + key);
            }
        }

        // Assertions to ensure the retrieved product matches expected attributes
        assertNotNull(product);
        assertEquals("Tile", product.getProductType());
        assertEquals(new BigDecimal("3.50"), product.getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.15"), product.getLaborCostPerSquareFoot());
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
        Product newProduct = new Product("Laminate", new BigDecimal("2.10"), new BigDecimal("2.80"));
        productDao.addProduct(newProduct);
        // Fetching the newly added product
        Product addedProduct = productDao.getProductByType("Laminate");

        // Assertions to verify that the retrieved product matches the added one
        assertNotNull(addedProduct);
        assertEquals("Laminate", addedProduct.getProductType());
        assertEquals(new BigDecimal("2.10"), addedProduct.getCostPerSquareFoot());
        assertEquals(new BigDecimal("2.80"), addedProduct.getLaborCostPerSquareFoot());
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
       //  Assertions to verify that the removed product matches
        assertTrue(productDao.removeProductByType("Bamboo"));
        assertNull(productDao.getProductByType("Bamboo"));
    }

}
