package fr.univnantes.alma.hadl.m1.cs;

import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;


public class CSServer extends AtomicComponent {
    private class ReceiveRequestService extends ProvidedService {
        
        ReceiveRequestService() {
        	// TODO: signature à compléter
            super("receiveRequest", null, null);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    // TODO: comment faire la jonction avec le binding ?
    public CSServer(final String label){
        super(label);
        Port receiveRequest = new Port("receiveRequest");
        ProvidedService provided = new ReceiveRequestService();
        
        addProvidedConnection(receiveRequest, provided);
    }
}
