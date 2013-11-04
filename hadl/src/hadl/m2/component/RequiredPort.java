package hadl.m2.component;

import hadl.m2.Link;
import hadl.m2.configuration.FromAttachment;
import hadl.m2.configuration.CompConfRequiredBinding;


public abstract class RequiredPort extends Port {
    
    protected RequiredConnection connection = null;
    
    protected FromAttachment attachment = null;
    
    protected CompConfRequiredBinding binding = null;
    
    public RequiredPort(final String label) {
        super(label);
    }
    
    @Override
    public void plug(final Link link) {
        if (link instanceof RequiredConnection) {
            this.connection = (RequiredConnection) link;
        }
        else if (link instanceof FromAttachment) {
            this.attachment = (FromAttachment) link;
        }
        else if (link instanceof CompConfRequiredBinding) {
            this.binding = (CompConfRequiredBinding) link;
        }
    }
}
