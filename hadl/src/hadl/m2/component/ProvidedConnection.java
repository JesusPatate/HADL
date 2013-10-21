package hadl.m2.component;

/**
 * A connection between provided service and port.
 */
class ProvidedConnection extends Connection {
    
    /**
     * Connected service
     */
    private final ProvidedService service;
    
    /**
     * Connected port
     */
    private final ProvidedPort port;
    
    public ProvidedConnection(final String label,
            final ProvidedService service, final ProvidedPort port) {
        
        super(label, service, port);
        this.service = service;
        this.port = port;
    }
    
    public ProvidedService getConnectedService() {
        return this.service;
    }
    
    public ProvidedPort getConnectedPort() {
        return this.port;
    }
}
