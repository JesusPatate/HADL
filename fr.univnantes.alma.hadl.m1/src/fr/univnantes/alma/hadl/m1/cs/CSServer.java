package fr.univnantes.alma.hadl.m1.cs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.univnantes.alma.hadl.m1.serverDetails.DBRequest;
import fr.univnantes.alma.hadl.m1.serverDetails.DBResponse;
import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.configuration.ComponentConfiguration;
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
			Response resp = null;
			
			if (composite != null) {
				resp = new Response(null, false, String.format(
						"Request not handle by %s", CSServer.this.label));
			} else {
				List<Object> values = new LinkedList<Object>();
				values.add("Request");
				values.add("handled");
				values.add("by");
				values.add("server");
				DBResponse dbResp = new DBResponse(values);
				resp = new Response(dbResp);
			}
			
			return resp;
		}
	}

	public CSServer(final String label, ComponentConfiguration composite) {
		super(label, composite);
		Port receiveRequest = new Port("receiveRequest");
		ProvidedService provided = new ReceiveRequest();
		addProvidedConnection(receiveRequest, provided);
	}

	public CSServer(final String label) {
		this(label, null);
	}
}
