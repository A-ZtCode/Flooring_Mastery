package dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dao.TaxDao;
import modelDTO.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class TaxDaoImplTest {

    private TaxDao taxDao;

    @BeforeEach
    public void setUp() {
        taxDao = new TaxDaoImpl();
    }

    @Test
    public void testGetTaxByState() {
        Tax tax = taxDao.getTaxByState("OH");
        assertNotNull(tax);
        assertEquals("OH", tax.getStateAbbreviation());
        assertEquals("Ohio", tax.getStateName());
        assertEquals(new BigDecimal("6.25"), tax.getTaxRate());
    }

    @Test
    public void testGetAllTaxes() {
        List<Tax> taxes = taxDao.getAllTaxes();
        assertNotNull(taxes);
        assertTrue(taxes.size() > 0);
    }

    @Test
    public void testAddTax() {
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
        assertTrue(taxDao.removeTaxByState("PA"));
        assertNull(taxDao.getTaxByState("PA"));
    }

    // Add more test cases as needed

}
