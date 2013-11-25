package fr.univnantes.alma.hadl.m1.serverDetails;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;

public class SecurityManager extends AtomicComponent {

	private static final Map<String, Class<?>> SECURITY_AUTH_PARAMS = new HashMap<String, Class<?>>();

	private static final Map<String, Class<?>> SECURITY_MGMT_PARAMS = new HashMap<String, Class<?>>();

	static {
		SECURITY_AUTH_PARAMS.put("login", String.class);
		SECURITY_AUTH_PARAMS.put("password", String.class);

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

		public SecurityAuthorization() {
			super("securityAuthorization", boolean.class, SECURITY_AUTH_PARAMS);
		}

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Vérifier que le couple (login, password) est dans la table
			// des utilisateurs du serveur
			return new Response(true);
		}
	}

	private class SecurityManagement extends ProvidedService {

		public SecurityManagement() {
			super("securityManagement", boolean.class, SECURITY_MGMT_PARAMS);
		}

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Vérifier que le couple (login, password) est dans la table
			// des utilisateurs de la BD
			return new Response(true);
		}
	}

	public SecurityManager(String label) {
		super(label);
		Port securityAuthorization = new Port("securityAuthorization");
		Port credentialQuery = new Port("credentialQuery");

		addProvidedConnection(securityAuthorization,
				new SecurityAuthorization());
		addProvidedConnection(credentialQuery, new SecurityManagement());
	}
}
