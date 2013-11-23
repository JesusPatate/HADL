package fr.univnantes.alma.hadl.m2.component;


import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.service.Interface;


public class Port extends Interface{
	private Component component = null;
	
    public Port(final String label){
        super(label);
    }
    
    void setComponent(Component component){
    	this.component = component;
    }
    
    @Override
	protected Response send(Request request){
    	return configuration.receive(this, request);
    }
    
    @Override
    public Response receive(Request request){
    	return component.receive(request);
    }
}
