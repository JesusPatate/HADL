package hadl.m2.configuration;

import hadl.m2.component.AtomicComponent;
import hadl.m2.component.Component;
import hadl.m2.component.ProvidedPort;
import hadl.m2.component.RequiredPort;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.FromRole;
import hadl.m2.connector.ToRole;

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
    public Map<String, FromAttachment> getFromAttachments() {
        return this.config.getFromAttachments();
    }
    
    /**
     * Returns all to attachments.
     * 
     * @return A map with entries (label, attachment)
     */
    public Map<String, ToAttachment> getToAttachments() {
        return this.config.getToAttachments();
    }
    
    public AtomicComponent addComponent(final AtomicComponent comp) {
        return this.config.addComponent(comp);
    }
    
    public AtomicConnector addConnector(final AtomicConnector con) {
        return this.config.addConnector(con);
    }
    
    public FromAttachment addFromAttachment(final String label,
            final RequiredPort port, final FromRole role) {
        
        return this.config.addFromAttachment(label, port, role);
    }
    
    public ToAttachment addToAttachment(final String label,
            final ProvidedPort port, final ToRole role) {
        
        return this.config.addToAttachment(label, port, role);
    }
}
