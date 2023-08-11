package dao;

import modelDTO.Product;

import java.util.List;

// Product DAO
public interface ProductDao {

    abstract Product getProductByType(String productType);

    public static List<Product> getAllProducts() {
        // Code to retrieve all products
        return null;
    }

    public static Product getProductByType(String type) {
        // Code to retrieve product by type
        return null;
    }

    List<Product> getAllProducts();

    void addProduct(Product product);

    boolean updateProduct(Product product);

    boolean removeProductByType(String productType);
}