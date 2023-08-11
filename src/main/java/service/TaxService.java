package service;

import modelDTO.Tax;
import java.util.List;

public interface TaxService {

    public  Tax getTaxByState(String state); // Get tax details for a specific state
    List<Tax> getAllTaxes(); // Get a list of all tax rates for all states

    // Advanced tax operations (if needed in the future)
    void addTax(Tax tax);
    void editTax(Tax tax);
    void removeTax(String stateAbbreviation);
    boolean validateTaxData(Tax tax);
}