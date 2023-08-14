package service;

import dao.TaxDao;
import modelDTO.Tax;

import java.math.BigDecimal;
import java.util.List;

public class TaxServiceImpl implements TaxService {

    private TaxDao taxDao;

    public TaxServiceImpl(TaxDao taxDao) {
        this.taxDao = taxDao;
    }

    @Override
    public Tax getTaxByState(String state) {
        // Fetching tax details for a specific state from the DAO
        return taxDao.getTaxByState(state);
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
