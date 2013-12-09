package fr.univnantes.alma.hadl.m1.serverDetails;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;


public class Database extends AtomicComponent {
	
	private static final Map<String, Class<?>> SECURITY_MANAGEMENT_PARAMS =
            new HashMap<String, Class<?>>();
	
	private static final Map<String, Class<?>> HANDLE_QUERY_PARAMS =
            new HashMap<String, Class<?>>();
    
    static {
        SECURITY_MANAGEMENT_PARAMS.put("login", String.class);
        SECURITY_MANAGEMENT_PARAMS.put("password", String.class);
        HANDLE_QUERY_PARAMS.put("request", DBRequest.class);
    }
    
    private class SecurityManagement extends Service {
    	
        public SecurityManagement() {
            super("securityManagement", boolean.class, SECURITY_MANAGEMENT_PARAMS);
        }
    }
    
    private class HandleQuery extends ProvidedService {
        public HandleQuery() {
            super("handleQuery", DBResponse.class, HANDLE_QUERY_PARAMS);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			DBRequest dbRequest = (DBRequest) parameters.get("request");
			Response resp = null;
			Map<String, Object> authParameters = new HashMap<String, Object>();
			authParameters.put("login", dbRequest.getLogin());
			authParameters.put("password", dbRequest.getPassword());
			Request authRequest = new Request("securityManagement", authParameters);
			resp = send(authRequest);
			boolean authorized = (Boolean) resp.getValue();
			
			if(authorized){
				String query = dbRequest.getQuery();
				List<Object> values = new LinkedList<Object>();
				
				if (store.containsKey(query)) {
					values.add(store.get(query));
				}

				DBResponse dbResp = new DBResponse(values);
				resp = new Response(dbResp);
			}
			else{
				List<Object> values = new LinkedList<Object>();
				values.add("Not authorized to access to database");
				DBResponse dbResp = new DBResponse(values, true);
				resp = new Response(dbResp);
			}
			
			return resp;
		}
    }
    
    private Map<String, Object> store = new HashMap<String, Object>();
    
    public Database(String label){
        super(label);
        Port securityManagement = new Port("securityManagement");
        Port query = new Port("query");
        
        addRequiredConnection(securityManagement, new SecurityManagement());
        addProvidedConnection(query, new HandleQuery());
        
        initializeStore();
    }
    
    private void initializeStore() {
    	store.put("key", "michel");
    }
}
