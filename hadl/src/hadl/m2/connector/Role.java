package hadl.m2.connector;

import hadl.m2.Interface;
import hadl.m2.Link;


/**
 * A role of a connector.
 */
public abstract class Role extends Interface {
    
    protected Link attachment = null;
    
    /**
     * Creates a new role.
     * 
     * @param label
     *            Name of the role
     */
    protected Role(final String label) {
        super(label);
    }
    
    @Override
    public void plug(Link link) {
        this.attachment = link;
    }
    
}
