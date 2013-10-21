package hadl.m2.configuration;

import hadl.m2.component.ProvidedPort;
import hadl.m2.connector.ToRole;


/**
 * Attachment between a component and a connector to role. 
 */
public abstract class ToAttachment extends Attachment {
    
    public ToAttachment(final String label, ProvidedPort port, ToRole role) {
        super(label, port, role);
    }
    
}