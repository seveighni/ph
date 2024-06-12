package printinghouse.exceptions;

public class PageSizeNotSupported extends Exception {
    public PageSizeNotSupported(String message) {
        super(message);
    }

    public PageSizeNotSupported(String message, Throwable cause) {
        super(message, cause);
    }
}
