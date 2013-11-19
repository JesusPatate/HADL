package hadl.m2.component;


import hadl.m2.Request;
import hadl.m2.Response;
import hadl.m2.service.Interface;
import hadl.m2.service.ProvidedService;

import java.util.HashMap;
import java.util.Map;


public class Port extends Interface{
	private Map<String, ProvidedService> providedServices = new HashMap<String, ProvidedService>();
	
    public Port(final String label){
        super(label);
    }
    
    void addProvidedService(ProvidedService service){
    	providedServices.put(service.getLabel(), service);
    }
    
    boolean isProvide(String service){
    	return providedServices.containsKey(service);
    }

    Response send(Request request){
    	return configuration.receive(this, request);
    }
    
    public Response receive(Request request){
    	ProvidedService requestedService = providedServices.get(request.getService());
    	
    	if(requestedService.getReturnType() == null){
    		requestedService.excecute(request.getParameters());
    	}
    	else{
    		
    		// renvoyer la valeur, trouver comment faire
    		//Object returnValue = requestedService.excecute(call.getParameters());
    		
    	}
    	
    	return null;
    }
}
