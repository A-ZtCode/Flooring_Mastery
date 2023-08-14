package dao;

import static org.junit.jupiter.api.Assertions.*;

import modelDTO.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class TaxDaoTest {

    private TaxDao taxDao;

    @BeforeEach
    public void setUp() {
        // Create an instance of the TaxDao implementation for testing
        taxDao = new TaxDaoImpl();
    }

    @Test
    public void testGetTaxByState() {
        // Test fetching tax information by state abbreviation
        Tax tax = taxDao.getTaxByState("OH");
        assertNotNull(tax);
        assertEquals("OH", tax.getStateAbbreviation());
        assertEquals("Ohio", tax.getStateName());
        assertEquals(new BigDecimal("7.00"), tax.getTaxRate());
    }

    @Test
    public void testGetAllTaxes() {
        // Test retrieving all tax information
        List<Tax> taxes = taxDao.getAllTaxes();
        assertNotNull(taxes);
        assertTrue(taxes.size() > 0);
    }

    @Test
    public void testAddTax() {
        // Test adding a new tax entry
        Tax newTax = new Tax("TX", "Texas", new BigDecimal("8.00"));
        taxDao.addTax(newTax);

        Tax addedTax = taxDao.getTaxByState("TX");
        assertNotNull(addedTax);
        assertEquals("TX", addedTax.getStateAbbreviation());
        assertEquals("Texas", addedTax.getStateName());
        assertEquals(new BigDecimal("8.00"), addedTax.getTaxRate());
    }

    @Test
    public void testUpdateTax() {
        // Test updating an existing tax entry
        Tax updatedTax = new Tax("OH", "Ohio", new BigDecimal("7.00"));
        assertTrue(taxDao.updateTax(updatedTax));

        Tax retrievedTax = taxDao.getTaxByState("OH");
        assertNotNull(retrievedTax);
        assertEquals("OH", retrievedTax.getStateAbbreviation());
        assertEquals("Ohio", retrievedTax.getStateName());
        assertEquals(new BigDecimal("7.00"), retrievedTax.getTaxRate());
    }

    @Test
    public void testRemoveTax() {
        // Test removing a tax entry by state abbreviation
        assertTrue(taxDao.removeTaxByState("OH"));
        assertNull(taxDao.getTaxByState("OH"));
    }
    
}
