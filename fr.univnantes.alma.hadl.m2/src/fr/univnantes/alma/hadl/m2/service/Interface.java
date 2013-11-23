package fr.univnantes.alma.hadl.m2.service;

import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.configuration.Configuration;


public abstract class Interface{
    protected final String label;
    protected Configuration configuration = null;
	
    public Interface(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setConfiguration(Configuration configuration){
    	this.configuration = configuration;
    }
        
    protected abstract Response send(Request request);
    
    public abstract Response receive(Request request);
}
