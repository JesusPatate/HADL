package hadl.m2.component;

import hadl.m2.Link;


/**
 * A connection between a service and a port of a component.
 */
public class Connection extends Link {
    
    private final String label;
    
    private final Service service;
    
    private final Port port;
    
    protected Connection(final String label, final Service service,
            final Port port) {
    	
        super(service, port);
        this.label = label;
        this.service = service;
        this.port = port;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public Service getConnectedService() {
        return this.service;
    }
    
    public Port getConnectedPort() {
        return this.port;
    }
}
