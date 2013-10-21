package hadl.m2.component;

public class NoSuchPortException extends Exception {
    
    private static final long serialVersionUID = 3407953681965408263L;
    
    public NoSuchPortException() {
        super();
    }
    
    public NoSuchPortException(final String message) {
        super(message);
    }
    
    public NoSuchPortException(final Throwable cause) {
        super(cause);
    }
    
    public NoSuchPortException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
