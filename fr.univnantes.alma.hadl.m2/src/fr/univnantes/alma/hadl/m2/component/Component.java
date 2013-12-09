package fr.univnantes.alma.hadl.m2.component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.univnantes.alma.hadl.m2.ArchitecturalElement;
import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.configuration.ComponentConfiguration;
import fr.univnantes.alma.hadl.m2.configuration.Configuration;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;

/**
 * A component of a software architecture.
 */
public abstract class Component extends ArchitecturalElement {
	protected Map<String, ProvidedService> providedServices = new HashMap<String, ProvidedService>();
	protected Map<String, Service> requiredServices = new HashMap<String, Service>();
	protected Map<Port, Set<ProvidedService>> portToProvided = new HashMap<Port, Set<ProvidedService>>();
	protected Map<ProvidedService, Port> providedToPort = new HashMap<ProvidedService, Port>();
	protected Map<Port, Set<Service>> portToRequired = new HashMap<Port, Set<Service>>();
	protected Map<Service, Port> requiredToPort = new HashMap<Service, Port>();
	protected Configuration configuration = null;
	protected ComponentConfiguration composite;

	public Component(final String label, ComponentConfiguration composite) {
		super(label);
		this.composite = composite;
	}

	public Component(final String label) {
		this(label, null);
	}

	public void setConfiguration(final Configuration config) {
		this.configuration = config;
	}

	public Set<Service> getProvidedServices() {
		return new HashSet<Service>(providedServices.values());
	}

	public Set<Service> getRequiredServices() {
		return new HashSet<Service>(requiredServices.values());
	}

	public Set<Service> getProvidedServices(final Port port) {
		return new HashSet<Service>(portToProvided.get(port));
	}

	public Set<Service> getRequiredServices(final Port port) {
		return new HashSet<Service>(portToRequired.get(port));
	}

	public Set<Port> getPorts() {
		return new HashSet<Port>(portToProvided.keySet());
	}

	public Port getProvidingPort(final String service) {
		ProvidedService providedService = providedServices.get(service);
		return providedToPort.get(providedService);
	}

	public Port getRequestingPort(final String service) {
		Service requiredService = requiredServices.get(service);
		return requiredToPort.get(requiredService);
	}

	public ComponentConfiguration getComposite() {
		return composite;
	}

	protected void addPort(final Port port) {
		port.setComponent(this);
		portToRequired.put(port, new HashSet<Service>());
		portToProvided.put(port, new HashSet<ProvidedService>());
	}

	protected void addProvidedService(final ProvidedService service) {
		providedServices.put(service.getLabel(), service);
		providedToPort.put(service, null);
	}

	protected void addRequiredService(final Service service) {
		requiredServices.put(service.getLabel(), service);
		requiredToPort.put(service, null);
	}

	protected void addRequiredConnection(Port port, Service service) {
		if (!portToRequired.containsKey(port)) {
			addPort(port);
		}

		addRequiredService(service);
		portToRequired.get(port).add(service);
		requiredToPort.put(service, port);
	}

	protected void addProvidedConnection(Port port, ProvidedService service) {
		if (!portToRequired.containsKey(port)) {
			addPort(port);
		}

		addProvidedService(service);
		portToProvided.get(port).add(service);
		providedToPort.put(service, port);
	}

	protected Response send(Request request) {
		Response resp = null;
		String service = request.getService();

		System.out.println(this.label + " envoie " + request);
		
		if (providedServices.containsKey(service)) {
			resp = providedServices.get(service).excecute(
					request.getParameters());
		} else if (requiredServices.containsKey(service)) {
			if (configuration != null) {
				Port port = getRequestingPort(service);
				resp = configuration.receive(port, request);
			} else {
				resp = new Response(null, false, String.format(
						"%s not connected to configuration", this.getLabel()));
			}
		}

		System.out.println(this.label + " renvoie " + resp);

		return resp;
	}

	protected Response receive(Request request) {
		Response resp = null;
		ProvidedService service = providedServices.get(request.getService());

		System.out.println(this.label + " reçoit " + request);

		if (request.isFromComposite()) {
			if (service != null) {
				resp = service.excecute(request.getParameters());
			} else {
				if (configuration != null) {
					Request fromComponent = new Request(request.getService(),
							request.getParameters());
					Port requestingPort = getRequestingPort(request
							.getService());
					resp = configuration.receive(requestingPort, fromComponent);
				} else {
					resp = new Response(null, false, String.format(
							"%s not connected to configuration",
							this.getLabel()));
				}
			}
		} else {

			resp = service.excecute(request.getParameters());
		}

		System.out.println(this.label + " renvoie " + resp);

		return resp;
	}
}
