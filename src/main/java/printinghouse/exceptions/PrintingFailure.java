package printinghouse.exceptions;

public class PrintingFailure extends Exception{
    public PrintingFailure(String message) {
        super(message);
    }

    public PrintingFailure(String message, Throwable cause) {
        super(message, cause);
    }
}
