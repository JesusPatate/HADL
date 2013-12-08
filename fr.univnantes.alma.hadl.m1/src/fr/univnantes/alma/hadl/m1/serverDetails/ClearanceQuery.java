package fr.univnantes.alma.hadl.m1.serverDetails;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Role;
import fr.univnantes.alma.hadl.m2.service.IncompatibleServiceException;
import fr.univnantes.alma.hadl.m2.service.NotConnectedServiceException;
import fr.univnantes.alma.hadl.m2.service.Service;

public class ClearanceQuery extends AtomicConnector {

	private static final Map<String, Class<?>> SECURITY_AUTHORIZATION_PARAMS = new HashMap<String, Class<?>>();

	static {
		SECURITY_AUTHORIZATION_PARAMS.put("login", String.class);
		SECURITY_AUTHORIZATION_PARAMS.put("password", String.class);
	}

	private class SecurityAuthorization extends Service {
		SecurityAuthorization() {
			super("securityAuthorization", boolean.class,
					SECURITY_AUTHORIZATION_PARAMS);
		}
	}

	public ClearanceQuery(String label) {
		super(label);
		Role requestor = new Role("requestor");
		Role grantor = new Role("grantor");
		SecurityAuthorization required = new SecurityAuthorization();
		SecurityAuthorization provided = new SecurityAuthorization();

		addProvidedService(grantor, provided);
		try {
			addRequiredService(requestor, required, provided);
		} catch (NotConnectedServiceException e) {
			e.printStackTrace();
		} catch (IncompatibleServiceException e) {
			e.printStackTrace();
		}
	}
}
