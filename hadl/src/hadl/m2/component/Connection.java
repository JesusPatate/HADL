package hadl.m2.component;

/**
 * A connection between a service and a port of a component.
 */
abstract class Connection {
    
    private final String label;
    
    protected Connection(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
}
