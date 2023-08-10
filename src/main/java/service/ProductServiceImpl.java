package service;

import dao.ProductDao;
import modelDTO.Product;

import java.util.List;

public class ProductServiceImpl implements ProductService{

    private ProductDao productDao;

    // Dependencies are injected, either through a constructor or setters.
    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }
    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public Product getProductByType(String productType) {
        return null;
    }

    @Override
    public void addProduct(Product product) {

    }

    @Override
    public void editProduct(Product product) {

    }

    @Override
    public void removeProduct(String productType) {

    }

    @Override
    public boolean validateProductData(Product product) {
        return false;
    }
}
