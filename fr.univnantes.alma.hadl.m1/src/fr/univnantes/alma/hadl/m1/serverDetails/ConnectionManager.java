package fr.univnantes.alma.hadl.m1.serverDetails;


import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;



public class ConnectionManager extends AtomicComponent {
    
    private class ReceiveRequestService extends ProvidedService {
    	// TODO: signature à compléter
        public ReceiveRequestService() {
            super("receiveRequest", null, null);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    private class HandleQuery extends Service {
        
        public HandleQuery() {
        	// TODO: signature à compléter
        	super("handleQuery", null, null);
        }
    }
    
    private class SecurityAuthorization extends Service {
        
        public SecurityAuthorization() {
        	// TODO: signature à compléter
        	super("securityAuthorization", null, null);
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
    
    /*private void getAuthorization(final Message msg) {
        int idx = msg.body.lastIndexOf(',');
        Message query = new Message("AUTH", msg.body.substring(0, idx));
        
        this.securityAuth.receive(query);
    }*/
}
