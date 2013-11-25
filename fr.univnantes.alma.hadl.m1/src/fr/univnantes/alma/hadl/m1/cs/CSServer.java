package fr.univnantes.alma.hadl.m1.cs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univnantes.alma.hadl.m1.DBRequest;
import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;


public class CSServer extends AtomicComponent {
    
    private static final Map<String, Class<?>> PARAMETERS =
            new HashMap<String, Class<?>>();
    
    static {
        PARAMETERS.put("request", DBRequest.class);
    }
    
    private class ReceiveRequest extends ProvidedService {
        
        ReceiveRequest() {
            super("receiveRequest", DBResponse.class, PARAMETERS);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			Request req = new Request("internalReceiveRequest", parameters);
			
			System.out.println("DBG send request : " + req); // DBG
			
			List<String> values = new ArrayList<String>();
			values.add("pouet");
			Response resp = new Response(new DBResponse(values));
			
//			return send(req);
			return resp;
		}
    }
    
    private class InternalReceiveRequest  extends Service {
        
        public InternalReceiveRequest() {
            super("internalReceiveRequest", DBResponse.class, PARAMETERS);
        }
    }
    
    public CSServer(final String label){
        super(label);
        Port receiveRequest = new Port("receiveRequest");
        Port internalConfiguration = new Port("internalConfiguration");
        ProvidedService provided = new ReceiveRequest();
        Service internalService = new InternalReceiveRequest(); 
        
        addProvidedConnection(receiveRequest, provided);
        addRequiredConnection(internalConfiguration, internalService);
    }
}
