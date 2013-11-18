package hadl.m2.component;

import hadl.m2.ArchitecturalElement;
import hadl.m2.Link;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A component of a software architecture.
 */
public abstract class Component extends ArchitecturalElement {

	protected Map<ProvidedService, Port> providedServices = new HashMap<ProvidedService, Port>();
	protected Map<String, Port> requiredServices = new HashMap<String, Port>();
	protected Map<Port, Set<ProvidedService>> providedConnections = new HashMap<Port, Set<ProvidedService>>();
	protected Map<Port, Set<String>> requiredConnections = new HashMap<Port, Set<String>>();

	/**
	 * Creates a new empty component.
	 * 
	 * @param label
	 *            Component name
	 */
	public Component(final String label) {
		super(label);
	}

	public Set<String> getProvidedServices() {
		Set<String> services = new HashSet<String>();
		
		for(Service service : this.providedServices.keySet()){
			services.add(service.getLabel());
		}
		
		return services;
	}

	public Set<String> getRequiredServices() {
		return new HashSet<String>(this.requiredServices.keySet());
	}

	public Set<String> getPorts() {
		Set<String> ports = new HashSet<String>();
		
		for(Port port : this.providedConnections.keySet()){
			ports.add(port.getLabel());
		}
		
		return ports;
	}

	/**
	 * Returns the port connected to a provided service
	 * 
	 * @param serviceLabel
	 *            Service name
	 * 
	 * @return The provided port connected to the given service or null if the
	 *         service is not connected to any port or provided by the component
	 */
	public Port getProvidingPort(final String serviceLabel) {
		Iterator<Port> it = this.providedConnections.keySet().iterator();
		boolean found = false;
		Port port = null;
		
		while(it.hasNext() && !found){
			port = it.next();
			found = port.getLabel().equals(serviceLabel);
		}

		return port;
	}

	/**
	 * Returns the port connected to a required service
	 * 
	 * @param serviceLabel
	 *            Service name
	 * 
	 * @return The required port connected to the given service or null if the
	 *         service is not connected to any port or required by the component
	 * @throws NoSuchServiceException
	 */
	public Port getRequestingPort(final String serviceLabel)
			throws NoSuchServiceException {

		Port port = null;
		RequiredService service = this.getRequiredServices().get(serviceLabel);

		if (service == null) {
			throw new NoSuchServiceException();
		} else {
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
	public Link addConnection(final String label, final String serviceLabel,
			final String portLabel) throws NoSuchServiceException,
			NoSuchPortException {

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
