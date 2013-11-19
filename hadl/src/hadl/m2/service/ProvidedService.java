package hadl.m2.service;

import hadl.m2.Response;

import java.util.Map;


/**
 * A service provided by a component.
 */
public abstract class ProvidedService extends Service {
    public ProvidedService(final String label, final String returnType, final Map<String, String> parameters) {
        super(label, returnType, parameters);
    }
    
    public abstract Response excecute(Map<String, Object> parameters);
}
