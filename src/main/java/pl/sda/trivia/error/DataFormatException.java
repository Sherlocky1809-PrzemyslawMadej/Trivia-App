package pl.sda.trivia.error;

public class DataFormatException extends RuntimeException {
    public DataFormatException(String message) {
        super(message);
    }
}
