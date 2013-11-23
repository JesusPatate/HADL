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
        	// TODO: signature à compléter
            super("securityAuthorization", null, null);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    private class CredentialQueryService extends Service {
        public CredentialQueryService() {
        	// TODO: signature à compléter
            super("credentialQuery", null, null);
        }
    }
    
    public SecurityManager(String label){
        super(label);
        Port securityAuthorization = new Port("securityAuthorization");
        Port credentialQuery = new Port("credentialQuery");
        
        addProvidedConnection(securityAuthorization, new SecurityAuthService());
        addRequiredConnection(credentialQuery, new CredentialQueryService());
    }
}
