package hadl.m2.component;

import hadl.m2.ArchitecturalElement;

import java.util.HashMap;
import java.util.Map;


/**
 * A component of a software architecture.
 */
public abstract class Component extends ArchitecturalElement {
    
    /**
     * Services provided by the component.
     */
    Map<String, ProvidedService> providedServices =
            new HashMap<String, ProvidedService>();
    
    /**
     * Services required by the component.
     */
    Map<String, RequiredService> requiredServices =
            new HashMap<String, RequiredService>();
    
    /**
     * Ports which can be connected to component provided services.
     */
    Map<String, Port> ports =
            new HashMap<String,Port>();
    
    /**
     * Connections between provided service and port of the component.
     */
    Map<String, Connection> connections =
            new HashMap<String, Connection>();
    
    /**
     * Creates a new empty component.
     * 
     * @param label
     *            Component name
     */
    public Component(final String label) {
        super(label);
    }
    
    public Map<String, ProvidedService> getProvidedServices() {
        return new HashMap<String, ProvidedService>(this.providedServices);
    }
    
    public Map<String, RequiredService> getRequiredServices() {
        return new HashMap<String, RequiredService>(this.requiredServices);
    }
    
    public Map<String, Port> getPorts() {
        return new HashMap<String, Port>(this.ports);
    }
    
    /**
     * Returns the port connected to a provided service
     * 
     * @param serviceLabel
     *            Service name
     * 
     * @return The provided port connected to the given service or null
     *         if the service is not connected to any port or provided by
     *         the component
     */
    public Port getProvidingPort(final String serviceLabel) {
        Port port = null;
        ProvidedService service = this.getProvidedServices().get(serviceLabel);
        
        if (service != null) {
            for (Connection con : this.connections.values()) {
                if (con.getConnectedService() == service) {
                    port = con.getConnectedPort();
                    break;
                }
            }
        }
        
        return port;
    }
    
    /**
     * Returns the port connected to a required service
     * 
     * @param serviceLabel
     *            Service name
     * 
     * @return The required port connected to the given service or null
     *         if the service is not connected to any port or required by
     *         the component
     * @throws NoSuchServiceException
     */
    public Port getRequestingPort(final String serviceLabel)
            throws NoSuchServiceException {
        
        Port port = null;
        RequiredService service = this.getRequiredServices().get(serviceLabel);
        
        if (service == null) {
            throw new NoSuchServiceException();
        }
        else {
            for (Connection con : this.connections.values()) {
                if (con.getConnectedService() == service) {
                    port = con.getConnectedPort();
                    break;
                }
            }
        }
        
        return port;
    }
    
    public Map<String, Connection> getConnections() {
    	return new HashMap<String, Connection>(this.connections);
    }
    
    protected ProvidedService addProvidedService(final ProvidedService service) {
        return this.providedServices.put(service.getLabel(), service);
    }
    
    protected RequiredService addRequiredService(final RequiredService service) {
        return this.requiredServices.put(service.getLabel(), service);
    }
    
    protected Port addPort(final Port port) {
        return this.ports.put(port.getLabel(), port);
    }
    
    /**
     * Connects a provided service and a provided port of the component.
     * 
     * @param label
     *            Name of the connection
     * @param serviceLabel
     *            Name of the service
     * @param portLabel
     *            Name of the port
     * 
     * @return The new connection.
     * 
     * @throws NoSuchServiceException
     *             The given name doesn't match with any provided service of the
     *             component
     * @throws NoSuchPortException
     *             The given name doesn't match with any provided port of the
     *             component
     */
    public Connection addConnection(final String label,
            final String serviceLabel, final String portLabel)
            throws NoSuchServiceException, NoSuchPortException {
        
    	// Retrieve the service
    	
        Service service = this.providedServices.get(serviceLabel);
        
        if(service == null) {
        	service = this.requiredServices.get(serviceLabel);
        }
        
        if (service == null) {
            throw new NoSuchServiceException();
        }
        
        // Retrieve the port
        
        Port port = this.ports.get(portLabel);
        
        if (port == null) {
            throw new NoSuchPortException();
        }
        
        return this.connections.put(label, new Connection(label, service, port));
    }
    
    /**
     * Removes a connection between provided service and port.
     * 
     * @param con
     *            Connection to remove
     * 
     * @return Removed connection or null if removal failed
     */
    public Connection removeProvidedConnection(final Connection con) {
        
        con.getConnectedPort().connections.remove(con);
        con.getConnectedService().connection = null;
        
        return this.connections.remove(con);
    }
}
