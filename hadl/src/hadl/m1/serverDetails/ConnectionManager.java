package hadl.m1.serverDetails;

import hadl.m1.CSMessage;
import hadl.m1.RPCCall;
import hadl.m2.Call;
import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.Port;
import hadl.m2.component.ProvidedService;
import hadl.m2.component.RequiredService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionManager extends AtomicComponent {

	/**
	 * Provided service that receives client queries.
	 */
	class ReceiveRequest extends ProvidedService {

		public ReceiveRequest() {
			super("receiveRequest");
		}

		@Override
		public void receive(final Message msg) {
			boolean wrongParam = false;

			if (msg.getHeader().equals("CALL")) {
				Call call = (Call) msg;

				if (call.getCalledService().equals(this.label)) {
					// TODO Vérifier les paramètres

					if (wrongParam) {
						// TODO Gérer appel incorrect
					}

					System.out
							.println("Le gestionnaire de connection reçoit : "
									+ call); // DBG

					CSMessage query = (CSMessage) call.getParameters().get(0);

					String msgType = (String) query.getElement(
							CSMessage.Part.HEADER, "type");

					String queryID = (String) query.getElement(
							CSMessage.Part.HEADER, "id");

					if (msgType.equals("QUERY")) {
						ConnectionManager.this.queryBuffer.put(queryID, query);
						ConnectionManager.this.checkSecurity(query);
					}
				} else {
					// TODO Gérer appel incorrect
				}
			}
		}
	}

	/**
	 * Provided service that receives database responses to client queries.
	 */
	class ReceiveDBResponse extends ProvidedService {

		public ReceiveDBResponse() {
			super("receiveDBResponse");
		}

		@Override
		public void receive(final Message msg) {
			boolean wrongParam = false;

			if (msg.getHeader().equals("CALL")) {
				Call call = (Call) msg;

				if (call.getCalledService().equals(this.label)) {
					// TODO Vérifier les paramètres

					if (wrongParam) {
						// TODO Gérer appel incorrect
					}

					System.out
							.println("Le gestionnaire de connection reçoit : "
									+ call); // DBG

					CSMessage query = (CSMessage) call.getParameters().get(0);

					String msgType = (String) query.getElement(
							CSMessage.Part.HEADER, "type");

					if (msgType.equals("QUERYRESP")) {
						// TODO Renvoyer la réponse
					}
				} else {
					// TODO Gérer appel incorrect
				}
			}
		}
	}

	/**
	 * Provided service that receives security manager responses to clearance
	 * queries.
	 */
	class ReceiveAuthorization extends ProvidedService {

		public ReceiveAuthorization() {
			super("receiveAuthorization");
		}

		@Override
		public void receive(Message msg) {
			boolean wrongParam = false;

			if (msg.getHeader().equals("CALL")) {
				Call call = (Call) msg;

				if (call.getCalledService().equals(this.label)) {
					// TODO Vérifier les paramètres

					if (wrongParam) {
						// TODO Gérer appel incorrect
					}

					System.out.println("Le gestionnaire de connexion reçoit : "
							+ call); // DBG

					CSMessage response = (CSMessage) call.getParameters()
							.get(0);

					String msgType = (String) response.getElement(
							CSMessage.Part.HEADER, "type");

					if (msgType.equals("AUTHRESP")) {
						ConnectionManager.this.queryDatabase(response);
					}
				} else {
					// TODO Gérer appel incorrect
				}
			}
		}

	}

	/**
	 * Provided service that receives database responses to client queries.
	 */
	class ReceiveResponse extends RequiredService {

		public ReceiveResponse() {
			super("receiveResponse");
		}

		@Override
		public void receive(Message msg) {
		}
	}

	class HandleQuery extends RequiredService {

		public HandleQuery() {
			super("handleQuery");
		}

		@Override
		public void receive(final Message msg) {
		}
	}

	/**
	 * Required service that handles clearance queries.
	 */
	class SecurityAuthorization extends RequiredService {

		public SecurityAuthorization() {
			super("securityAuthorization");
		}

		@Override
		public void receive(final Message msg) {
		}
	}

	class ExternalSocket extends Port {

		public ExternalSocket(final ConnectionManager component) {
			super("externalSocket");
		}

		@Override
		public void receive(final Message msg) {
			Call call = (Call) msg;
			String service = call.getCalledService();

			if (this.linked(service)) {
				this.getLink(service).send(this, msg);
			}

			else {
				if (this.binding != null) {
					this.binding.send(this, msg);
				}
			}
		}
	}

	class DBQuery extends Port {

		public DBQuery(final ConnectionManager component) {
			super("DBQuery");
		}

		@Override
		public void receive(final Message msg) {
			Call call = (Call) msg;
			String service = call.getCalledService();

			if (this.linked(service)) {
				this.getLink(service).send(this, msg);
			}
		}
	}

	class SecurityQuery extends Port {

		public SecurityQuery(final ConnectionManager component) {
			super("securityQuery");
		}

		@Override
		public void receive(final Message msg) {
			Call call = (Call) msg;
			String service = call.getCalledService();

			if (this.linked(service)) {
				this.getLink(service).send(this, msg);
			}
		}
	}

	/**
	 * Client queries are stored in this buffer while waiting for security
	 * authorization.
	 */
	private final Map<String, CSMessage> queryBuffer = new HashMap<String, CSMessage>();

	/**
	 * Constructs a new connection manager.
	 * 
	 * @param label
	 *            Component name
	 */
	public ConnectionManager(final String label) {

		super(label);

		RequiredService securityAuth = new SecurityAuthorization();
		RequiredService handleQuery = new HandleQuery();
		ProvidedService receiveRequest = new ReceiveRequest();
		ProvidedService receiveAuth = new ReceiveAuthorization();
		ProvidedService reveiveDBresponse = new ReceiveDBResponse();

		Port externalSocket = new ExternalSocket(this);
		Port securityQuery = new SecurityQuery(this);
		Port dbQuery = new DBQuery(this);

		try {
			this.addProvidedService(receiveRequest);
			this.addPort(externalSocket);
			this.addConnection(receiveRequest.getLabel(), receiveRequest,
					externalSocket);

			this.addProvidedService(reveiveDBresponse);
			this.addPort(dbQuery);
			this.addConnection(reveiveDBresponse.getLabel(), reveiveDBresponse,
					dbQuery);

			this.addProvidedService(receiveAuth);
			this.addPort(securityQuery);
			this.addConnection(receiveAuth.getLabel(), receiveAuth,
					securityQuery);

			this.addRequiredService(securityAuth);
			this.addConnection(securityAuth.getLabel(), securityAuth,
					securityQuery);

			this.addRequiredService(handleQuery);
			this.addPort(dbQuery);
			this.addConnection(handleQuery.getLabel(), handleQuery, dbQuery);
		}
		catch (NoSuchPortException e) {
			// Couldn't be reachable
		}
		catch (NoSuchServiceException e) {
			// Couldn't be reachable
		}
	}

	private void checkSecurity(final CSMessage msg) {
		String id = (String) msg.getElement(CSMessage.Part.HEADER, "id");
		String login = (String) msg.getElement(CSMessage.Part.BODY, "login");
		String pwd = (String) msg.getElement(CSMessage.Part.BODY, "password");

		CSMessage queryMsg = new CSMessage(id);

		queryMsg.addHeaderElement("type", "AUTH");
		queryMsg.addBodyElement("login", login);
		queryMsg.addBodyElement("password", pwd);

		List<Object> args = new ArrayList<Object>();
		args.add(queryMsg);

		Call call = new RPCCall("securityAuthorization", args);

		System.out.println("Le gestionnaire de connection envoie : " + call); // DBG

		try {
			Port port = getRequestingPort("securityAuthorization");
			port.receive(call);
		}
		catch (NoSuchServiceException e) {
			// Couldn't be reachable
		}
	}

	private void queryDatabase(final CSMessage msg) {
		String id = (String) msg.getElement(CSMessage.Part.HEADER, "id");
		Boolean auth = (Boolean) msg.getElement(CSMessage.Part.BODY, "auth");

		if (auth) {
			CSMessage queryMsg = this.queryBuffer.get(id);
			String query = (String) queryMsg.getElement(CSMessage.Part.BODY,
					"query");

			if (queryMsg != null) {
				CSMessage response = new CSMessage(id);

				response.addHeaderElement("type", "QUERY");
				response.addBodyElement("query", query);

				List<Object> args = new ArrayList<Object>();
				args.add(response);

				Call call = new RPCCall("handleQuery", args);

				System.out.println("Le gestionnaire de connexion envoie : "
						+ call); // DBG

				try {
					Port port = getRequestingPort("handleQuery");
					port.receive(call);
				} catch (NoSuchServiceException e) {
					// Couldn't be reachable
				}
			}
		}

		else {
			// TODO Gérer refus du security mgr
		}
	}

	private void sendDBResponse(final CSMessage dbResponse) {
		String id = (String) dbResponse.getElement(CSMessage.Part.HEADER, "id");
		
		System.out.println("DBG id: " + id); // DBG
	}
}
