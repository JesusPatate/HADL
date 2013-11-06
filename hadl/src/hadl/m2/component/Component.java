package hadl.m2.component;

import hadl.m2.ArchitecturalElement;
import hadl.m2.Link;

import java.util.HashMap;
import java.util.Map;


/**
 * A component of a software architecture.
 */
public abstract class Component extends ArchitecturalElement {
    
    /**
     * Services provided by the component.
     */
    protected Map<String, ProvidedService> providedServices =
            new HashMap<String, ProvidedService>();
    
    /**
     * Services required by the component.
     */
    protected Map<String, RequiredService> requiredServices =
            new HashMap<String, RequiredService>();
    
    /**
     * Ports which can be connected to component provided services.
     */
    protected Map<String, Port> ports =
            new HashMap<String, Port>();
    
    /**
     * Connections between provided service and port of the component.
     */
    protected Map<String, Connection> connections =
            new HashMap<String, Connection>();
    
    /**
     * 
     */
    protected Map<String, Connection> serviceToConnection =
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
            for (Port p : this.ports.values()) {
                if (p.linked(serviceLabel)) {
                    port = p;
                }
            }
        }
        
        return port;
    }
    
    public Map<String, Connection> getConnections() {
        return new HashMap<String, Connection>(this.connections);
    }
    
    /**
     * Returns the connection between a service and a port.
     * 
     * <p>
     * Method used by component ports to solve call routing.
     * </p>
     * 
     * @param soughtService
     *            Called service
     * @param enquirer
     *            Requesting port
     * 
     * @return Connection between the given service and the requesting port or
     *         null if it doesn't exist.
     */
    protected Connection getConnection(final String soughtService,
            final Port enquirer) {
        
        Connection con = null;
        
        Connection c = this.serviceToConnection.get(soughtService);
        
        if (c != null && c.getConnectedPort() == enquirer) {
            con = c;
        }
        
        return con;
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
    public Link addConnection(final String label,
            final String serviceLabel, final String portLabel)
            throws NoSuchServiceException, NoSuchPortException {
        
        Service service = null;
        Link link = null;
        
        // Retrieve the port
        
        Port port = this.ports.get(portLabel);
        
        if (port == null) {
            throw new NoSuchPortException();
        }
        
        // Retrieve the service
        
        if (providedServices.containsKey(serviceLabel)) {
            service = this.providedServices.get(serviceLabel);
            
            Connection con = new Connection(label, service, port);
            this.connections.put(label, con);
            this.serviceToConnection.put(serviceLabel, con);
            link = con;
        }
        
        else if (this.requiredServices.containsKey(serviceLabel)) {
            port.connectRequiredService(serviceLabel);
        }
        
        else {
            throw new NoSuchServiceException();
        }
        
        return link;
    }
    
    protected Link addConnection(final String label, final Service service,
            final Port port) throws NoSuchServiceException, NoSuchPortException {
        
        Link link = null;
        
        String sLabel = service.getLabel();
        String pLabel = port.getLabel();
        
        if (this.ports.containsKey(pLabel) == false) {
            throw new NoSuchPortException();
        }
        
        if (this.providedServices.containsKey(sLabel)) {
            Connection con = new Connection(label, service, port);
            this.connections.put(label, con);
            this.serviceToConnection.put(sLabel, con);
            link = con;
        }
        
        else if (this.requiredServices.containsKey(sLabel)) {
            port.connectRequiredService(sLabel);
        }
        
        else {
            throw new NoSuchServiceException();
        }
        
        return link;
    }
    
    /**
     * Removes a connection between provided service and port.
     * 
     * @param con
     *            Connection to remove
     * 
     * @return Removed connection or null if removal failed
     */
    public Connection removeConnection(final String connection) {
        Connection con = this.connections.get(connection);
        String service = con.getConnectedService().getLabel();
        
        if (con != null) {
            con.getConnectedPort().disconnect(service);
            con.getConnectedService().disconnect();
            this.connections.remove(con);
        }
        
        return con;
    }
    
    public boolean provides(final String service) {
        return this.providedServices.containsKey(service);
    }
    
    public boolean requires(final String service) {
        return this.requiredServices.containsKey(service);
    }
}
