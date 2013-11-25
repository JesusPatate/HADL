package fr.univnantes.alma.hadl.m2.connector;

import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.service.Interface;


/**
 * A role of a connector.
 */
public class Role extends Interface{
	private Connector connector = null;
	
    public Role(final String label){
        super(label);
    }
    
    void setConnector(Connector connector){
    	this.connector = connector;
    }
    
    public Connector getConnector() {
    	return this.connector;
    }
    
    @Override
    public Response receive(Request request){
    	return connector.receive(request);
    }
}
