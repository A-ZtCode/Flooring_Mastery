package service;

import modelDTO.Product;

import java.util.List;

public interface ProductService {

        List<Product> getAllProducts(); // Get a list of all products
        Product getProductByType(String productType); // Fetch a specific product by type

        // Advanced product operations (if needed in the future)
        void addProduct(Product product);
        void editProduct(Product product);
        void removeProduct(String productType);
        boolean validateProductData(Product product);
    }
