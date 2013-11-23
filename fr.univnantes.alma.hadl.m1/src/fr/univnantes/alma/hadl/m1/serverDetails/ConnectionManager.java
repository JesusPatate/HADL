package fr.univnantes.alma.hadl.m1.serverDetails;


import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;



public class ConnectionManager extends AtomicComponent {
    
    private class ReceiveRequest extends ProvidedService {
        
        public ReceiveRequest() {
            super("receiveRequest", null, null);
        }
        
        @Override
        public void receive(final Message msg) {
            System.out.println("Le gestionnaire de connection reçoit : " + msg);
            
            if (msg.header.contentEquals("QUERY")) {
                getAuthorization(msg);
            }
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    private class ReceiveResponse extends Service {
        
        public ReceiveResponse() {
            super("", null, null);
        }
    }
    
    private class HandleQuery extends Service {
        
        public HandleQuery() {
        	super("handleQuery", null, null);
        }
    }
    
    private class SecurityAuthorization extends Service {
        
        public SecurityAuthorization() {
        	super("securityAuthorization", null, null);
        }        
    }
    
    public ConnectionManager(String label){
        super(label);
        Port externalSocketPro = new Port("externalSocket");
        Port securityQueryPort = new Port("securityQuery");
        Port dbQueryPort = new Port("dbQuery");
        
        addProvidedConnection(externalSocketPro, new ReceiveRequest());
        addRequiredConnection(securityQueryPort, new SecurityAuthorization());
        addRequiredConnection(dbQueryPort, new HandleQuery());
    }
    
    private void getAuthorization(final Message msg) {
        int idx = msg.body.lastIndexOf(',');
        Message query = new Message("AUTH", msg.body.substring(0, idx));
        
        this.securityAuth.receive(query);
    }
}
