package hadl.m2.connector;

import hadl.m2.ArchitecturalElement;

import java.util.HashMap;
import java.util.Map;


/**
 * A connector of a software architecture.
 * 
 * <p>
 * A connector models a specific communication mechanism enabling
 * interaction between components.
 * </p>
 */
public abstract class Connector extends ArchitecturalElement {
    
    private final Map<String, FromRole> fromRoles =
            new HashMap<String, FromRole>();
    
    private final Map<String, ToRole> toRoles = new HashMap<String, ToRole>();
    
    /**
     * Creates a new empty connector.
     * 
     * @param label
     *            Name of the connector
     */
    public Connector(final String label) {
        super(label);
    }
    
    public FromRole getFromRole(final String label) {
        return this.fromRoles.get(label);
    }
    
    public ToRole getToRole(final String label) {
        return this.toRoles.get(label);
    }
    
    public Map<String, FromRole> getFromRoles() {
        return new HashMap<String, FromRole>(this.fromRoles);
    }
    
    public Map<String, ToRole> getToRoles() {
        return new HashMap<String, ToRole>(this.toRoles);
    }
    
    public FromRole addFromRole(final FromRole role) {
        return this.fromRoles.put(role.getLabel(), role);
    }
    
    public ToRole addToRole(final ToRole role) {
        return this.toRoles.put(role.getLabel(), role);
    }
    
    public FromRole removeFromRole(final String label) {
        return this.fromRoles.remove(label);
    }
    
    public ToRole removeToRole(final String label) {
        return this.toRoles.remove(label);
    }
}
