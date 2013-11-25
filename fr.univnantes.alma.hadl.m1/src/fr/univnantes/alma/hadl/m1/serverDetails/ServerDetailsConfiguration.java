package fr.univnantes.alma.hadl.m1.serverDetails;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m1.DBRequest;
import fr.univnantes.alma.hadl.m1.cs.DBResponse;
import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.configuration.ComponentConfiguration;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;


public class ServerDetailsConfiguration extends ComponentConfiguration {
    
	private static final Map<String, Class<?>> PARAMETERS =
            new HashMap<String, Class<?>>();
    
    static {
        PARAMETERS.put("request", DBRequest.class);
    }
    
    private class InternalReceiveRequest extends ProvidedService {
        
        InternalReceiveRequest() {
            super("internalReceiveRequest", DBResponse.class, PARAMETERS);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			Request req = new Request("receiveRequest", parameters);
			
			return send(req);
		}
    }
    
    private class ReceiveRequest  extends Service {
        
        public ReceiveRequest() {
            super("receiveRequest", DBResponse.class, PARAMETERS);
        }
    }
	
    public ServerDetailsConfiguration(String label) {
        super(label);
        Port receiveRequest = new Port("receiveRequest");
        Port externalConfiguration = new Port("externalConfiguration");
        
        ProvidedService internalService = new InternalReceiveRequest();
        Service required = new ReceiveRequest(); 
        
        addRequiredConnection(receiveRequest, required);
        addProvidedConnection(externalConfiguration, internalService);
    }
}
