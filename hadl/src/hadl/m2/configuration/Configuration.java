package hadl.m2.configuration;

import hadl.m2.ArchitecturalElement;
import hadl.m2.Request;
import hadl.m2.Response;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.Port;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.Role;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public abstract class Configuration extends ArchitecturalElement {
    /**
     * Configuration components
     */
    private final Set<AtomicComponent> components = new HashSet<AtomicComponent>();
    
    /**
     * Configuration connectors
     */
    private final Set<AtomicConnector> connectors = new HashSet<AtomicConnector>();
    
    private final Map<Port, Port> bindings = new HashMap<Port, Port>();
    
    private final Map<Role, Port> rpAttachements = new HashMap<Role, Port>();
    
    private final Map<Port, Role> prAttachements = new HashMap<Port, Role>();
    
    /**
     * Creates a new empty configuration.
     * 
     * @param label
     *            Name of the new configuration
     */
    public Configuration(final String label) {
        super(label);
    }
    
    /**
     * Returns all internal components.
     * 
     * @return A map with entries (label, component)
     */
    public Set<AtomicComponent> getComponents() {
        return new HashSet<AtomicComponent>(components);
    }
    
    /**
     * Returns all internal connectors.
     * 
     * @return A map with entries (label, connectors)
     */
    public Set<AtomicConnector> getConnectors() {
        return new HashSet<AtomicConnector>(connectors);
    }
    
    public void addComponent(final AtomicComponent comp) {
        components.add(comp);
    }
    
    public void addConnector(final AtomicConnector con) {
        connectors.add(con);
    }
    
    // mettre en place des comparateurs pour vérifiers que les services sont bien compatibles
    public void addAttachment(final Port port, final Role role) {
        
    }
    
    public void addBinding(final Port configPort, final Port compPort) {
    }
    
    public Response receive(Port port, Request request){
    	// binding ou attachement
    	return null;
    }
    
    public Response receive(Role role, Request request){
    	// attachement
    	return null;
    }
}
