package hadl.m2.component;


/**
 * A connection between required service and port.
 */
class RequiredConnection extends Connection {
    
    /**
     * Connected service
     */
    private final RequiredService service;
    
    /**
     * Connected port
     */
    private final RequiredPort port;
    
    public RequiredConnection(final String label,
            final RequiredService service, final RequiredPort port)
            throws NoSuchServiceException, NoSuchPortException {
        
        super(label);
        
        this.service = service;
        this.port = port;
    }
    
    public RequiredService getConnectedService() {
        return this.service;
    }
    
    public RequiredPort getConnectedPort() {
        return this.port;
    }
}
