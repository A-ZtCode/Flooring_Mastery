package dao;

import modelDTO.Tax;
import java.util.List;

// Tax DAO Interface
public interface TaxDao {

    /**
     * Retrieves a tax record by its state abbreviation.
     *
     * @param state The state abbreviation.
     * @return The tax record for the given state, or null if not found.
     */
    Tax getTaxByState(String state);

    /**
     * Retrieves all tax records.
     *
     * @return A list of all tax records.
     */
    List<Tax> getAllTaxes();

    /**
     * Adds a new tax record.
     *
     * @param tax The tax record to be added.
     * @return The added tax record, with any generated ID.
     */
    Tax addTax(Tax tax);

    /**
     * Updates an existing tax record.
     *
     * @param tax The tax record with updated information.
     * @return true if the update was successful, false otherwise.
     */
    boolean updateTax(Tax tax);

    /**
     * Removes a tax record based on state abbreviation.
     *
     * @param state The state abbreviation of the tax record to be removed.
     * @return true if the deletion was successful, false otherwise.
     */
    boolean removeTaxByState(String state);
}
