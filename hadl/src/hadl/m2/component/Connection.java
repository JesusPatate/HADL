package hadl.m2.component;

import hadl.m2.Link;


/**
 * A connection between a service and a port of a component.
 */
abstract class Connection extends Link {
    
    private final String label;
    
    protected Connection(final String label, final Service service,
            final Port port) {
        super(service, port);
        
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
}
