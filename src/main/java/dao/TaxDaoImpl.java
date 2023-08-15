package dao;

import modelDTO.Tax;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * TaxDaoImpl is the concrete implementation of the TaxDao interface.
 * Tax records are stored both in-memory (using a HashMap) and in a file.
 * Backup and restore functionalities are also provided to ensure data persistence.
 */
public class TaxDaoImpl implements TaxDao {

    // In-memory storage for tax records, indexed by state abbreviation
    private final Map<String, Tax> taxes = new HashMap<>();

    // Path to the file containing tax data
    private final String FILE_PATH = "src/main/java/Taxes.txt";
    // Backup path for the tax data file
//    private final String BACKUP_PATH = FILE_PATH + ".bak";

    /**
     * Constructor initializes the DAO by loading tax records from the file into in-memory storage.
     */
    public TaxDaoImpl() {
        // Load taxes into the in-memory map during instantiation
        loadTaxesFromFile().forEach(tax -> {
            taxes.put(tax.getStateAbbreviation(), tax);
            System.out.println("Loaded: " + tax.getStateAbbreviation());
        });
    }

    // Fetch tax information by state abbreviation
    @Override
    public Tax getTaxByState(String state) {
        Tax tax = taxes.get(state);
        if (tax == null) {
            System.out.println("State not found in in-memory map: " + state);
        }
        return tax;
    }

    // Fetch all tax information
    @Override
    public List<Tax> getAllTaxes() {
        return new ArrayList<>(taxes.values()); // Return all tax values from the in-memory storage
    }

    // Add a new tax entry
    @Override
    public Tax addTax(Tax newTax) {
        if (newTax == null || newTax.getStateAbbreviation() == null || newTax.getStateName() == null) {
            throw new DataPersistenceException("Tax data is invalid.");
        }
        taxes.put(newTax.getStateAbbreviation(), newTax); // Add tax to in-memory storage
        saveTaxesToFile(); // Save the updated taxes back to the file
        return newTax;
    }

    // Update an existing tax entry
    @Override
    public boolean updateTax(Tax updatedTax) {
        if (updatedTax == null || updatedTax.getStateAbbreviation() == null || updatedTax.getStateName() == null) {
            throw new DataPersistenceException("Tax data is invalid.");
        }
        taxes.put(updatedTax.getStateAbbreviation(), updatedTax); // Update tax in in-memory storage
        saveTaxesToFile(); // Save the updated taxes back to the file
        return true;  // Update was successful
    }

    // Remove a tax entry by state abbreviation
    @Override
    public boolean removeTaxByState(String state) {
        if (!taxes.containsKey(state)) {
            return false;  // No tax entry was removed
        }
        taxes.remove(state);
        saveTaxesToFile(); // Save the updated taxes back to the file
        return true;  // Tax entry was removed successfully
    }

    /**
     * Loads tax records from the data file into the in-memory storage.
     *
     * @return A list of tax records read from the file.
     */    private List<Tax> loadTaxesFromFile() {
        taxes.clear(); // Clear the in-memory map before loading
        List<Tax> fileTaxes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine();  // Skip the header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    try {
                        String stateAbbreviation = parts[0].trim();
                        String stateName = parts[1].trim();
                        BigDecimal taxRate = new BigDecimal(parts[2].trim());
                        fileTaxes.add(new Tax(stateAbbreviation, stateName, taxRate)); // Add tax to in-memory storage
                    } catch (NumberFormatException e) {
                        System.err.println("Error converting value to number from line: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException ex) {
            throw new DataPersistenceException("Error reading taxes from file.", ex);
        }
        return fileTaxes;
    }


    /**
     * Writes the in-memory tax records to the data file.
     */
    private void saveTaxesToFile() {
//        backupFile();
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (Tax tax : taxes.values()) {
                writer.write(tax.getStateAbbreviation() + "," + tax.getStateName() + "," + tax.getTaxRate() + "\n");
            }
        } catch (IOException ex) {
            System.err.println("Error writing taxes to file: " + ex.getMessage());
//            restoreBackup();  // if there's an error, restore from backup
        }
    }

    /**
     * Creates a backup of the current tax data file.
     */
//    private void backupFile() {
//        File sourceFile = new File(FILE_PATH);
//        File backupFile = new File(BACKUP_PATH);
//        try (FileInputStream fis = new FileInputStream(sourceFile);
//             FileOutputStream fos = new FileOutputStream(backupFile)) {
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = fis.read(buffer)) != -1) {
//                fos.write(buffer, 0, bytesRead);
//            }
//        } catch (IOException ex) {
//            System.err.println("Error creating backup: " + ex.getMessage());
//        }
//    }

    /**
     * Restores tax data from the backup file.
     */
//    private void restoreBackup() {
//        File backupFile = new File(BACKUP_PATH);
//        File sourceFile = new File(FILE_PATH);
//        try (FileInputStream fis = new FileInputStream(backupFile);
//             FileOutputStream fos = new FileOutputStream(sourceFile)) {
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = fis.read(buffer)) != -1) {
//                fos.write(buffer, 0, bytesRead);
//            }
//        } catch (IOException ex) {
//            System.err.println("Error restoring backup: " + ex.getMessage());
//        }
//    }
}
