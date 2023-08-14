package dao;

import modelDTO.Tax;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class TaxDaoImpl implements TaxDao {

    private final Map<String, Tax> taxes = new HashMap<>(); // in-memory storage
    private final String FILE_PATH = "src/main/java/Taxes.txt"; // Name of the file that contains tax data
    private final String BACKUP_PATH = FILE_PATH + ".bak"; // Backup

    public TaxDaoImpl() {
        // Load taxes into the in-memory map during instantiation
        loadTaxesFromFile().forEach(tax -> taxes.put(tax.getStateAbbreviation(), tax));
    }

    // Fetch tax information by state abbreviation
    @Override
    public Tax getTaxByState(String state) {
        loadTaxesFromFile(); // Load taxes from the file to the in-memory storage
        return taxes.get(state); // Fetch the tax from the in-memory storage
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

    // Helper method to load taxes from the file to in-memory storage
    private List<Tax> loadTaxesFromFile() {
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

    // Helper method to save taxes to the file
    private void saveTaxesToFile() {
        backupFile();
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (Tax tax : taxes.values()) {
                writer.write(tax.getStateAbbreviation() + "," + tax.getStateName() + "," + tax.getTaxRate() + "\n");
            }
        } catch (IOException ex) {
            System.err.println("Error writing taxes to file: " + ex.getMessage());
            restoreBackup();  // if there's an error, restore from backup
        }
    }

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
            System.err.println("Error creating backup: " + ex.getMessage());
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
            System.err.println("Error restoring backup: " + ex.getMessage());
        }
    }
}
