package fr.univnantes.alma.hadl.m1.cs;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m1.DBRequest;
import fr.univnantes.alma.hadl.m1.DBResponse;
import fr.univnantes.alma.hadl.m2.Request;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.Service;


public class CSClient extends AtomicComponent {
    
    private static final Map<String, Class<?>> RECEIVE_REQUEST_PARAMS =
            new HashMap<String, Class<?>>();
    
    static {
        RECEIVE_REQUEST_PARAMS.put("request", DBRequest.class);
    }
    
    /**
     * Required service receiveRequest
     */
    private class ReceiveRequestService extends Service {
        
		ReceiveRequestService() {
            super("receiveRequest", DBResponse.class, RECEIVE_REQUEST_PARAMS);
		}
    }
    
    public CSClient(final String label){        
        super(label);
        Port sendRequest = new Port("sendRequest");
        
        addRequiredConnection(sendRequest, new ReceiveRequestService());
    }
    
    public void send(final DBRequest dbReq) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("request", dbReq);
        Request req = new Request("receiveRequest", params);
        
        Response resp = super.send(req);
        
        DBResponse dbResp = (DBResponse) resp.getValue();
        
        System.out.println("Réponse de la BD : " + resp.getValue());
        //System.out.println("Réponse de la BD : " + dbResp.getValues());
    }
}
