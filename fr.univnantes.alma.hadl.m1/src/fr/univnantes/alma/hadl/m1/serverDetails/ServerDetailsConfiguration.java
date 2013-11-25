package fr.univnantes.alma.hadl.m1.serverDetails;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m1.DBRequest;
import fr.univnantes.alma.hadl.m1.DBResponse;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.configuration.ComponentConfiguration;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;


public class ServerDetailsConfiguration extends ComponentConfiguration {
    
	private static final Map<String, Class<?>> PARAMETERS =
            new HashMap<String, Class<?>>();
    
    static {
        PARAMETERS.put("request", DBRequest.class);
    }
    
    private class ReceiveRequest  extends ProvidedService {
        
        public ReceiveRequest() {
            super("receiveRequest", DBResponse.class, PARAMETERS);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			return new Response(null);
		}
    }
	
    public ServerDetailsConfiguration(String label) {
        super(label);
        Port receiveRequest = new Port("receiveRequest");
        addProvidedConnection(receiveRequest, new ReceiveRequest());
    }
}
