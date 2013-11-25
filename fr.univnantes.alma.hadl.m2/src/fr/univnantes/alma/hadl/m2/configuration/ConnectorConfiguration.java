package fr.univnantes.alma.hadl.m2.configuration;

import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Connector;

import java.util.Set;

// TODO Rajouter les methodes de la config 
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
        this.config = new Config(label);
    }
    
    /**
     * Returns all internal components.
     * 
     * @return A map with entries (label, component)
     */
    public Set<AtomicComponent> getComponents() {
        return this.config.getComponents();
    }
    
    /**
     * Returns all internal connectors.
     * 
     * @return A map with entries (label, connectors)
     */
    public Set<AtomicConnector> getConnectors() {
        return this.config.getConnectors();
    }
}
