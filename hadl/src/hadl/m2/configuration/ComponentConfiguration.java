package hadl.m2.configuration;

import hadl.m2.component.AtomicComponent;
import hadl.m2.component.Component;
import hadl.m2.component.Port;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.Role;

import java.util.HashMap;
import java.util.Map;


/**
 * A composite component.
 * 
 * <p>
 * A component which holds an internal configuration.
 * </p>
 */
public class ComponentConfiguration extends Component {
    
    /**
     * Internal configuration
     */
    class Config extends Configuration {
        
        public Config(final String label) {
            super(label);
        }
    }
    
    private final Config config;
    
    protected final Map<String, CompConfBinding> bindings =
            new HashMap<String, CompConfBinding>();
    
    public ComponentConfiguration(final String label) {
        super(label);
        this.config = new Config(this.label);
    }
    
    /**
     * Returns all internal components.
     * 
     * @return A map with entries (label, component)
     */
    public Map<String, AtomicComponent> getComponents() {
        return this.config.getComponents();
    }
    
    /**
     * Returns all internal connectors.
     * 
     * @return A map with entries (label, connectors)
     */
    public Map<String, AtomicConnector> getConnectors() {
        return this.config.getConnectors();
    }
    
    /**
     * Returns all from attachments.
     * 
     * @return A map with entries (label, attachment)
     */
    public Map<String, Attachment> getAttachments() {
        return this.config.getAttachments();
    }
    
    public AtomicComponent addComponent(final AtomicComponent comp) {
        return this.config.addComponent(comp);
    }
    
    public AtomicConnector addConnector(final AtomicConnector con) {
        return this.config.addConnector(con);
    }
    
    public Attachment addAttachment(final String label,
            final Port port, final Role role) {
        
        return this.config.addAttachment(label, port, role);
    }
    
    public CompConfBinding addBinding(final String label,
            final Port port1, final Port port2) {
        
        return this.bindings.put(
                label, new CompConfBinding(label, port1, port2));
    }
}
