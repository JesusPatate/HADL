package fr.univnantes.alma.hadl.m2.configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.univnantes.alma.hadl.m2.ArchitecturalElement;
import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.Component;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.connector.Connector;
import fr.univnantes.alma.hadl.m2.connector.Role;
import fr.univnantes.alma.hadl.m2.service.Service;

public abstract class Configuration extends ArchitecturalElement {
	protected final Set<Component> components = new HashSet<Component>();
	protected final Set<Connector> connectors = new HashSet<Connector>();
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

	public void addAttachment(final Port port, final Role role)
			throws IllegalLinkException {
		checkAttachment(port, role);
		prAttachements.put(port, role);
		rpAttachements.put(role, port);
	}

	private void checkAttachment(Port port, Role role)
			throws IllegalLinkException {
		Component component = port.getComponent();
		Connector connector = role.getConnector();

		for (Service s : connector.getProvidedServices(role)) {
			if (!component.getProvidedServices(port).contains(s)) {
				throw new IllegalLinkException(String.format(
						"%s service is not provided by %s 's port %s",
						s.getLabel(), component.getLabel(), port.getLabel()));
			}
		}

		for (Service s : component.getRequiredServices(port)) {
			if (!connector.getRequiredServices(role).contains(s)) {
				throw new IllegalLinkException(String.format(
						"%s service is not required by %s 's role %s",
						s.getLabel(), connector.getLabel(), role.getLabel()));
			}
		}
	}

	public void addBinding(final Port port1, final Port port2)
			throws IllegalLinkException {
		checkBinding(port1, port2);
		bindings.put(port1, port2);
		bindings.put(port2, port1);
	}

	private void checkBinding(Port port1, Port port2)
			throws IllegalLinkException {
		Component component1 = port1.getComponent();
		Component component2 = port2.getComponent();

		for (Service s : component1.getProvidedServices(port1)) {
			if (!component2.getProvidedServices(port2).contains(s)) {
				throw new IllegalLinkException(String.format(
						"%s service is not provided by %s 's port %s",
						s.getLabel(), component2.getLabel(), port2.getLabel()));
			}
		}

		for (Service s : component2.getRequiredServices(port2)) {
			if (!component1.getRequiredServices(port1).contains(s)) {
				throw new IllegalLinkException(String.format(
						"%s service is not required by %s 's port %s",
						s.getLabel(), component1.getLabel(), port1.getLabel()));
			}
		}
	}

	public Response receive(Port port, Request request) {
		Response resp = null;

		if (bindings.containsKey(port)) {
			resp = bindings.get(port).receive(request);
		} else if (prAttachements.containsKey(port)) {
			resp = prAttachements.get(port).receive(request);
		} else {
			resp = new Response(null, false, String.format(
					"%s not linked to component", port.getLabel()));
		}

		return resp;
	}

	public Response receive(Role role, Request request) {
		Response resp = null;

		if (rpAttachements.containsKey(role)) {
			Port port = rpAttachements.get(role);
			resp = port.receive(request);
			Port bindedPort = bindings.get(port);

			// Request must be processed by composite if there is one.
			if (!resp.getProcessed() && bindedPort != null) {
				resp = bindedPort.receive(request);
			}
		} else {
			resp = new Response(null, false, String.format(
					"%s not linked to component", role.getLabel()));
		}

		return resp;
	}
}
