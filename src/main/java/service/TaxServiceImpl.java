package service;

import dao.TaxDao;
import modelDTO.Tax;

import java.util.List;

public class TaxServiceImpl implements TaxService {
    private TaxDao taxDao;
    // Dependencies are injected, either through a constructor or setters.
    public TaxServiceImpl(TaxDao taxDao) {
        this.taxDao = taxDao;
    }

    @Override
    public Tax getTaxByState(String state) {
        return taxDao.getTaxByState(state);
    }

    @Override
    public List<Tax> getAllTaxes() {
        return null;
    }

    @Override
    public void addTax(Tax tax) {
    }


    @Override
    public void editTax(Tax tax) {

    }

    @Override
    public void removeTax(String stateAbbreviation) {

    }

    @Override
    public boolean validateTaxData(Tax tax) {
        return false;
    }
}
