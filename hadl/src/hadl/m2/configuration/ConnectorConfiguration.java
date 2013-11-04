package hadl.m2.configuration;

import hadl.m2.component.AtomicComponent;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.Connector;

import java.util.Map;


public class ConnectorConfiguration extends Connector {
    
    /**
     * Internal configuration
     */
    class Config extends Configuration {
        
        public Config(final String label) {
            super(label);
        }
    }
    
    private final Config config;
    
    public ConnectorConfiguration(final String label) {
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
}
