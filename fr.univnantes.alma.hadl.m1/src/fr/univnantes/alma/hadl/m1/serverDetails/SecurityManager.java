package fr.univnantes.alma.hadl.m1.serverDetails;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;

public class SecurityManager extends AtomicComponent {

	private static final Map<String, Class<?>> SECURITY_AUTHORIZATION_PARAMS = new HashMap<String, Class<?>>();

	private static final Map<String, Class<?>> SECURITY_MANAGEMENT_PARAMS = new HashMap<String, Class<?>>();

	static {
		SECURITY_AUTHORIZATION_PARAMS.put("login", String.class);
		SECURITY_AUTHORIZATION_PARAMS.put("password", String.class);
		SECURITY_MANAGEMENT_PARAMS.put("login", String.class);
		SECURITY_MANAGEMENT_PARAMS.put("password", String.class);

		// TODO Paramètres de security management
	}

	// TODO Tables utilisateurs du serveur et de la BD

	/**
	 * Teste si un utilisateur à le droit de se connecter au serveur.
	 * 
	 * <p>
	 * Signature du service : boolean securityAuthorization( String login,
	 * String password)
	 * </p>
	 */
	private class SecurityAuthorization extends ProvidedService {
		SecurityAuthorization() {
			super("securityAuthorization", boolean.class, SECURITY_AUTHORIZATION_PARAMS);
		}

		@Override
		public Response excecute(Map<String, Object> parameters) {
			String login = (String) parameters.get("login");
			String password = (String) parameters.get("password");
			boolean authorized = users.containsKey(login) && users.get(login).equals(password);
			return new Response(authorized);
		}
	}

	private class SecurityManagement extends ProvidedService {
		 SecurityManagement() {
			super("securityManagement", boolean.class, SECURITY_MANAGEMENT_PARAMS);
		}

		@Override
		public Response excecute(Map<String, Object> parameters) {
			String login = (String) parameters.get("login");
			String password = (String) parameters.get("password");
			boolean authorized = users.containsKey(login) && users.get(login).equals(password);
			return new Response(authorized);
		}
	}
	
	private Map<String, String> users = new HashMap<String, String>();

	public SecurityManager(String label) {
		super(label);
		Port securityAuthorization = new Port("securityAuthorization");
		Port credentialQuery = new Port("credentialQuery");
		addProvidedConnection(securityAuthorization,
				new SecurityAuthorization());
		addProvidedConnection(credentialQuery, new SecurityManagement());
		
		initializeUsers();
	}
	
	private void initializeUsers(){
		users.put("login", "pwd");
	}
}
