package fr.univnantes.alma.hadl.m1.cs;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m1.serverDetails.DBRequest;
import fr.univnantes.alma.hadl.m1.serverDetails.DBResponse;
import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Role;
import fr.univnantes.alma.hadl.m2.service.IncompatibleServiceException;
import fr.univnantes.alma.hadl.m2.service.NotConnectedServiceException;
import fr.univnantes.alma.hadl.m2.service.Service;

public class CSRPC extends AtomicConnector {

	private static final Map<String, Class<?>> RECEIVE_REQUEST_PARAMS = new HashMap<String, Class<?>>();

	static {
		RECEIVE_REQUEST_PARAMS.put("request", DBRequest.class);
	}

	private class ReceiveRequestService extends Service {

		ReceiveRequestService() {
			super("receiveRequest", DBResponse.class, RECEIVE_REQUEST_PARAMS);
		}
	}

	public CSRPC(String label) {
		super(label);
		Role callerRole = new Role("caller");
		Role calleeRole = new Role("callee");
		ReceiveRequestService provided = new ReceiveRequestService();
		ReceiveRequestService required = new ReceiveRequestService();
		addProvidedService(calleeRole, provided);
		try {
			addRequiredService(callerRole, required, provided);
		} catch (NotConnectedServiceException e) {
			e.printStackTrace();
		} catch (IncompatibleServiceException e) {
			e.printStackTrace();
		}
	}
}
