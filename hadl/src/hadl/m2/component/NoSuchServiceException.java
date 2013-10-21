package hadl.m2.component;

public class NoSuchServiceException extends Exception {
    
    private static final long serialVersionUID = 7612296360124671L;
    
    public NoSuchServiceException() {
        super();
    }
    
    public NoSuchServiceException(final String message) {
        super(message);
    }
    
    public NoSuchServiceException(final Throwable cause) {
        super(cause);
    }
    
    public NoSuchServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
