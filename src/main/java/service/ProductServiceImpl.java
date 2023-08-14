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
    private final ProductDao productDao;

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
        return productDao.getAllProducts();
    }

    /**
     * Fetches a specific product based on its type.
     * @param productType The type of the product to be retrieved.
     * @return The product corresponding to the provided type, or null if not found.
     */
    @Override
    public Product getProductByType(String productType) {
        return productDao.getProductByType(productType);
    }

    /**
     * Adds a new product to the data store after validating its data.
     * @param product The product object to be added.
     * @throws ServiceException if the provided product data is invalid.
     */
    @Override
    public void addProduct(Product product) {
        if (validateProductData(product)) {
            productDao.addProduct(product);
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
            productDao.updateProduct(product);
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
        productDao.removeProductByType(productType);
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
