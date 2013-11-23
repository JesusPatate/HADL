package fr.univnantes.alma.hadl.m1.cs;

import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.Service;


public class CSClient extends AtomicComponent {
    
    private class ReceiveRequestService extends Service {

		ReceiveRequestService() {
			// TODO: signature à compléter
			super("receiveRequest", null, null);
		}
    }
    
    public CSClient(final String label){        
        super(label);
        Port sendRequest = new Port("sendRequest");
        
        addRequiredConnection(sendRequest, new ReceiveRequestService());
    }
}
