package printinghouse.exceptions;

public class PaperCapacityExceeded extends Exception {
    public PaperCapacityExceeded(String message) {
        super(message);
    }

    public PaperCapacityExceeded(String message, Throwable cause) {
        super(message, cause);
    }
}
