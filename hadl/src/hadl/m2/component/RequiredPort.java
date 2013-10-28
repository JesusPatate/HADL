package hadl.m2.component;

import hadl.m2.Link;
import hadl.m2.configuration.FromAttachment;


public abstract class RequiredPort extends Port {
    
    protected Link connection = null;
    
    protected Link attachment = null;
    
    public RequiredPort(final String label) {
        super(label);
    }
    
    @Override
    public void plug(final Link link) {
        if (link instanceof RequiredConnection) {
            this.connection = link;
        }
        else if (link instanceof FromAttachment) {
            this.attachment = link;
        }
    }
}
