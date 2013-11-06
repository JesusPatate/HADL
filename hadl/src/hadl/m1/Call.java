package hadl.m1;

import hadl.m2.Message;

import java.util.List;


public interface Call extends Message {
    
    /**
     * Returns called service.
     * 
     * @return Called service name.
     */
    public String getCalledService();
    
    /**
     * Returns call parameters.
     * 
     * @return List of parameters passed to the service.
     */
    public List<Object> getParameters();
}
