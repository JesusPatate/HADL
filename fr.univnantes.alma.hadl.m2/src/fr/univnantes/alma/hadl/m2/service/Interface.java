package fr.univnantes.alma.hadl.m2.service;

import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;


public abstract class Interface{
    protected final String label;
	
    public Interface(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public abstract Response receive(Request request);
}
