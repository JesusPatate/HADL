package hadl.m2.configuration;

import hadl.m2.ArchitecturalElement;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.ProvidedPort;
import hadl.m2.component.RequiredPort;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.FromRole;
import hadl.m2.connector.ToRole;

import java.util.HashMap;
import java.util.Map;


abstract class Configuration extends ArchitecturalElement {
    
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
    private final Map<String, FromAttachment> fromAttachments =
            new HashMap<String, FromAttachment>();
    
    /**
     * Connection to attachments
     */
    private final Map<String, ToAttachment> toAttachments =
            new HashMap<String, ToAttachment>();
    
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
     * Returns all from attachments.
     * 
     * @return A map with entries (label, attachment)
     */
    public Map<String, FromAttachment> getFromAttachments() {
        return new HashMap<String, FromAttachment>(this.fromAttachments);
    }
    
    /**
     * Returns all to attachments.
     * 
     * @return A map with entries (label, attachment)
     */
    public Map<String, ToAttachment> getToAttachments() {
        return new HashMap<String, ToAttachment>(this.toAttachments);
    }
    
    public AtomicComponent addComponent(final AtomicComponent comp) {
        return this.components.put(comp.getLabel(), comp);
    }
    
    public AtomicConnector addConnector(final AtomicConnector con) {
        return this.connectors.put(con.getLabel(), con);
    }
    
    public FromAttachment addFromAttachment(final String label,
            final RequiredPort port, final FromRole role) {
        
        return this.fromAttachments.put(label, new FromAttachment(label, port,
                role));
    }
    
    public ToAttachment addToAttachment(final String label,
            final ProvidedPort port, final ToRole role) {
        
        return this.toAttachments.put(label, new ToAttachment(label, port,
                role));
    }
}
