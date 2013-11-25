package fr.univnantes.alma.hadl.m1.serverDetails;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m1.DBRequest;
import fr.univnantes.alma.hadl.m1.cs.DBResponse;
import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Role;
import fr.univnantes.alma.hadl.m2.service.IncompatibleServiceException;
import fr.univnantes.alma.hadl.m2.service.NotConnectedServiceException;
import fr.univnantes.alma.hadl.m2.service.Service;

public class SQLQuery extends AtomicConnector {
	
	 private static final Map<String, Class<?>> PARAMETERS =
	            new HashMap<String, Class<?>>();
	    
	    static {
	    	PARAMETERS.put("request", DBRequest.class);
	    }
	
	private class SQLQueryService extends Service {
		
		SQLQueryService() {
			super("handleQuery", DBResponse.class, PARAMETERS);
		}
    }
	
    public SQLQuery(String label) {
        super(label);
        Role caller = new Role("caller");
        Role callee = new Role("callee");
        SQLQueryService required = new SQLQueryService();
        SQLQueryService provided = new SQLQueryService();
        
		addProvidedService(callee, provided);
        
        try {
	        addRequiredService(caller, required, provided);
		}
        catch (NotConnectedServiceException e) {
			e.printStackTrace();
		}
        catch (IncompatibleServiceException e) {
			e.printStackTrace();
		}
    }
}
