package ir.maktab.onlineexam.exeption;

public final class ProfileAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public ProfileAlreadyExistException() {
        super();
    }

    public ProfileAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ProfileAlreadyExistException(final String message) {
        super(message);
    }

    public ProfileAlreadyExistException(final Throwable cause) {
        super(cause);
    }

}