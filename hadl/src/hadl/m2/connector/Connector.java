package hadl.m2.connector;

import hadl.m2.ArchitecturalElement;
import hadl.m2.Request;
import hadl.m2.Response;
import hadl.m2.service.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A connector of a software architecture.
 * 
 * <p>
 * A connector models a specific communication mechanism enabling interaction
 * between components.
 * </p>
 */
public abstract class Connector extends ArchitecturalElement {
    private Map<Role, Service> roleToProvided = new HashMap<Role, Service>();
    private Map<Role, Service> roleToRequired = new HashMap<Role, Service>();
    private Map<Service, Role> providedToRole = new HashMap<Service, Role>();
    private Map<Service, Role> requiredToRole = new HashMap<Service, Role>();
    private Map<Service, Service> providedToRequired = new HashMap<Service, Service>();
    
    
    public Connector(final String label) {
        super(label);
    }
    
    Response receive(Request request){
    	Service providedService = null;
    	Iterator<Service> it = providedToRequired.keySet().iterator();
    	
    	while(it.hasNext() && providedService == null){
    		Service currentService = it.next();
    		
    		if(currentService.getLabel().equals(request.getService())){
    			providedService = currentService;
    		}
    	}
    	
    	Service requiredService = providedToRequired.get(providedService);
    	Role role = requiredToRole.get(requiredService);
    	return role.send(request);
    }
}
