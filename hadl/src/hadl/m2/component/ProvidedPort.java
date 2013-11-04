package hadl.m2.component;

import hadl.m2.Link;
import hadl.m2.configuration.CompConfProvidedBinding;
import hadl.m2.configuration.ToAttachment;


public abstract class ProvidedPort extends Port {
    
    protected ProvidedConnection connection = null;
    
    protected ToAttachment attachment = null;
    
    protected CompConfProvidedBinding binding = null;
    
    public ProvidedPort(final String label) {
        super(label);
    }
    
    @Override
    public void plug(final Link link) {
        if (link instanceof ProvidedConnection) {
            this.connection = (ProvidedConnection) link;
        }
        else if (link instanceof ToAttachment) {
            this.attachment = (ToAttachment) link;
        }
        else if(link instanceof CompConfProvidedBinding) {
            this.binding = (CompConfProvidedBinding) link;
        }
    }
}
