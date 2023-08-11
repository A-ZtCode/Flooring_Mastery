package dao;

import modelDTO.Tax;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TaxDaoImpl implements TaxDao {

    private final Map<String, Tax> taxes = new HashMap<>(); // in-memory storage
    private final String FILE_PATH = "taxes.txt"; // Name of the file that contains tax data


    @Override
    public Tax getTaxByState(String state) {
        List<Tax> taxes = loadTaxesFromFile();
        for (Tax tax : taxes) {
            if (tax.getStateAbbreviation().equals(state)) {
                return tax;
            }
        }
        return null; // Return null if no matching tax is found
    }

    @Override
    public List<Tax> getAllTaxes() {
        return loadTaxesFromFile();
    }


    @Override
    public Tax addTax(Tax newTax) {
        List<Tax> taxes = loadTaxesFromFile();

        // Check if the tax entry for the provided state already exists
        for (Tax tax : taxes) {
            if (tax.getStateAbbreviation().equals(newTax.getStateAbbreviation())) {
                throw new DaoException("Tax for the state " + newTax.getStateAbbreviation() + " already exists!");
            }
        }

        // If not, add it to the list
        taxes.add(newTax);

        // Then, save the updated list back to the file
        saveTaxesToFile(taxes);
        return newTax;
    }
    @Override
    public boolean updateTax(Tax updatedTax) {
        List<Tax> taxes = loadTaxesFromFile();
        int index = -1;

        // Check if the tax entry for the provided state exists
        for (int i = 0; i < taxes.size(); i++) {
            if (taxes.get(i).getStateAbbreviation().equals(updatedTax.getStateAbbreviation())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;  // Tax entry not found, return false
        }

        // Update the tax details
        taxes.set(index, updatedTax);

        // Then, save the updated list back to the file
        saveTaxesToFile(taxes);
        return true;  // Update was successful, return true
    }


    @Override
    public boolean removeTaxByState(String state) {
        List<Tax> taxes = loadTaxesFromFile();

        // Use Java 8 streams to filter out the tax entry for the provided state
        List<Tax> updatedTaxes = taxes.stream()
                .filter(tax -> !tax.getStateAbbreviation().equals(state))
                .collect(Collectors.toList());

        if (taxes.size() == updatedTaxes.size()) {
            return false;  // No tax entry was removed
        }

        // Save the updated list without the removed tax entry back to the file
        saveTaxesToFile(updatedTaxes);
        return true;  // Tax entry was removed successfully
    }


    // Helper method to load taxes from the file
    private List<Tax> loadTaxesFromFile() {
        List<Tax> taxes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String stateAbbreviation = parts[0].trim();
                    String stateName = parts[1].trim();
                    BigDecimal taxRate = new BigDecimal(parts[2].trim());
                    taxes.add(new Tax(stateAbbreviation, stateName, taxRate));
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading taxes from file: " + ex.getMessage());
        }
        return taxes;
    }

    // Helper method to save taxes to the file
    private void saveTaxesToFile(List<Tax> taxes) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (Tax tax : taxes) {
                writer.write(tax.getStateAbbreviation() + "," + tax.getStateName() + "," + tax.getTaxRate() + "\n");
            }
        } catch (IOException ex) {
            System.err.println("Error writing taxes to file: " + ex.getMessage());
        }
    }
}
