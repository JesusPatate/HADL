package hadl.m2.configuration;

import hadl.m2.Link;
import hadl.m2.component.Port;
import hadl.m2.connector.Role;


/**
 * An attachment link of a configuration.
 */
public abstract class Attachment extends Link {
    
    private final String label;
    
    public Attachment(final String label, final Port port, final Role role) {
        super(port, role);
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
}
