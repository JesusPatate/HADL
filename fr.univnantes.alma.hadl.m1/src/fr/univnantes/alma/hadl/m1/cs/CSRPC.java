package fr.univnantes.alma.hadl.m1.cs;

import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Role;
import fr.univnantes.alma.hadl.m2.service.IncompatibleServiceException;
import fr.univnantes.alma.hadl.m2.service.NotConnectedServiceException;
import fr.univnantes.alma.hadl.m2.service.Service;


public class CSRPC extends AtomicConnector {
	private class ReceiveRequestService extends Service{
		ReceiveRequestService(){
			// TODO: signature à compléter
			super("receiveRequest", null, null);
		}
	}
	
	
    public CSRPC(String label) {
        super(label);
        Role callerRole = new Role("caller");
        Role calleeRole = new Role("callee");
        ReceiveRequestService provided = new ReceiveRequestService();
        ReceiveRequestService required = new ReceiveRequestService();
        
        addRequiredService(calleeRole, required);
        try {
			addProvidedService(callerRole, provided, required);
		} catch (NotConnectedServiceException e) {
			e.printStackTrace();
		} catch (IncompatibleServiceException e) {
			e.printStackTrace();
		}
    }
}
