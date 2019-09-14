package api ;

public class DocumentException extends Exception {

    public DocumentException(String message) {
        super(message);
    }

    public DocumentException(String message, Throwable throwable) {
        super(message, throwable);
    }

}