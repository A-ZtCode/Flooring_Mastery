package dao;

import modelDTO.Product;
import java.util.List;

/**
 * ProductDao represents the data access object interface for Product entities.
 * It provides methods to perform CRUD operations on products.
 */
public interface ProductDao {

    /**
     * Retrieves all the available products from the data store.
     * @return A list of all products.
     */
    List<Product> getAllProducts();

    /**
     * Retrieves a product based on its type.
     * @param type The type of the product to retrieve.
     * @return The product matching the given type, or null if not found.
     */
    Product getProductByType(String type);

    /**
     * Adds a new product to the data store.
     * @param product The product entity to be added.
     */
    void addProduct(Product product);

    /**
     * Updates an existing product in the data store.
     * @param product The updated product entity.
     * @return true if the product was updated successfully, false otherwise.
     */
    boolean updateProduct(Product product);

    /**
     * Removes a product based on its type.
     * @param productType The type of the product to be removed.
     * @return true if the product was removed successfully, false otherwise.
     */
    boolean removeProductByType(String productType);
}
