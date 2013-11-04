package hadl.m2.component;

import hadl.m2.Interface;
import hadl.m2.Link;
import hadl.m2.configuration.Attachment;
import hadl.m2.configuration.CompConfBinding;

import java.util.HashSet;
import java.util.Set;


public abstract class Port extends Interface {
    
	protected final Set<Connection> connections = new HashSet<Connection>();
	
    protected Attachment attachment = null;
    
    protected CompConfBinding binding = null;
    
    public Port(final String label) {
        super(label);
    }
    
    @Override
    public void plug(final Link link) {
        if (link instanceof Connection) {
            this.connections.add((Connection) link);
        }
        else if (link instanceof Attachment) {
            this.attachment = (Attachment) link;
        }
        else if(link instanceof CompConfBinding) {
            this.binding = (CompConfBinding) link;
        }
    }
}
