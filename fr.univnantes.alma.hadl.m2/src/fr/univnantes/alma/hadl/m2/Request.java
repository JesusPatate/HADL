package fr.univnantes.alma.hadl.m2;

import java.util.HashMap;
import java.util.Map;

public class Request {
	private final String service;
	private final Map<String, Object> parameters;
	private final boolean fromComposite;
	
	public Request(String service, Map<String, Object> parameters){
		this(service, parameters, false);
	}
	
	public Request(String service, Map<String, Object> parameters, boolean fromComposite) {
		this.service = service;
		this.parameters = parameters;
		this.fromComposite = fromComposite;
	}

	public String getService(){
		return service;
	}
	
	public Map<String, Object> getParameters(){
		return new HashMap<String, Object>(parameters);
	}
	
	public boolean isFromComposite() {
		return fromComposite;
	}
	
	@Override
	public String toString() {
	    return String.format("%s(%s)", service, parameters);
	}
}
