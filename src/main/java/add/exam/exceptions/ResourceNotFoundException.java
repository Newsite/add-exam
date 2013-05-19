package add.exam.exceptions;

/**
 * Exception for missing resources.
 *
 */
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Exception ex) {
        super(message, ex);
    }
}
