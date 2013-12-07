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
    private final Class<?> returnType;
    private final Map<String, Class<?>> parameters;
    
    public Service(final String label, final Class<?> returnType, 
    		final Map<String, Class<?>> parameters) {
        this.label = label;
        this.returnType = returnType;
        this.parameters = parameters;
    }
    
    public String getLabel() {
        return label;
    }
    
    public Class<?> getReturnType(){
    	return returnType;
    }
    
    public Map<String, Class<?>> getParameters(){
    	return new HashMap<String, Class<?>>(parameters);
    }
    
    public boolean isCompatible(Service service){
    	boolean compatible = label.equals(service.label) &&
    			returnType.equals(service.returnType);
    	
    	Iterator<Entry<String, Class<?>>> it = 
    			parameters.entrySet().iterator();
    	Iterator<Entry<String, Class<?>>> it2 = 
    			service.parameters.entrySet().iterator();
    	
    	while(compatible && it.hasNext() && it2.hasNext()){
    		Entry<String, Class<?>> e1 = it.next();
    		Entry<String, Class<?>> e2 = it2.next();
    		compatible = e1.getKey().equals(e2.getKey()) &&
    				e1.getValue().equals(e2.getValue());
    	}
    	
    	return compatible && 
    			parameters.size() == service.parameters.size();
    }
    
    public boolean equals(final Object obj) {
        boolean result = true;
 
        if ((obj != this) && (obj instanceof Service)) {
            Service other = (Service) obj;
 
            if (!this.isCompatible(other)) {
                result = false;
            }
        }
 
        return result;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer("service ");
        buf.append(this.getLabel() + "(");
        
        for(Entry<String, Class<?>> param : this.parameters.entrySet()) {
            buf.append(param.getKey() + ": " + param.getValue().getSimpleName()+ ", ");
        }
        
        buf = buf.delete(buf.length() - 2, buf.length());
        
        buf.append(") : ");
        
        buf.append(this.returnType.getSimpleName());
        
        return buf.toString();
    }
}
