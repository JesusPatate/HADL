package fr.univnantes.alma.hadl.m2.connector;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.service.Service;

class Glue {
	private Map<Service, GlueFunction> providedToFunction =
			new HashMap<Service, GlueFunction>();
	
	void addTransformation(Service providedService, GlueFunction function){
		providedToFunction.put(providedService, function);
	}
	
	Request getTransformedRequest(Service providedService,
			Request request){
		return providedToFunction.get(providedService).execute(request);
	}
}
