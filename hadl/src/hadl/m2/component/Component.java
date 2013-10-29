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
    Map<String, ProvidedPort> providedPorts =
            new HashMap<String, ProvidedPort>();
    
    /**
     * Ports which can be connected to component required services.
     */
    Map<String, RequiredPort> requiredPorts =
            new HashMap<String, RequiredPort>();
    
    /**
     * Connections between provided service and port of the component.
     */
    Map<String, ProvidedConnection> providedConnections =
            new HashMap<String, ProvidedConnection>();
    
    /**
     * Connections between required service and port of the component.
     */
    Map<String, RequiredConnection> requiredConnections =
            new HashMap<String, RequiredConnection>();
    
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
        return this.providedServices;
    }
    
    public Map<String, RequiredService> getRequiredServices() {
        return this.requiredServices;
    }
    
    public Map<String, ProvidedPort> getProvidedPorts() {
        return this.providedPorts;
    }
    
    public Map<String, RequiredPort> getRequiredPorts() {
        return this.requiredPorts;
    }
    
    public Map<String, ProvidedConnection> getProvidedConnections() {
        return this.providedConnections;
    }
    
    public Map<String, RequiredConnection> getRequiredConnections() {
        return this.requiredConnections;
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
    public ProvidedPort getProvidingPort(final String serviceLabel) {
        ProvidedPort port = null;
        ProvidedService service = this.getProvidedServices().get(serviceLabel);
        
        if (service != null) {
            for (ProvidedConnection con : this.providedConnections.values()) {
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
     */
    public RequiredPort getRequestingPort(final String serviceLabel) {
        RequiredPort port = null;
        RequiredService service = this.getRequiredServices().get(serviceLabel);
        
        if (service != null) {
            for (RequiredConnection con : this.requiredConnections.values()) {
                if (con.getConnectedService() == service) {
                    port = con.getConnectedPort();
                    break;
                }
            }
        }
        
        return port;
    }
    
    public ProvidedService addProvidedService(final ProvidedService service) {
        return this.providedServices.put(service.getLabel(), service);
    }
    
    public RequiredService addRequiredService(final RequiredService service) {
        return this.requiredServices.put(service.getLabel(), service);
    }
    
    public ProvidedPort addProvidedPort(final ProvidedPort port) {
        return this.providedPorts.put(port.getLabel(), port);
    }
    
    public RequiredPort addRequiredPort(final RequiredPort port) {
        return this.requiredPorts.put(port.getLabel(), port);
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
    public ProvidedConnection addProvidedConnection(final String label,
            final String serviceLabel, final String portLabel)
            throws NoSuchServiceException, NoSuchPortException {
        
        ProvidedService service = this.providedServices.get(serviceLabel);
        ProvidedPort port = this.providedPorts.get(portLabel);
        
        if (service == null) {
            throw new NoSuchServiceException();
        }
        
        if (port == null) {
            throw new NoSuchPortException();
        }
        
        return this.providedConnections.put(label,
                new ProvidedConnection(label, service, port));
    }
    
    /**
     * Connects a required service and a required port of the component.
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
     *             The given name doesn't match with any required service of the
     *             component
     * @throws NoSuchPortException
     *             The given name doesn't match with any required port of the
     *             component
     */
    public RequiredConnection addRequiredConnection(final String label,
            final String serviceLabel, final String portLabel)
            throws NoSuchServiceException, NoSuchPortException {
        
        RequiredService service = this.requiredServices.get(serviceLabel);
        RequiredPort port = this.requiredPorts.get(portLabel);
        
        if (service == null) {
            throw new NoSuchServiceException();
        }
        
        if (port == null) {
            throw new NoSuchPortException();
        }
        
        return this.requiredConnections.put(label,
                new RequiredConnection(label, service, port));
    }
    
    /**
     * Removes a provided service.
     * 
     * @param service
     *            Provided service to remove
     * 
     * @return Removed service or null if removal failed
     */
    public ProvidedService removeProvidedService(
            final ProvidedService service) {
        
        return this.providedServices.remove(service);
    }
    
    /**
     * Removes a required service.
     * 
     * @param service
     *            Required service to remove
     * 
     * @return Removed service or null if removal failed
     */
    public RequiredService removeRequiredService(
            final RequiredService service) {
        
        return this.requiredServices.remove(service);
    }
    
    /**
     * Removes a provided port.
     * 
     * @param port
     *            Provided port to remove
     * 
     * @return Removed port or null if removal failed
     */
    public ProvidedPort removeProvidedPort(
            final ProvidedPort port) {
        
        return this.providedPorts.remove(port);
    }
    
    /**
     * Removes a required port.
     * 
     * @param port
     *            Required port to remove
     * 
     * @return Removed port or null if removal failed
     */
    public RequiredPort removeRequiredPort(
            final RequiredPort port) {
        
        return this.requiredPorts.remove(port);
    }
    
    /**
     * Removes a connection between provided service and port.
     * 
     * @param con
     *            Connection to remove
     * 
     * @return Removed connection or null if removal failed
     */
    public ProvidedConnection removeProvidedConnection(
            final ProvidedConnection con) {
        
        return this.providedConnections.remove(con);
    }
    
    /**
     * Removes a connection between required service and port.
     * 
     * @param con
     *            Connection to remove
     * 
     * @return Removed connection or null if removal failed
     */
    public RequiredConnection removeRequiredConnection(
            final RequiredConnection con) {
        
        return this.requiredConnections.remove(con);
    }
}
