package service;

import dao.ProductDao;
import modelDTO.Product;
import java.util.List;

public class ProductServiceImpl implements ProductService{

    private ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getAllProducts() {
        // Fetching all products from the DAO
        return productDao.getAllProducts();
    }

    @Override
    public Product getProductByType(String productType) {
        // Fetching a specific product by its type from the DAO
        return productDao.getProductByType(productType);
    }

    @Override
    public void addProduct(Product product) {
        // Validating the product data before adding
        if (validateProductData(product)) {
            productDao.addProduct(product);
        } else {
            throw new ServiceException("Invalid product data provided.");
        }
    }

    @Override
    public void editProduct(Product product) {
        // Validating the product data before editing
        if (validateProductData(product)) {
            productDao.editProduct(product);
        } else {
            throw new ServiceException("Invalid product data provided.");
        }
    }

    @Override
    public void removeProduct(String productType) {
        // Removing a product by its type
        productDao.removeProduct(productType);
    }

    @Override
    public boolean validateProductData(Product product) {
        // Basic validation: Ensure the product and its type are not null
        return product != null && product.getProductType() != null && !product.getProductType().isEmpty();
    }
}
