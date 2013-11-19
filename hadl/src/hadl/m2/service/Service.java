package hadl.m2.service;

import java.util.HashMap;
import java.util.Map;


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
 * @see hadl.m2.component.Port
 * @see
 */
public abstract class Service{
    private final String label;
    private final String returnType;
    private final Map<String, String> parameters;
    
    Service(final String label, final String returnType, final Map<String, String> parameters) {
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
}
