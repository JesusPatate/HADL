package fr.univnantes.alma.hadl.m1.serverDetails;

import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;


public class SecurityManager extends AtomicComponent {
    
    private class SecurityAuthService extends ProvidedService {
        public SecurityAuthService() {
            super("securityAuthorization", null, null);
        }
        
        public void receive(Message msg) {
            System.out.println("Le gestionnaire de sécurité reçoit : " + msg);
            
            if (msg.header.contentEquals("AUTH")) {
                final String login = msg.getBodyElement(0);
                final String pwd = msg.getBodyElement(1);
                
                credentialQueryService.requestDatabase(login, pwd);
            }
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    private class CredentialQueryService extends Service {
        
        public CredentialQueryService() {
            super("credentialQuery", null, null);
        }
        
        public void requestDatabase(final String login, final String pwd) {
            Message query = new Message("CREDQUERY",
                    "\'" + login + "\',\'" + pwd + "\'");
            
            System.out.println("Le gestionnaire de sécurité envoie : " + query); // DBG
            
            if (this.connection != null) {
                this.connection.send(this, query);
            }
        }
    }
    
    public SecurityManager(String label){
        super(label);
        Port securityAuthorizationPort = new Port("securityAuthorization");
        Port credentialQueryPort = new Port("credentialQuery");
        
        addProvidedConnection(securityAuthorizationPort, new SecurityAuthService());
        addRequiredConnection(credentialQueryPort, new CredentialQueryService());
    }
}
