package fr.univnantes.alma.hadl.m1.serverDetails;

import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Role;
import fr.univnantes.alma.hadl.m2.service.IncompatibleServiceException;
import fr.univnantes.alma.hadl.m2.service.NotConnectedServiceException;
import fr.univnantes.alma.hadl.m2.service.Service;


public class ClearanceRequest extends AtomicConnector {
	private class ClearanceRequestService extends Service {
		ClearanceRequestService() {
			// TODO: signature à compléter
			super("clearanceRequest", null, null);
		}
    }
	
    public ClearanceRequest(String label) {
        super(label);
        Role requestor = new Role("requestor");
        Role grantor = new Role("grantor");
        ClearanceRequestService required = new ClearanceRequestService();
        ClearanceRequestService provided = new ClearanceRequestService();
        
        addRequiredService(grantor, required);
        try {
			addProvidedService(requestor, provided, required);
		} catch (NotConnectedServiceException e) {
			e.printStackTrace();
		} catch (IncompatibleServiceException e) {
			e.printStackTrace();
		}
    }
}
