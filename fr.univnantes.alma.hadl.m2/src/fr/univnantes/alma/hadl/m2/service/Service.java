package fr.univnantes.alma.hadl.m2.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * A service of a component.
 * 
 * <p>
 * Superclass of provided and required service. A service represents a
 * functionality provided or required by a component. It can be dynamically
 * attached to a port in order to be provided to other components (or by another
 * one if it is required).
 * </p>
 * 
 * @see fr.univnantes.alma.hadl.m2.component.Port
 * @see
 */
public abstract class Service{
    private final String label;
    private final String returnType;
    private final Map<String, String> parameters;
    
    public Service(final String label, final String returnType, 
    		final Map<String, String> parameters) {
        this.label = label;
        this.returnType = returnType;
        this.parameters = parameters;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getReturnType(){
    	return returnType;
    }
    
    public Map<String, String> getParameters(){
    	return new HashMap<String, String>(parameters);
    }
    
    public boolean isCompatible(Service service){
    	boolean compatible = label.equals(service.label) &&
    			returnType.equals(service.returnType);
    	
    	Iterator<Entry<String, String>> it = 
    			parameters.entrySet().iterator();
    	Iterator<Entry<String, String>> it2 = 
    			service.parameters.entrySet().iterator();
    	
    	while(compatible && it.hasNext() && it2.hasNext()){
    		Entry<String, String> e1 = it.next();
    		Entry<String, String> e2 = it2.next();
    		compatible = e1.getKey().equals(e2.getKey()) &&
    				e1.getValue().equals(e2.getValue());
    	}
    	
    	return compatible && 
    			parameters.size() == service.parameters.size();
    }
}
