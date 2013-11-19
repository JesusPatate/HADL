package hadl.m2.connector;

import hadl.m2.Request;
import hadl.m2.Response;
import hadl.m2.service.Interface;


/**
 * A role of a connector.
 */
public abstract class Role extends Interface {
	private final Connector connector;
	
    public Role(final String label, Connector connector) {
        super(label);
        this.connector = connector;
    }
    
    Response send(Request request){
    	return configuration.receive(this, request);
    }
    
    public Response receive(Request request){
    	return connector.receive(request);
    }
}
