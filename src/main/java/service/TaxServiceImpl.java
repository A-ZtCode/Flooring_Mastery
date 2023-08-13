package service;

import dao.TaxDao;
import modelDTO.Tax;
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
    public void addTax(Tax tax) {
        // Validating the tax data before adding
        if (validateTaxData(tax)) {
            taxDao.addTax(tax);
        } else {
            throw new ServiceException("Invalid tax data provided.");
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
        // Basic validation: Ensure the tax and its state abbreviation are not null
        return tax != null && tax.getStateAbbreviation() != null && !tax.getStateAbbreviation().isEmpty();
    }
}
