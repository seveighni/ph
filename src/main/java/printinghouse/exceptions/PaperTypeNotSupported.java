package printinghouse.exceptions;

public class PaperTypeNotSupported extends Exception{
    public PaperTypeNotSupported(String message) {
        super(message);
    }

    public PaperTypeNotSupported(String message, Throwable cause) {
        super(message, cause);
    }
}
