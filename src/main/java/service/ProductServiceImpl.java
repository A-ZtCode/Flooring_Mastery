package service;

import dao.ProductDao;
import modelDTO.Product;

import java.util.List;

/**
 * An implementation of the ProductService interface.
 * This class provides business logic and validation for product-related operations,
 * interacting with the Data Access Object (DAO) layer to manage product data.
 */
public class ProductServiceImpl implements ProductService {

    // Instance of ProductDao to interact with the data store.
    private ProductDao productDao;

    /**
     * Constructor for ProductServiceImpl.
     * @param productDao The ProductDao instance for data operations.
     */
    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * Fetches all available products from the data store.
     * @return List of all products.
     */
    @Override
    public List<Product> getAllProducts() {
        try {
            return productDao.getAllProducts();
        } catch (RuntimeException e) {
            throw new ServiceException("Error getting products.", e);
        }
    }

    /**
     * Fetches a specific product based on its type.
     * @param type The type of the product to be retrieved.
     * @return The product corresponding to the provided type, or null if not found.
     */
    @Override
    public Product getProductByType(String type) {
        try {
            return productDao.getProductByType(type);
        } catch (RuntimeException e) {
            throw new ServiceException("Error getting product by type.", e);
        }
    }
        /**
         * Adds a new product to the data store after validating its data.
         * @param product The product object to be added.
         * @throws ServiceException if the provided product data is invalid.
         */
        @Override
        public void addProduct(Product product) {
            if (validateProductData(product)) {
                try {
                    productDao.addProduct(product);
                } catch (RuntimeException e) {
                    throw new ServiceException("Error adding product.", e);
                }
            } else {
                throw new ServiceException("Invalid product data provided.");
            }
        }

    /**
     * Updates an existing product in the data store after validating its data.
     * @param product The product object with updated details.
     * @throws ServiceException if the provided product data is invalid.
     */
    @Override
    public void editProduct(Product product) {
        if (validateProductData(product)) {
            try {
                productDao.updateProduct(product);
            } catch (RuntimeException e) {
                throw new ServiceException("Error editing product.", e);
            }
        } else {
            throw new ServiceException("Invalid product data provided.");
        }
    }

    /**
     * Removes a product from the data store based on its type.
     * @param productType The type of the product to be removed.
     */
    @Override
    public void removeProduct(String productType) {
        try {
            productDao.removeProductByType(productType);
        } catch (RuntimeException e) {
            throw new ServiceException("Error removing product.", e);
        }
    }

    /**
     * Validates the provided product data.
     * In this implementation, it checks if the product and its type are not null or empty.
     * @param product The product object to be validated.
     * @return true if the product data is valid, false otherwise.
     */
    @Override
    public boolean validateProductData(Product product) {
        return product != null && product.getProductType() != null && !product.getProductType().isEmpty();
    }
}
