package hadl.m2.connector;

import hadl.m2.Link;


/**
 * Provider role of a connector.
 */
public abstract class FromRole extends Role {
    
    protected Link attachment = null;
    
    /**
     * Creates a new from role.
     * 
     * @param label
     *            Name of the role
     */
    public FromRole(final String label) {
        super(label);
    }
    
    @Override
    public void plug(Link link) {
        this.attachment = link;
    }
}
