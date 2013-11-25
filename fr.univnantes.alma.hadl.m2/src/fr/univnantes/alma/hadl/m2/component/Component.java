package fr.univnantes.alma.hadl.m2.component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.univnantes.alma.hadl.m2.ArchitecturalElement;
import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.configuration.Configuration;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;


/**
 * A component of a software architecture.
 */
public abstract class Component extends ArchitecturalElement{
    private Map<String, ProvidedService> providedServices = new HashMap<String, ProvidedService>();
    private Map<String, Service> requiredServices = new HashMap<String, Service>();
    private Map<Port, Set<ProvidedService>> portToProvided = new HashMap<Port, Set<ProvidedService>>();
    private Map<ProvidedService, Port> providedToPort = new HashMap<ProvidedService, Port>();
    private Map<Port, Set<Service>> portToRequired = new HashMap<Port, Set<Service>>();
    private Map<Service, Port> requiredToPort = new HashMap<Service, Port>();
    protected Configuration configuration = null;
	
    public Component(final String label){
        super(label);
    }
    
    public void setConfiguration(final Configuration config) {
    	this.configuration = config;
    }
    
    public Set<Service> getProvidedServices(){
        return new HashSet<Service>(providedServices.values());
    }
    
    public Set<Service> getRequiredServices(){
        return new HashSet<Service>(requiredServices.values());
    }
    
    public Set<Port> getPorts(){
        return new HashSet<Port>(portToProvided.keySet());
    }
    
    protected void addPort(final Port port){
    	port.setComponent(this);
        portToRequired.put(port, new HashSet<Service>());
        portToProvided.put(port, new HashSet<ProvidedService>());
    }
    
    protected void addProvidedService(final ProvidedService service){
        providedServices.put(service.getLabel(), service);
        providedToPort.put(service, null);
    }
    
    protected void addRequiredService(final Service service){
        requiredServices.put(service.getLabel(), service);
        requiredToPort.put(service, null);
    }
    
    protected void addRequiredConnection(Port port, Service service){
    	if(!portToRequired.containsKey(port)){
    		addPort(port);
    	}
    	
    	addRequiredService(service);    	
    	portToRequired.get(port).add(service);
    	requiredToPort.put(service, port);
    }
    
    protected void addProvidedConnection(Port port, ProvidedService service){
    	if(!portToRequired.containsKey(port)){
    		addPort(port);
    	}
    	
    	addProvidedService(service);
    	portToProvided.get(port).add(service);
    	providedToPort.put(service, port);
    }
        
    protected Response send(Request request){
    	Response resp = null;
    	String service = request.getService();
    	
    	if(providedServices.containsKey(service)){
    		resp = providedServices.get(service).excecute(request.getParameters());
    	}
    	else if(requiredServices.containsKey(service)){
    		Port port = getRequestingPort(service);
    		resp = configuration.receive(port, request);
    	}
    	
    	return resp;
    }
    
    Response receive(Request request) {
    	ProvidedService service = providedServices.get(request.getService());
    	return service.excecute(request.getParameters());
    }
    
    public Port getProvidingPort(final String service){
    	ProvidedService providedService = providedServices.get(service);
    	return providedToPort.get(providedService);
    }
    
    public Port getRequestingPort(final String service){
    	Service requiredService = requiredServices.get(service);
    	return requiredToPort.get(requiredService);
    }
}
