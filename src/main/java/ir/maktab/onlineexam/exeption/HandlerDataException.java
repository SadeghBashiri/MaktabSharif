package ir.maktab.onlineexam.exeption;

public final class HandlerDataException extends RuntimeException {

    public HandlerDataException(String message) {
        super(message);
    }

    public HandlerDataException(String message, Throwable cause) {
        super(message, cause);
    }
}