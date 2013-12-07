package fr.univnantes.alma.hadl.m2.configuration;


public class IllegalAttachmentException extends Exception {
    
    private static final long serialVersionUID = -4649119410826047523L;

    public IllegalAttachmentException(String message) {
        super(message);
    }
    
    public IllegalAttachmentException(Throwable cause) {
        super(cause);
    }
    
    public IllegalAttachmentException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public IllegalAttachmentException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
