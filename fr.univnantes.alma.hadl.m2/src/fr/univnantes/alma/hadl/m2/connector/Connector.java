package fr.univnantes.alma.hadl.m2.connector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.univnantes.alma.hadl.m2.ArchitecturalElement;
import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.service.IncompatibleServiceException;
import fr.univnantes.alma.hadl.m2.service.NotConnectedServiceException;
import fr.univnantes.alma.hadl.m2.service.Service;


/**
 * A connector of a software architecture.
 * 
 * <p>
 * A connector models a specific communication mechanism enabling interaction
 * between components.
 * </p>
 */
public abstract class Connector extends ArchitecturalElement{
	private Map<String, Service> providedServices = new HashMap<String, Service>();
    private Map<String, Service> requiredServices = new HashMap<String, Service>();
    private Map<Role, Set<Service>> roleToProvided = new HashMap<Role, Set<Service>>();
    private Map<Service, Role> providedToRole = new HashMap<Service, Role>();
    private Map<Role, Set<Service>> roleToRequired = new HashMap<Role, Set<Service>>();
    private Map<Service, Role> requiredToRole = new HashMap<Service, Role>();
    private Map<Service, Service> providedToRequired = new HashMap<Service, Service>();
    private Glu glu = new Glu();
    
    public Connector(final String label){
        super(label);
    }
    
    public Set<Role> getRoles(){
        return new HashSet<Role>(roleToProvided.keySet());
    }
    
    protected void addRole(Role role){
    	role.setConnector(this);
    	roleToProvided.put(role, new HashSet<Service>());
    	roleToRequired.put(role, new HashSet<Service>());
    }
    
    protected void addRequiredService(Role role, Service service){
    	if(!roleToProvided.containsKey(role)){
    		addRole(role);
    	}
    	
    	requiredServices.put(service.getLabel(), service);
    	roleToRequired.get(role).add(service);
    	requiredToRole.put(service, role);
    }
    
    protected void addProvidedService(Role role, Service service, 
    		Service equivalent, GluFunction function) 
    				throws NotConnectedServiceException, 
    				IncompatibleServiceException{
    	if(!service.isCompatible(equivalent)){
    		throw new IncompatibleServiceException(service, equivalent);
    	}
    	else if(requiredToRole.get(equivalent) == null){
    		throw new NotConnectedServiceException(equivalent, label);
    	}
    	
    	if(!roleToProvided.containsKey(role)){
    		addRole(role);
    	}
    	
    	providedServices.put(service.getLabel(), service);
    	roleToProvided.get(role).add(service);
    	providedToRole.put(service, role);
    	providedToRequired.put(service, equivalent);
    	glu.addTransformation(service, function);
    }
    
    protected void addProvidedService(Role role, Service service,
    		Service equivalent) throws NotConnectedServiceException,
    		IncompatibleServiceException{
    	addProvidedService(role, service, equivalent,
    			GluFunction.identity);
    }
    
    protected Response receive(Request request){
    	Service service = providedServices.get(request.getService());
    	Service equivalent = providedToRequired.get(service);
    	Role role = providedToRole.get(equivalent);
    	Request transformedReq = 
    			glu.getTransformedRequest(service, request);
    	return role.send(transformedReq);
    }
    
    public Role getProvidingRole(final String service){
    	Service providedService = providedServices.get(service);
    	return providedToRole.get(providedService);
    }
    
    public Role getRequestingRole(final String service){
    	Service requiredService = requiredServices.get(service);
    	return requiredToRole.get(requiredService);
    }
}
