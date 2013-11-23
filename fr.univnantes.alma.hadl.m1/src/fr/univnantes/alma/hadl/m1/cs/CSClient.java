package fr.univnantes.alma.hadl.m1.cs;

import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;


public class CSClient extends AtomicComponent {
    
    private class ReceiveRequestService extends Service {

		ReceiveRequestService() {
			super("receiveRequest", null, null);
		}
    }
    
    private class ReceiveResponseService extends ProvidedService {
        
        public ReceiveResponseService() {
            super("receiveResponse", null, null);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    private class SendRequestService extends ProvidedService {
        
        public SendRequestService() {
        	super("sendRequest", null, null);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    public CSClient(final String label){        
        super(label);
        Port receiveRequestPort = new Port("receiveRequest");
        Port sendRequestPort = new Port("sendRequest");
        Port receiveResponsePort = new Port("receiveResponse");
        
        addRequiredConnection(receiveRequestPort, new ReceiveRequestService());
        addProvidedConnection(sendRequestPort, new SendRequestService());
        addProvidedConnection(receiveResponsePort, new ReceiveResponseService());
    }
}
