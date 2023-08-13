package dao;

public class DataPersistenceException extends RuntimeException {

    // Constructor with only message
    public DataPersistenceException(String message) {
        super(message);
    }

    // Constructor with both message and cause
    public DataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
