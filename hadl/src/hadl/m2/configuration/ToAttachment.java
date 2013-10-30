package hadl.m2.configuration;

import hadl.m2.component.ProvidedPort;
import hadl.m2.connector.ToRole;


/**
 * Attachment between a component and a connector to role.
 */
public class ToAttachment extends Attachment {
    
    public ToAttachment(final String label, final ProvidedPort port,
            final ToRole role) {
        
        super(label, port, role);
    }
    
}
