package fr.univnantes.alma.hadl.m1;

import java.util.ArrayList;
import java.util.List;


public class DBResponse {
    
	// if error occured, the error message is the only object in value list.
	private final boolean error; 
    private final List<Object> values;
    
    public DBResponse(final List<Object> values, boolean error) {
    	this.error = error;
        this.values = values;
    }
    
    public DBResponse(final List<Object> values) {
    	this(values, false);
    }

    public List<Object> getValues() {
        return new ArrayList<Object>(values);
    }
    
    public boolean getError() {
        return error;
    }
}
