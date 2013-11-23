package fr.univnantes.alma.hadl.m2.configuration;

import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Component;
import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;

import java.util.Set;


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
