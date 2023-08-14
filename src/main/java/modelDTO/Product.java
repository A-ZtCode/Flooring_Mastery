package modelDTO;

import java.math.BigDecimal;

/**
 * The Product class represents a specific type of product available for purchase, detailing its type and associated costs.
 *
 * Each product is defined by its type (e.g., "Wood", "Tile", etc.) and has associated costs for the product material itself (cost per square foot) and the labor cost for installing
 * the product (labor cost per square foot).
 */
public class Product {
    // The type of product (e.g., "Wood", "Tile", etc.)
    private String productType;
    // Cost of the product material per unit area (e.g., per square foot)
    private BigDecimal costPerSquareFoot;
    // Labor cost for installing the product per unit area (e.g., per square foot)
    private BigDecimal laborCostPerSquareFoot;

    /**
     * Constructor that initializes the product type and associated costs.
     *
     * @param productType The type of the product.
     * @param costPerSquareFoot Cost of the product material per unit area.
     * @param laborCostPerSquareFoot Labor cost for installing the product per unit area.
     */
    public Product(String productType, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot) {
        this.productType = productType;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    // Getters and setters for all the attributes.
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }
}
