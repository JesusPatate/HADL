package hadl.m2.component;

import hadl.m2.Link;
import hadl.m2.Linkable;


/**
 * A service of a component.
 * 
 * <p>
 * Superclass of provided and required service. A service represents a
 * functionality provided or required by a component. It can be dynamically
 * attached to a port in order to be provided to other components (or by another
 * one if it is required).
 * </p>
 * 
 * @see hadl.m2.component.Port
 * @see
 */
public abstract class Service implements Linkable {
    
    protected final String label;
    
    protected Link connection = null;
    
    protected Service(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    @Override
    public void plug(final Link link) {
        this.connection = link;
    }
}
