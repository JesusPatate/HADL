package hadl.m2.component;

import hadl.m2.Link;
import hadl.m2.configuration.ToAttachment;


public abstract class ProvidedPort extends Port {
    
    protected Link connection = null;
    
    protected Link attachment = null;
    
    public ProvidedPort(final String label) {
        super(label);
    }
    
    @Override
    public void plug(final Link link) {
        if(link instanceof ProvidedConnection) {
            this.connection = link;
        }
        else if (link instanceof ToAttachment){
            this.attachment = link;
        }
    }
}
