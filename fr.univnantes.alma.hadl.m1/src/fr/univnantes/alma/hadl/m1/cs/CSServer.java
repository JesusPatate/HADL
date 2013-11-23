package fr.univnantes.alma.hadl.m1.cs;

import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;


public class CSServer extends AtomicComponent {
    
    private class ReceiveResponseService extends Service {
        
        public ReceiveResponseService() {
            super("receiveResponse", null, null);
        }
    }
    
    private class ReceiveRequestService extends ProvidedService {
        
        public ReceiveRequestService() {
            super("receiveRequest", null, null);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    private class SendResponseService extends ProvidedService {
        
        public SendResponseService() {
            super("sendResponse", null, null);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    public CSServer(final String label){
        super(label);
        Port receiveRequestPort = new Port("receiveRequest");
        Port sendResponsePort = new Port("sendResponse");
        Port receiveResponsePort = new Port("receiveResponse");
        
        addRequiredConnection(receiveRequestPort, new ReceiveRequestService());
        addRequiredConnection(receiveResponsePort, new ReceiveResponseService());
        addProvidedConnection(sendResponsePort, new SendResponseService());
    }
}
