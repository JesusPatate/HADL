package fr.univnantes.alma.hadl.m2.configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.univnantes.alma.hadl.m2.ArchitecturalElement;
import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Component;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Connector;
import fr.univnantes.alma.hadl.m2.connector.Role;
import fr.univnantes.alma.hadl.m2.service.Service;


public abstract class Configuration extends ArchitecturalElement {
    
	protected final Set<Component> components =
            new HashSet<Component>();
    
    protected final Set<Connector> connectors =
            new HashSet<Connector>();
    
    protected final Map<Port, Port> bindings = new HashMap<Port, Port>();
    
    protected final Map<Role, Port> rpAttachements = new HashMap<Role, Port>();
    
    protected final Map<Port, Role> prAttachements = new HashMap<Port, Role>();
    
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
    public Set<Component> getComponents() {
        return new HashSet<Component>(components);
    }
    
    /**
     * Returns all internal connectors.
     * 
     * @return A map with entries (label, connectors)
     */
    public Set<Connector> getConnectors() {
        return new HashSet<Connector>(connectors);
    }
    
    public void addComponent(final Component comp) {
        components.add(comp);
        comp.setConfiguration(this);
    }
    
    public void addConnector(final Connector con) {
        connectors.add(con);
        con.setConfiguration(this);
    }
    
    // TODO vérifier que les services sont bien compatibles
    public void addAttachment(final Port port, final Component component,
            final Role role, final Connector connector)
            throws IllegalAttachmentException {
        
//        this.checkAttachment(port, component, role, connector);
        prAttachements.put(port, role);
        rpAttachements.put(role, port);
    }
    
    private void checkAttachment(Port port, Component component, Role role,
            Connector connector) throws IllegalAttachmentException {
        
        Set<Service> portProServices = component.getProvidedServices(port);
        Set<Service> portReqServices = component.getRequiredServices(port);
        
        Set<Service> roleProServices = connector.getProvidedServices(role);
        Set<Service> roleReqServices = connector.getRequiredServices(role);
        
        for (Service s : roleProServices) {
            if (!portProServices.contains(s)) {
                throw new IllegalAttachmentException(s.getLabel() +
                        " service is not provided by " + component.getLabel()
                        + "'s port " + port.getLabel());
            }
        }
        
        for (Service s : portReqServices) {
            if (!roleReqServices.contains(s)) {
                throw new IllegalAttachmentException(s.getLabel() +
                        " service is not required by " + connector.getLabel()
                        + "'s role " + role.getLabel());
            }
        }
    }
    
    // TODO vérifier que les services sont bien compatibles
    public void addBinding(final Port configPort, final Port compPort) {
        bindings.put(configPort, compPort);
        bindings.put(compPort, configPort);
    }
    
    public Response receive(Port port, Request request) {
        Response resp = null;
        
        if (bindings.containsKey(port)) {
            resp = bindings.get(port).receive(request);
        }
        else {
            resp = prAttachements.get(port).receive(request);
        }
        
        return resp;
    }
    
    public Response receive(Role role, Request request) {
        Port port = rpAttachements.get(role);
        Response resp = port.receive(request);
        Port bindedPort = bindings.get(port);
        
        // La requête doit être traitée par le composite.
        if (!resp.getProcessed() && bindedPort != null) {  
            resp = bindings.get(port).receive(request);
        }
        
        return resp;
    }
}
