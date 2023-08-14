package service;

/**
 * ServiceException is thrown when there's a domain-specific error in the service layer.
 */
public class ServiceException extends RuntimeException {

    /**
     * Constructs a new ServiceException with the specified detail message.
     * @param message The detail message.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new ServiceException with the specified detail message and cause.
     * @param message The detail message.
     * @param cause The cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ServiceException with the specified cause.
     * @param cause The cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
