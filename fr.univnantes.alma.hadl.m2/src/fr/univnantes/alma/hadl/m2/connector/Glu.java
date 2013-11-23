package fr.univnantes.alma.hadl.m2.connector;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.service.Service;

class Glu {
	private Map<Service, GluFunction> providedToFunction =
			new HashMap<Service, GluFunction>();
	
	void addTransformation(Service providedService, GluFunction function){
		providedToFunction.put(providedService, function);
	}
	
	Request getTransformedRequest(Service providedService,
			Request request){
		return providedToFunction.get(providedService).execute(request);
	}
}
