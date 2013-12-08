package fr.univnantes.alma.hadl.m2.configuration;

import java.util.Set;

import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.Component;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.connector.Connector;
import fr.univnantes.alma.hadl.m2.connector.Role;


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
    class InternalConfiguration extends Configuration {
        
        public InternalConfiguration(final String label) {
            super(label);
        }
    }
    
    private final InternalConfiguration internalConfiguration;
    
    public ComponentConfiguration(final String label) {
        super(label);
        internalConfiguration = new InternalConfiguration(this.label);
    }
    
    /**
     * Returns all internal components.
     * 
     * @return A map with entries (label, component)
     */
    public Set<Component> getComponents() {
        return internalConfiguration.getComponents();
    }
    
    /**
     * Returns all internal connectors.
     * 
     * @return A map with entries (label, connectors)
     */
    public Set<Connector> getConnectors() {
        return internalConfiguration.getConnectors();
    }
    
    public void addComponent(final Component comp){
        internalConfiguration.addComponent(comp);
    }
    
    public void addConnector(final Connector con){
        internalConfiguration.addConnector(con);
    }
    
    public void addAttachment(final Port port, final Component component,
            final Role role, final Connector connector)
                    throws IllegalAttachmentException{
        internalConfiguration.addAttachment(port, component, role, connector);
    }
    
    public void addBinding(final Port configPort, final Port compPort){
        internalConfiguration.addBinding(configPort, compPort);
    }
    
    public Response receive(Port port, Request request){
    	Response response = null;
    	Port bindedPort = internalConfiguration.bindings.get(port);
    	
		if (portToProvided.containsKey(bindedPort)) {
			Request forComponent = new Request(request.getService(), request.getParameters(), true);
			response = configuration.receive(bindedPort, forComponent);
		} else {
			response = internalConfiguration.receive(port, request);
		}
    	
        return response;
    }
    
    public Response receive(Role role, Request request){
    	return internalConfiguration.receive(role, request);
    }
    
    protected Response receive(Request request) {
    	Port port = getProvidingPort(request.getService());
    	return internalConfiguration.receive(port, request);
    }
}
