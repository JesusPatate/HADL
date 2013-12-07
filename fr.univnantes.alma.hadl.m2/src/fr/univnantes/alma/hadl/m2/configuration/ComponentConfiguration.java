package fr.univnantes.alma.hadl.m2.configuration;

import java.util.Set;

import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Component;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Connector;
import fr.univnantes.alma.hadl.m2.connector.Role;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;


// TODO: passage de la config haut niveau vers la config bas niveau

/**
 * A composite component.
 * 
 * <p>
 * A component which holds an internal configuration.
 * </p>
 */
public abstract class ComponentConfiguration extends Component {
    
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
    
    public void addComponent(final AtomicComponent comp){
        this.config.addComponent(comp);
    }
    
    public void addConnector(final AtomicConnector con){
        this.config.addConnector(con);
    }
    
    // TODO vérifier que les services sont bien compatibles
    public void addAttachment(final Port port, final Component component,
            final Role role, final Connector connector)
                    throws IllegalAttachmentException{
        
        this.config.addAttachment(port, component, role, connector);
    }
    
    // TODO vérifier que les services sont bien compatibles
    public void addBinding(final Port configPort, final Port compPort){
        this.config.addBinding(configPort, compPort);
    }
    
    public Response receive(Port port, Request request){
    	System.out.println("config interne");
        return this.config.receive(port, request);
    }
    
    public Response receive(Role role, Request request){
        return this.receive(role, request);
    }
    
    protected Response receive(Request request) {
    	/*ProvidedService service = providedServices.get(request.getService());
    	return service.excecute(request.getParameters());*/
    	Port port = getProvidingPort(request.getService());
    	return config.receive(port, request);
    }
}
