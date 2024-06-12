package printinghouse.exceptions;

public class PrintColorNotSupported extends Exception{
    public PrintColorNotSupported(String message) {
        super(message);
    }

    public PrintColorNotSupported(String message, Throwable cause) {
        super(message, cause);
    }
}
