package service;

/**
 * The OrderNotFoundException class is a custom exception designed to handle scenarios
 * where an order is not found in the system.
 */
public class OrderNotFoundException extends Exception {

    /**
     * Constructor to create a new instance of the OrderNotFoundException with a specific message.
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}
