package modelDTO;

import java.math.BigDecimal;

/**
 * The Tax class represents the tax rate associated with a specific state.
 *
 * Each state, identified by both its name and abbreviation, has a corresponding tax rate.
 * This class provides methods to get and set these attributes, and also a toString method
 * for a formatted output.
 */
public class Tax {

    // The abbreviation of the state (e.g., "NY", "CA", etc.)
    private String stateAbbreviation;

    // The full name of the state (e.g., "New York", "California", etc.)
    private String stateName;

    // The tax rate associated with the state
    private BigDecimal taxRate;

    /**
     * Constructor that initializes the state's abbreviation, name, and associated tax rate.
     *
     * @param stateAbbreviation Abbreviation of the state.
     * @param stateName Full name of the state.
     * @param taxRate Tax rate associated with the state.
     */
    public Tax(String stateAbbreviation, String stateName, BigDecimal taxRate) {
        this.stateAbbreviation = stateAbbreviation;
        this.stateName = stateName;
        this.taxRate = taxRate;
    }

    // Getters and setters for all the attributes.
    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * Provides a formatted string representation of the Tax object.
     *
     * @return A string in the format: "State: [stateAbbreviation], Tax Rate: [taxRate]%".
     */
    @Override
    public String toString() {
        return "State: " + this.getStateAbbreviation() + ", Tax Rate: " + this.getTaxRate() + "%";
    }
}
