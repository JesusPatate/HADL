package fr.univnantes.alma.hadl.m1.cs;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m1.DBRequest;
import fr.univnantes.alma.hadl.m1.DBResponse;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;


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
			/*Request req = new Request("internalReceiveRequest", parameters);
			
			System.out.println("DBG send request : " + req); // DBG
			
			List<Object> values = new ArrayList<Object>();
			values.add("pouet");
			Response resp = new Response(new DBResponse(values));
			
//			return send(req);
			return resp;*/
			return new Response(null, false);
		}
    }
    
    public CSServer(final String label){
        super(label);
        Port receiveRequest = new Port("receiveRequest");
        ProvidedService provided = new ReceiveRequest();
        
        addProvidedConnection(receiveRequest, provided);
    }
}
