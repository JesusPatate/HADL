package fr.univnantes.alma.hadl.m2.service;


import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;


/**
 * A service provided by a component.
 */
public abstract class ProvidedService extends Service {
    public ProvidedService(final String label, final String returnType, final Map<String, String> parameters) {
        super(label, returnType, parameters);
    }
    
    public abstract Response excecute(Map<String, Object> parameters);
}
