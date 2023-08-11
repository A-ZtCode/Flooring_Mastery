package dao;

public class DaoException extends RuntimeException {

    // Constructor with only message
    public DaoException(String message) {
        super(message);
    }

    // Constructor with both message and cause
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
