package fr.univnantes.alma.hadl.m1.cs;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m1.serverDetails.DBRequest;
import fr.univnantes.alma.hadl.m1.serverDetails.DBResponse;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;

public class CSServer extends AtomicComponent {

	private static final Map<String, Class<?>> PARAMETERS = new HashMap<String, Class<?>>();

	static {
		PARAMETERS.put("request", DBRequest.class);
	}

	private class ReceiveRequest extends ProvidedService {

		ReceiveRequest() {
			super("receiveRequest", DBResponse.class, PARAMETERS);
		}

		@Override
		public Response excecute(Map<String, Object> parameters) {
			return new Response(null, false, String.format("Request not handle by %s", CSServer.this.label));
		}
	}

	public CSServer(final String label) {
		super(label);
		Port receiveRequest = new Port("receiveRequest");
		ProvidedService provided = new ReceiveRequest();
		addProvidedConnection(receiveRequest, provided);
	}
}
