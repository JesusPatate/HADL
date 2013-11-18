package hadl.m2.configuration;

import hadl.m2.ArchitecturalElement;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.Port;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.Role;

import java.util.HashMap;
import java.util.Map;


public abstract class Configuration extends ArchitecturalElement {
    
    /**
     * Configuration components
     */
    private final Map<String, AtomicComponent> components =
            new HashMap<String, AtomicComponent>();
    
    /**
     * Configuration connectors
     */
    private final Map<String, AtomicConnector> connectors =
            new HashMap<String, AtomicConnector>();
    
    /**
     * Connection from attachments
     */
    private final Map<String, Attachment> attachments =
            new HashMap<String, Attachment>();
    
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
    public Map<String, AtomicComponent> getComponents() {
        return new HashMap<String, AtomicComponent>(this.components);
    }
    
    /**
     * Returns all internal connectors.
     * 
     * @return A map with entries (label, connectors)
     */
    public Map<String, AtomicConnector> getConnectors() {
        return new HashMap<String, AtomicConnector>(this.connectors);
    }
    
    /**
     * Returns all attachments.
     * 
     * @return A map with entries (label, attachment)
     */
    public Map<String,Attachment> getAttachments() {
        return new HashMap<String, Attachment>(this.attachments);
    }
    
    public AtomicComponent addComponent(final AtomicComponent comp) {
        return this.components.put(comp.getLabel(), comp);
    }
    
    public AtomicConnector addConnector(final AtomicConnector con) {
        return this.connectors.put(con.getLabel(), con);
    }
    
    public Attachment addAttachment(final String label,
            final Port port, final Role role) {
        
        return this.attachments.put(label, new Attachment(label, port,
                role));
    }
}
