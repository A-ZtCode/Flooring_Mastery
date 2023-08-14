package service;

import modelDTO.Product;
import java.util.List;

/**
 * Defines the contract for product-related operations within the service layer.
 * This interface provides methods to fetch, manage, and validate product data.
 */
public interface ProductService {

        /**
         * Retrieves a list of all available products.
         * @return List of all products.
         */
        List<Product> getAllProducts();

        /**
         * Fetches a product based on its type.
         * @param productType The type of the product to be retrieved.
         * @return The product corresponding to the provided type, or null if not found.
         */
        Product getProductByType(String productType);

        /**
         * Adds a new product to the data store.
         * @param product The product object to be added.
         */
        void addProduct(Product product);

        /**
         * Updates the details of an existing product in the data store.
         * @param product The product object with updated details.
         */
        void editProduct(Product product);

        /**
         * Removes a product from the data store based on its type.
         * @param productType The type of the product to be removed.
         */
        void removeProduct(String productType);

        /**
         * Validates the details of a given product. This method ensures that all product
         * attributes are valid and consistent.
         * @param product The product object to be validated.
         * @return true if the product data is valid, false otherwise.
         */
        boolean validateProductData(Product product);
}
