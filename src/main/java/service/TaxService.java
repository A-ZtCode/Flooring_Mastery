package service;

import modelDTO.Tax;
import java.util.List;

/**
 * Interface for the TaxService which provides methods to handle various tax-related operations.
 */
public interface TaxService {

    /**
     * Retrieves the tax details for a specific state.
     * @param state The name or abbreviation of the state.
     * @return The tax details for the provided state.
     */
    Tax getTaxByState(String state);

    /**
     * Retrieves a list of tax rates for all states.
     * @return A list containing tax rates for all states.
     */
    List<Tax> getAllTaxes();

    /**
     * Adds a new tax record.
     * @param tax The tax record to be added.
     * @return true if the tax was added successfully, false otherwise.
     */
    boolean addTax(Tax tax);

    /**
     * Edits an existing tax record.
     * @param tax The tax record with updated details.
     */
    void editTax(Tax tax);

    /**
     * Removes a tax record based on the state abbreviation.
     * @param stateAbbreviation The state abbreviation for which the tax record is to be removed.
     */
    void removeTax(String stateAbbreviation);

    /**
     * Validates the provided tax data to ensure its correctness.
     * @param tax The tax record to be validated.
     * @return true if the tax data is valid, false otherwise.
     */
    boolean validateTaxData(Tax tax);
}
