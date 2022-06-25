package pl.sda.trivia.error;

public class DataNotAvailableException extends RuntimeException {
    public DataNotAvailableException(String message) {
        super(message);
    }
}
