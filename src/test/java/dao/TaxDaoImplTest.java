package dao;

import static org.junit.jupiter.api.Assertions.*;

import modelDTO.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class provides unit tests for the TaxDaoImpl.
 * It ensures that basic CRUD operations on the TaxDao implementation work as expected.
 */
public class TaxDaoImplTest {

    private TaxDao taxDao;
    /**
     * This method sets up the test environment. It is run before each test.
     */
    @BeforeEach
    public void setUp() {
        // Create an instance of the TaxDao implementation for testing
        taxDao = new TaxDaoImpl();
    }

    /**
     * Test case to verify fetching a tax by its state abbreviation.
     * It checks if the retrieved tax's attributes match expected values.
     */
    @Test
    public void testGetTaxByState() {
        // Test fetching tax information by state abbreviation
        Tax tax = taxDao.getTaxByState("OH");
        assertNotNull(tax);
        assertEquals("OH", tax.getStateAbbreviation());
        assertEquals("Ohio", tax.getStateName());
        assertEquals(new BigDecimal("7.00"), tax.getTaxRate());
    }

    /**
     * Test case to verify fetching all tax entries.
     * It ensures the retrieved list is not empty.
     */
    @Test
    public void testGetAllTaxes() {
        // Test retrieving all tax information
        List<Tax> taxes = taxDao.getAllTaxes();
        // Assertions to ensure that the list of taxes is not empty
        assertNotNull(taxes);
        assertTrue(taxes.size() > 0);
    }

    /**
     * Test case to verify adding a new tax entry.
     * It checks if the new tax is correctly stored and can be retrieved.
     */
    @Test
    public void testAddTax() {
        // Test adding a new tax entry
        Tax newTax = new Tax("TX", "Texas", new BigDecimal("8.00"));
        taxDao.addTax(newTax);
        // Test fetching the newly added tax
        Tax addedTax = taxDao.getTaxByState("TX");
        // Assertions to verify that the retrieved tax matches the added one
        assertNotNull(addedTax);
        assertEquals("TX", addedTax.getStateAbbreviation());
        assertEquals("Texas", addedTax.getStateName());
        assertEquals(new BigDecimal("8.00"), addedTax.getTaxRate());
    }
    /**
     * Test case to verify updating an existing tax entry.
     * It checks if the modifications on the tax are correctly stored and can be retrieved.
     */
    @Test
    public void testUpdateTax() {
        // Test updating an existing tax entry
        Tax updatedTax = new Tax("OH", "Ohio", new BigDecimal("7.00"));
        assertTrue(taxDao.updateTax(updatedTax));
        // Test fetching the updated tax
        Tax retrievedTax = taxDao.getTaxByState("OH");
        // Assertions to verify that the retrieved tax matches the updated attributes
        assertNotNull(retrievedTax);
        assertEquals("OH", retrievedTax.getStateAbbreviation());
        assertEquals("Ohio", retrievedTax.getStateName());
        assertEquals(new BigDecimal("7.00"), retrievedTax.getTaxRate());
    }
    /**
     * Test case to verify removing a tax entry.
     * It ensures the tax is correctly removed and can't be retrieved afterward.
     */
    @Test
    public void testRemoveTax() {
        // Test removing a tax entry by state abbreviation
        // Assertions to ensure the tax was indeed removed
        assertTrue(taxDao.removeTaxByState("OH"));
        assertNull(taxDao.getTaxByState("OH"));
    }
    
}
