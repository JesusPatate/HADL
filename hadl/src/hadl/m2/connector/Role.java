package hadl.m2.connector;

import hadl.m2.Interface;


/**
 * A role of a connector.
 */
public abstract class Role extends Interface {
    
    /**
     * Creates a new role.
     * 
     * @param label
     *            Name of the role
     */
    protected Role(final String label) {
        super(label);
    }
    
}
