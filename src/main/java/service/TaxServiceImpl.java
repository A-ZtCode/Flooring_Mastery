package service;

import dao.TaxDao;
import modelDTO.Tax;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service implementation for tax-related operations.
 * This class provides methods to handle various tax-related functionalities like fetching, adding, editing, and validating tax records.
 */
public class TaxServiceImpl implements TaxService {

    private TaxDao taxDao;
    // A mapping from state names to their abbreviations for convenience
    private Map<String, String> stateToAbbreviationMap;
    /**
     * Constructor for the TaxServiceImpl.
     * @param taxDao Data Access Object (DAO) for tax operations.
     */
    public TaxServiceImpl(TaxDao taxDao) {
        this.taxDao = taxDao;
        initializeStateMapping(); // Initialize the map in the constructor
    }
    /**
     * Initializes the state to abbreviation mapping.
     */
    private void initializeStateMapping() {
        stateToAbbreviationMap = new HashMap<>();
        stateToAbbreviationMap.put("Alabama", "AL");
        stateToAbbreviationMap.put("Alaska", "AK");
        stateToAbbreviationMap.put("Arizona", "AZ");
        stateToAbbreviationMap.put("Arkansas", "AR");
        stateToAbbreviationMap.put("California", "CA");
        stateToAbbreviationMap.put("Colorado", "CO");
        stateToAbbreviationMap.put("Connecticut", "CT");
        stateToAbbreviationMap.put("Delaware", "DE");
        stateToAbbreviationMap.put("Florida", "FL");
        stateToAbbreviationMap.put("Georgia", "GA");
        stateToAbbreviationMap.put("Hawaii", "HI");
        stateToAbbreviationMap.put("Idaho", "ID");
        stateToAbbreviationMap.put("Illinois", "IL");
        stateToAbbreviationMap.put("Indiana", "IN");
        stateToAbbreviationMap.put("Iowa", "IA");
        stateToAbbreviationMap.put("Kansas", "KS");
        stateToAbbreviationMap.put("Kentucky", "KY");
        stateToAbbreviationMap.put("Louisiana", "LA");
        stateToAbbreviationMap.put("Maine", "ME");
        stateToAbbreviationMap.put("Maryland", "MD");
        stateToAbbreviationMap.put("Massachusetts", "MA");
        stateToAbbreviationMap.put("Michigan", "MI");
        stateToAbbreviationMap.put("Minnesota", "MN");
        stateToAbbreviationMap.put("Mississippi", "MS");
        stateToAbbreviationMap.put("Missouri", "MO");
        stateToAbbreviationMap.put("Texas", "TX");
    }

    /**
     * Gets the abbreviation for a given state name.
     * If the provided string is already an abbreviation, it's returned as is.
     * @param stateNameOrAbbreviation The state name or its abbreviation.
     * @return The abbreviation for the provided state name, or null if not found.
     */
    private String getStateAbbreviation(String stateNameOrAbbreviation) {
        // If length is 2, treat as abbreviation; otherwise, get abbreviation from mapping
        if (stateNameOrAbbreviation.length() == 2) {
            return stateNameOrAbbreviation;
        }
        return stateToAbbreviationMap.getOrDefault(stateNameOrAbbreviation, null);
    }

    /**
     * Retrieves the tax details for a specified state.
     * The input can either be the full state name or its abbreviation.
     * @param state The name or abbreviation of the state.
     * @return The tax details for the given state or null if not found.
     */
    @Override
    public Tax getTaxByState(String state) {
        String stateAbbreviation = getStateAbbreviation(state);
        if (stateAbbreviation == null) {
            return null; // Return null if state is not found in the mapping.
        }
        return taxDao.getTaxByState(stateAbbreviation);
    }
    /**
     * Retrieves a list of tax details for all states.
     */
    @Override
    public List<Tax> getAllTaxes() {
        // Fetching all tax details from the DAO
        return taxDao.getAllTaxes();
    }

    /**
     * Adds a new tax record.
     * This method validates the tax data before adding it to the DAO.
     * @param tax The tax details to add.
     * @return true if the tax data was added successfully, false otherwise.
     * @throws ServiceException If the tax data is invalid or an error occurs during addition.
     */
    @Override
    public boolean addTax(Tax tax) {
        try {
            // Validating the tax data before adding
            if (validateTaxData(tax)) {
                taxDao.addTax(tax);
                return true; // Returning true after successful addition
            } else {
                throw new ServiceException("Invalid tax data provided.");
            }
        } catch (RuntimeException e) {
            throw new ServiceException("Error while adding tax.", e);
        }
    }

    /**
     * Edits an existing tax record.
     * This method validates the tax data before editing.
     * @param tax The updated tax details.
     * @throws ServiceException If the tax data is invalid or an error occurs during editing.
     */
    @Override
    public void editTax(Tax tax) {
        // Validating the tax data before editing
        if (validateTaxData(tax)) {
            taxDao.updateTax(tax);
        } else {
            throw new ServiceException("Invalid tax data provided.");
        }
    }

    /**
     * Removes tax details for a specified state abbreviation.
     * @param stateAbbreviation The abbreviation of the state for which tax details should be removed.
     * @throws ServiceException If an error occurs during removal.
     */
    @Override
    public void removeTax(String stateAbbreviation) {
        // Removing tax details for a specific state
        taxDao.removeTaxByState(stateAbbreviation);
    }

    /**
     * Validates a given tax record.
     * @param tax The tax record to validate.
     * @return true if the tax data is valid, false otherwise.
     */
    @Override
    public boolean validateTaxData(Tax tax) {
        if (tax == null) return false;

        String abbreviation = tax.getStateAbbreviation();
        BigDecimal rate = tax.getTaxRate();

        // Check if the abbreviation is not null and not too long
        if (abbreviation == null || abbreviation.isEmpty() || abbreviation.length() > 2) return false;

        // Check for valid tax rate (not negative, not over 100%)
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(new BigDecimal("100")) > 0) return false;

        return true;
    }
}
