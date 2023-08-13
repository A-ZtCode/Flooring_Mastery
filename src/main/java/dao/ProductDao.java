package dao;

import modelDTO.Product;
import java.util.List;

public interface ProductDao {

    List<Product> getAllProducts(); // Retrieve all products

    Product getProductByType(String type); // Retrieve product by type

    // Add the additional methods that were in ProductDaoImpl but not here
    void addProduct(Product product);

    boolean updateProduct(Product product);

    boolean removeProductByType(String productType);
}
