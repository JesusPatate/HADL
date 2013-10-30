package hadl.m2.connector;

import hadl.m2.Link;

/**
 * A requestor role of a connector.
 */
public abstract class ToRole extends Role {
    
    protected Link attachment = null;
    
    /**
     * Creates a new to role.
     * 
     * @param label
     *            Name of the role.
     */
    public ToRole(final String label) {
        super(label);
    }
    
    @Override
    public void plug(Link link) {
        this.attachment = link;
    }
}
