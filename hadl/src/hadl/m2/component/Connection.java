package hadl.m2.component;

import hadl.m2.Link;
import hadl.m2.Linkable;

/**
 * A connection between a service and a port of a component.
 */
abstract class Connection extends Link{
    
    private final String label;
    
    protected Connection(final String label, Service service, Port port) {
        super((Linkable) service, (Linkable) port);
        
    	this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
}
