package service;

import dao.TaxDao;
import modelDTO.Tax;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxServiceImpl implements TaxService {

    private TaxDao taxDao;

    // Add the mapping to your TaxServiceImpl:

    private Map<String, String> stateToAbbreviationMap;  // Declare the map
    public TaxServiceImpl(TaxDao taxDao) {
        this.taxDao = taxDao;
        initializeStateMapping(); // Initialize the map in the constructor
    }

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

    private String getStateAbbreviation(String stateNameOrAbbreviation) {
        // If length is 2, treat as abbreviation; otherwise, fetch abbreviation from mapping
        if (stateNameOrAbbreviation.length() == 2) {
            return stateNameOrAbbreviation;
        }
        return stateToAbbreviationMap.getOrDefault(stateNameOrAbbreviation, null);
    }
    @Override
    public Tax getTaxByState(String state) {
        String stateAbbreviation = getStateAbbreviation(state);
        if (stateAbbreviation == null) {
            return null; // Or throw an exception, depending on your requirement
        }
        return taxDao.getTaxByState(stateAbbreviation);
    }

    @Override
    public List<Tax> getAllTaxes() {
        // Fetching all tax details from the DAO
        return taxDao.getAllTaxes();
    }

    @Override
    public boolean addTax(Tax tax) {
        try {
            // Validating the tax data before adding
            if (validateTaxData(tax)) {
                taxDao.addTax(tax);
                return true; // Consider returning true after successful addition
            } else {
                throw new ServiceException("Invalid tax data provided.");
            }
        } catch (RuntimeException e) {
            throw new ServiceException("Error while adding tax.", e);
        }
    }

    @Override
    public void editTax(Tax tax) {
        // Validating the tax data before editing
        if (validateTaxData(tax)) {
            taxDao.updateTax(tax);
        } else {
            throw new ServiceException("Invalid tax data provided.");
        }
    }

    @Override
    public void removeTax(String stateAbbreviation) {
        // Removing tax details for a specific state
        taxDao.removeTaxByState(stateAbbreviation);
    }

    @Override
    public boolean validateTaxData(Tax tax) {
        if (tax == null) return false;

        String abbreviation = tax.getStateAbbreviation();
        BigDecimal rate = tax.getTaxRate(); // Assuming you have a getTaxRate method in the Tax class

        // Check if the abbreviation is not null and not too long
        if (abbreviation == null || abbreviation.isEmpty() || abbreviation.length() > 2) return false;

        // Check for valid tax rate (not negative, not over 100%)
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(new BigDecimal("100")) > 0) return false;

        return true;
    }
}
