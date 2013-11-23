package fr.univnantes.alma.hadl.m2;

import java.util.HashMap;
import java.util.Map;

public class Request {
	private final String service;
	private final Map<String, Object> parameters;
	
	public Request(String service, Map<String, Object> parameters){
		this.service = service;
		this.parameters = parameters;
	}
	
	public String getService(){
		return service;
	}
	
	public Map<String, Object> getParameters(){
		return new HashMap<String, Object>(parameters);
	}
}
