package fr.univnantes.alma.hadl.m1.serverDetails;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.univnantes.alma.hadl.m1.DBRequest;
import fr.univnantes.alma.hadl.m1.cs.DBResponse;
import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;



public class ConnectionManager extends AtomicComponent {
	
	private static final Map<String, Class<?>> RECEIVE_REQUEST_PARAMS =
            new HashMap<String, Class<?>>();
	
	private static final Map<String, Class<?>> SECURITY_AUTHORIZATION_PARAMS =
            new HashMap<String, Class<?>>();
    
    static {
        RECEIVE_REQUEST_PARAMS.put("request", DBRequest.class);
        SECURITY_AUTHORIZATION_PARAMS.put("login", String.class);
        SECURITY_AUTHORIZATION_PARAMS.put("password", String.class);
    }
    
    private class ReceiveRequestService extends ProvidedService {
        public ReceiveRequestService() {
            super("receiveRequest", DBResponse.class, RECEIVE_REQUEST_PARAMS);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			Response resp = null;
			Map<String, Object> authParameters = new HashMap<String, Object>();
			authParameters.put("login", parameters.get("login"));
			authParameters.put("password", parameters.get("password"));
			Request authRequest = new Request("securityAuthorization", authParameters);
			resp = send(authRequest);
			boolean authorized = (Boolean) resp.getValue();
			
			if(authorized){
				resp = send(new Request("handleQuery", parameters));
			}
			else{
				List<Object> values = new LinkedList<Object>();
				values.add("Not authorized to access to server");
				DBResponse dbResp = new DBResponse(values, true);
				resp = new Response(dbResp);
			}
			
			return resp;
		}
    }
    
    private class HandleQuery extends Service {
        
        public HandleQuery() {
        	super("handleQuery", DBResponse.class, RECEIVE_REQUEST_PARAMS);
        }
    }
    
    private class SecurityAuthorization extends Service {
        
        public SecurityAuthorization() {
        	super("securityAuthorization", boolean.class, SECURITY_AUTHORIZATION_PARAMS);
        }        
    }
    
    public ConnectionManager(String label){
        super(label);
        Port externalSocket = new Port("externalSocket");
        Port securityCheck = new Port("securityCheck");
        Port dbQuery = new Port("dbQuery");
        
        addProvidedConnection(externalSocket, new ReceiveRequestService());
        addRequiredConnection(securityCheck, new SecurityAuthorization());
        addRequiredConnection(dbQuery, new HandleQuery());
    }
}
