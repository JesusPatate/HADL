package hadl.m2.component;

import hadl.m2.ArchitecturalElement;
import hadl.m2.Request;
import hadl.m2.Response;
import hadl.m2.service.ProvidedService;
import hadl.m2.service.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * A component of a software architecture.
 */
public abstract class Component extends ArchitecturalElement {
	/**
     * Services provided by the component.
     */
    private Set<ProvidedService> providedServices = new HashSet<ProvidedService>();
    
    /**
     * Services required by the component.
     */
    private Set<Service> requiredServices = new HashSet<Service>();
    
    /**
     * Ports which can be connected to component provided services.
     */
    private Map<Port, Set<Service>> portToRequired = new HashMap<Port, Set<Service>>();
    private Map<Service, Port> requiredToPort = new HashMap<Service, Port>();
	
    public Component(final String label) {
        super(label);
    }
    
    public Set<Service> getProvidedServices() {
        return new HashSet<Service>(providedServices);
    }
    
    public Set<Service> getRequiredServices() {
        return new HashSet<Service>(requiredServices);
    }
    
    public Set<Port> getPorts() {
        return new HashSet<Port>(portToRequired.keySet());
    }
    
    public Port getProvidingPort(final String service) {
    	Iterator<Port> it = portToRequired.keySet().iterator();
    	Port port = null;
    	
    	while(it.hasNext() && port == null){
    		Port currentPort = it.next();
    		
    		if(currentPort.isProvide(service)){
    			port = currentPort;
    		}
    	}
    	
    	return port;
    }
    
    public Port getRequestingPort(final String service){
    	Iterator<Service> it = requiredToPort.keySet().iterator();
    	Port port = null;
    	
    	while(it.hasNext() && port == null){
    		Service currentService = it.next();
    			
    		if(currentService.getLabel().equals(service)){
        		port = requiredToPort.get(currentService);
        	}
    	}
    	
    	return port;
    }
    
    protected void addProvidedService(final ProvidedService service) {
        providedServices.add(service);
    }
    
    protected void addRequiredService(final Service service) {
        requiredServices.add(service);
    }
    
    protected void addPort(final Port port) {
        portToRequired.put(port, new HashSet<Service>());
    }
    
    protected void addRequiredConnection(Port port, Service service){
    	portToRequired.get(port).add(service);
    	requiredToPort.put(service, port);
    }
    
    protected void addProvidedConnection(Port port, ProvidedService service){
    	port.addProvidedService(service);
    }
    
    protected Response send(Request request){
    	Port port = getRequestingPort(request.getService());
    	return port.send(request);
    }
}
