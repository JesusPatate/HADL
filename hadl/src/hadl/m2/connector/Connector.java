package hadl.m2.connector;

import hadl.m2.ArchitecturalElement;

import java.util.HashMap;
import java.util.Map;


/**
 * A connector of a software architecture.
 * 
 * <p>
 * A connector models a specific communication mechanism enabling interaction
 * between components.
 * </p>
 */
public abstract class Connector extends ArchitecturalElement {
    
    private final Map<String, Role> roles = new HashMap<String, Role>();
    
    /**
     * Creates a new empty connector.
     * 
     * @param label
     *            Name of the connector
     */
    public Connector(final String label) {
        super(label);
    }
    
    public Map<String, Role> getRoles() {
        return new HashMap<String, Role>(this.roles);
    }
    
    protected Role addRole(final Role role) {
        return this.roles.put(role.getLabel(), role);
    }
}
