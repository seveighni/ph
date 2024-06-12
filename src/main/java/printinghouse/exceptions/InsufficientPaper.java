package printinghouse.exceptions;

public class InsufficientPaper extends Exception{
    public InsufficientPaper(String message) {
        super(message);
    }

    public InsufficientPaper(String message, Throwable cause) {
        super(message, cause);
    }
}
