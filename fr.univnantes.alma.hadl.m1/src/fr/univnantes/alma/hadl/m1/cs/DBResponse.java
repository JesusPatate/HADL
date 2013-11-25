package fr.univnantes.alma.hadl.m1.cs;

import java.util.ArrayList;
import java.util.List;


public class DBResponse {
    
    private final List<String> values;
    
    public DBResponse(final List<String> values) {
        this.values = values;
    }

    public List<String> getValues() {
        return new ArrayList<String>(values);
    }
}
