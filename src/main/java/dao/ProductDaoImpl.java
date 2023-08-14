package dao;

import modelDTO.Product;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the ProductDao interface. Provides methods
 * to interact with the product data source, which in this case is a file.
 */
public class ProductDaoImpl implements ProductDao {

    // In-memory storage for products, using a Map for faster access based on product type.
    private final Map<String, Product> products = new HashMap<>();
    // Path to the file that contains the product data.
    private final String FILE_PATH = "src/main/java/Products.txt";

    /**
     * Constructor loads products from the file into memory.
     */
    public ProductDaoImpl() {
        loadProductsFromFile();
    } // e

    /**
     * Fetches a product from in-memory storage based on product type.
     */
    //@Override
    public Product getProductByType(String productType) {
        return products.get(productType);
    }

    /**
     * Retrieves all products from in-memory storage.
     */
    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    /**
     * Adds a new product to in-memory storage and then saves to file.
     * Throws an exception if the product already exists.
     */
    @Override
    public void addProduct(Product product) {
        if (products.containsKey(product.getProductType())) {
            throw new DataPersistenceException("Product of type " + product.getProductType() + " already exists!");
        }
        backupFile(); // Backup before adding
        products.put(product.getProductType(), product);
        try {
            saveProductsToFile(); // Save after adding
        } catch (DataPersistenceException ex) {
            restoreBackup(); // Restore backup if there's an error
            throw ex; // Re-throw the exception
        }
    }


    /**
     * Updates an existing product in in-memory storage and then saves to file.
     * Returns false if the product does not exist.
     */
    @Override
    public boolean updateProduct(Product product) {
        if (!products.containsKey(product.getProductType())) {
            return false;
        }
        products.put(product.getProductType(), product);
        saveProductsToFile();
        return true;
    }

    /**
     * Removes a product from in-memory storage based on product type and then saves to file.
     * Returns false if the product does not exist.
     */
    @Override
    public boolean removeProductByType(String productType) {
        if (!products.containsKey(productType)) {
            return false;
        }
        products.remove(productType);
        saveProductsToFile();
        return true;
    }

    /**
     * Loads products from the file and stores them in-memory.
     * Throws a DaoException if there's an issue reading from the file.
     */
    private void loadProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine();  // This will skip the first line, which is the header

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    try {
                        String productType = parts[0].trim();
                        BigDecimal costPerSquareFoot = new BigDecimal(parts[1].trim());
                        BigDecimal laborCostPerSquareFoot = new BigDecimal(parts[2].trim());
                        Product product = new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);
                        products.put(productType, product);
                    } catch (NumberFormatException e) {
                        System.err.println("Error converting value to number from line: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException ex) {
            throw new DataPersistenceException("Error reading products from file.", ex);
        }
    }

    /**
     * Saves the current in-memory products to the file.
     * Throws a DaoException if there's an issue writing to the file.
     */
    private void saveProductsToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (Product product : products.values()) {
                writer.write(product.getProductType() + "," + product.getCostPerSquareFoot() + "," + product.getLaborCostPerSquareFoot() + "\n");
            }
        } catch (IOException ex) {
            throw new DataPersistenceException("Error writing products to file.", ex);
        }
    }

    private final String BACKUP_PATH = FILE_PATH + ".bak"; // Backup

    // Backup the current data file
    private void backupFile() {
        File sourceFile = new File(FILE_PATH);
        File backupFile = new File(BACKUP_PATH);
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(backupFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            throw new DataPersistenceException("Error creating backup for products.", ex);
        }
    }

    // Restore from the backup file
    private void restoreBackup() {
        File backupFile = new File(BACKUP_PATH);
        File sourceFile = new File(FILE_PATH);
        try (FileInputStream fis = new FileInputStream(backupFile);
             FileOutputStream fos = new FileOutputStream(sourceFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            throw new DataPersistenceException("Error restoring backup for products.", ex);
        }
    }

}









