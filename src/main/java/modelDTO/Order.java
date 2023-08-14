package modelDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The Order class represents a customer's order, detailing the product they purchased,
 * the tax rate applied, and various costs related to the purchase.
 *
 * The order has a unique order number, customer details, and details about the product
 * they purchased, including the type of product, its area, and various costs.
 */
public class Order {
    // The unique identifier for the order
    private Integer orderNumber;
    // The name of the customer placing the order
    private String customerName;
    // The state for tax purposes
    private String state;
    // Tax rate applicable for the state
    private BigDecimal taxRate;
    // Type of product ordered
    private String productType;
    // Area of the product ordered (typically in square feet or similar unit)
    private BigDecimal area;
    // Cost of the product per unit area
    private BigDecimal costPerSquareFoot;
    // Labor cost for installing the product per unit area
    private BigDecimal laborCostPerSquareFoot;
    // Total material cost
    private BigDecimal materialCost;
    // Total labor cost
    private BigDecimal laborCost;
    // Total tax amount
    private BigDecimal tax;
    // Total order amount including tax
    private BigDecimal total;
    // Date of the order
    private Date orderDate;

    /**
     * Constructor that initializes all attributes of the order.
     */

    public Order(Integer orderNumber, String customerName, String state, BigDecimal taxRate, String productType, BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total, Date orderDate) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate;
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
        this.orderDate = orderDate;
    }

    //    Getters, and Setters for all attributes
    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
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

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Calculates the material cost based on area and cost per square foot.
     *
     * @return the calculated material cost.
     */
    public BigDecimal calculateMaterialCost() {
        return area.multiply(costPerSquareFoot);
    }

    /**
     * Calculates the labor cost based on area and labor cost per square foot.
     *
     * @return the calculated labor cost.
     */
    public BigDecimal calculateLaborCost() {
        return area.multiply(laborCostPerSquareFoot);
    }

    /**
     * Calculates the tax amount based on the total cost and the tax rate.
     *
     * @param tax the tax rate applicable.
     * @return the calculated tax amount.
     */
    public BigDecimal calculateTax(Tax tax) {
        BigDecimal sum = calculateMaterialCost().add(calculateLaborCost());
        return sum.multiply(tax.getTaxRate().divide(new BigDecimal("100")));
    }

    /**
     * Calculates the total cost, including material, labor, and tax.
     *
     * @param tax the tax rate applicable.
     * @return the calculated total cost.
     */
    public BigDecimal calculateTotal(Tax tax) {
        return calculateMaterialCost().add(calculateLaborCost()).add(calculateTax(tax));
    }

    /**
     * Default constructor.
     */
    public Order() {
        // This is a default constructor
    }
}