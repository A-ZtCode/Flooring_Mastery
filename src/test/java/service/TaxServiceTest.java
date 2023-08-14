package service;

import dao.TaxDao;
import modelDTO.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaxServiceTest {
    /**
     * Mocking the TaxDao to simulate interactions with the database
     */
    @Mock
    private TaxDao taxDao;
    /**
     * Injecting the mocked TaxDao into the TaxService for testing
     */
    @InjectMocks
    private TaxService taxService = new TaxServiceImpl(taxDao);

    /**
     * Initial set-up method to run before each test
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test fetching tax details for a specific state
     */
    @Test
    public void testGetTaxByState() {
        // Create a dummy tax for testing
        Tax dummyTax = new Tax("NY", "New York", new BigDecimal("7.25"));
        // Mock the behaviour of the DAO method
        when(taxDao.getTaxByState("NY")).thenReturn(dummyTax);

        Tax tax = taxService.getTaxByState("NY");

        // Assert that the returned tax is not null and has expected values
        assertNotNull(tax);
        assertEquals("NY", tax.getStateAbbreviation());

        // Verify that the DAO method was called once
        verify(taxDao, times(1)).getTaxByState("NY");
    }

    /**
     * Test fetching all tax records
     */
    @Test
    public void testGetAllTaxes() {
        // Create a dummy tax for testing
        Tax dummyTax = new Tax("NY", "New York", new BigDecimal("7.25"));
        // Mock the behaviour of the DAO method
        when(taxDao.getAllTaxes()).thenReturn(Arrays.asList(dummyTax));

        List<Tax> taxes = taxService.getAllTaxes();

        // Assert that the returned list has the expected size and values
        assertEquals(1, taxes.size());
        assertEquals("NY", taxes.get(0).getStateAbbreviation());
        // Verify that the DAO method was called once
        verify(taxDao, times(1)).getAllTaxes();
    }

    /**
     * Test adding a valid tax record
     */
    @Test
    public void testAddValidTax() {
        // Create a valid tax for testing
        Tax validTax = new Tax("TX", "Texas", new BigDecimal("6.75"));
        // Mock the behaviour of the DAO method to do nothing
        doReturn(true).when(taxDao).addTax(validTax);

        boolean result = taxService.addTax(validTax);
        assertTrue(result);
        // Verify that the DAO method was called once with the expected parameter
        verify(taxDao, times(1)).addTax(validTax);
    }


    /**
     * Test adding an invalid tax record (empty state abbreviation)
     */
    @Test
    public void testAddInvalidTax() {
        // Create an invalid tax for testing (empty state abbreviation)
        Tax invalidTax = new Tax("", "Unknown", new BigDecimal("6.75"));

        // Assert that the service method throws an exception with the expected message
        Exception exception = assertThrows(ServiceException.class, () -> {
            taxService.addTax(invalidTax);
        });

        String expectedMessage = "Invalid tax data provided.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test editing a valid tax record
     */
    @Test
    public void testEditValidTax() {
        // Create a valid tax for testing
        Tax validTax = new Tax("TX", "Texas", new BigDecimal("6.50"));
        // Mock the behaviour of the DAO method to do nothing
        doNothing().when(taxDao).updateTax(validTax);

        taxService.editTax(validTax);

        // Verify that the DAO method was called once with the expected parameter
        verify(taxDao, times(1)).updateTax(validTax);
    }


    /**
     * Test exception handling when there's a DAO exception during fetching tax by state
     */
    @Test
    public void testDaoExceptionWhenFetchingByState() {
        // Mock the behaviour of the DAO method to throw an exception
        when(taxDao.getTaxByState("TX")).thenThrow(new RuntimeException("Database error"));

        // Assert that the service method throws an exception with the expected message
        Exception exception = assertThrows(ServiceException.class, () -> {
            taxService.getTaxByState("TX");
        });

        String expectedMessage = "Error fetching tax details.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test exception handling when there's a DAO exception during fetching all taxes
     */
    @Test
    public void testDaoExceptionWhenFetchingAll() {
        // Mock the behaviour of the DAO method to throw an exception
        when(taxDao.getAllTaxes()).thenThrow(new RuntimeException("Database error"));

        // Assert that the service method throws an exception with the expected message
        Exception exception = assertThrows(ServiceException.class, () -> {
            taxService.getAllTaxes();
        });

        String expectedMessage = "Error fetching all tax details.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test exception handling when there's a DAO exception during adding a tax
     */
    @Test
    public void testDaoExceptionWhenAdding() {
        // Create a valid tax for testing
        Tax validTax = new Tax("TX", "Texas", new BigDecimal("6.75"));
        // Mock the behaviour of the DAO method to throw an exception
        doThrow(new RuntimeException("Database error")).when(taxDao).addTax(validTax);

        // Assert that the service method throws an exception with the expected message
        Exception exception = assertThrows(ServiceException.class, () -> {
            taxService.addTax(validTax);
        });

        String expectedMessage = "Error adding tax details.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test exception handling when there's a DAO exception during editing a tax
     */
    @Test
    public void testDaoExceptionWhenEditing() {
        // Create a valid tax for testing
        Tax validTax = new Tax("TX", "Texas", new BigDecimal("6.50"));
        // Mock the behaviour of the DAO method to throw an exception
        doThrow(new RuntimeException("Database error")).when(taxDao).updateTax(validTax);

        // Assert that the service method throws an exception with the expected message
        Exception exception = assertThrows(ServiceException.class, () -> {
            taxService.editTax(validTax);
        });

        String expectedMessage = "Error editing tax details.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test exception handling when there's a DAO exception during removing a tax
     */
    @Test
    public void testDaoExceptionWhenRemoving() {
        // Mock the behaviour of the DAO method to throw an exception
        doThrow(new RuntimeException("Database error")).when(taxDao).removeTaxByState("TX");

        // Assert that the service method throws an exception with the expected message
        Exception exception = assertThrows(ServiceException.class, () -> {
            taxService.removeTax("TX");
        });

        String expectedMessage = "Error removing tax details.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    /**
     * Test adding a tax record for a state that already exists in the database
     */

    @Test
    public void testAddTaxForExistingState() {
        // Create a tax for an existing state for testing
        Tax existingTax = new Tax("TX", "Texas", new BigDecimal("6.75"));
        // Mock the behaviour of the DAO method to throw an exception
        when(taxDao.addTax(existingTax)).thenThrow(new RuntimeException("Database error: Duplicate entry"));

        // Assert that the service method throws an exception with the expected message
        Exception exception = assertThrows(ServiceException.class, () -> {
            taxService.addTax(existingTax);
        });

        String expectedMessage = "Error adding tax details.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}

