package hadl.m1;

import java.util.ArrayList;
import java.util.List;


public class RPCCall implements Call {
    
    /**
     * Name of the service called
     */
    private final String service;
    
    /**
     * Call parameters
     */
    public final List<Object> parameters = new ArrayList<Object>();
    
    public RPCCall(final String method, final List<Object> parameters) {
        this.service = method;
        
        for(Object arg : parameters) {
            this.parameters.add(arg);
        }
    }

    @Override
    public String getCalledService() {
        return this.service;
    }

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>(this.parameters);
    }
    
    public String toString() {
        return String.format("%s(%s)", this.service, this.parameters);
    }

    @Override
    public String getHeader() {
        return "CALL";
    }

    @Override
    public String getBody() {
        return this.toString();
    }
}
