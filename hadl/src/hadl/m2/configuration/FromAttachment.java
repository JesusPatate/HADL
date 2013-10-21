package hadl.m2.configuration;

import hadl.m2.component.RequiredPort;
import hadl.m2.connector.FromRole;


/**
 * Attachment between a component and a connector from role. 
 */
public abstract class FromAttachment extends Attachment {
    
    public FromAttachment(final String label, RequiredPort port, FromRole role) {
        super(label, port, role);
    }
    
}
