package fr.univnantes.alma.hadl.m1;

import fr.univnantes.alma.hadl.m1.cs.CSClient;
import fr.univnantes.alma.hadl.m1.cs.CSConfiguration;
import fr.univnantes.alma.hadl.m1.cs.CSRPC;
import fr.univnantes.alma.hadl.m1.cs.CSServer;
import fr.univnantes.alma.hadl.m1.serverDetails.ClearanceQuery;
import fr.univnantes.alma.hadl.m1.serverDetails.ConnectionManager;
import fr.univnantes.alma.hadl.m1.serverDetails.DBRequest;
import fr.univnantes.alma.hadl.m1.serverDetails.Database;
import fr.univnantes.alma.hadl.m1.serverDetails.SQLQuery;
import fr.univnantes.alma.hadl.m1.serverDetails.SecurityManager;
import fr.univnantes.alma.hadl.m1.serverDetails.SecurityQuery;
import fr.univnantes.alma.hadl.m1.serverDetails.ServerDetailsConfiguration;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.configuration.IllegalLinkException;
import fr.univnantes.alma.hadl.m2.connector.Role;

public class Main {
	private final static CSClient client = new CSClient("client");
	private static ServerDetailsConfiguration serverDetails = new ServerDetailsConfiguration(
			"serverDetails");
	private final static CSServer server = new CSServer("server", serverDetails);
	private final static CSRPC rpc = new CSRPC("RPC");
	private final static CSConfiguration cs = new CSConfiguration("cs");

	private final static ConnectionManager connectionManager = new ConnectionManager(
			"connectionManager");
	private final static ClearanceQuery clearanceQuery = new ClearanceQuery(
			"clearanceQuery");
	private final static SecurityManager securityManager = new SecurityManager(
			"securityManager");
	private final static SQLQuery sqlQuery = new SQLQuery("sqlQuery");
	private final static Database database = new Database("database");
	private final static SecurityQuery securityQuery = new SecurityQuery(
			"securityQuery");
	

	public static void main(String[] args) throws IllegalLinkException {
		buildServerDetails();
		buildCS();
		DBRequest req = new DBRequest("key", "login", "pwd");
		client.send(req);
	}

	private static void buildServerDetails() throws IllegalLinkException {
		serverDetails.addComponent(connectionManager);
		serverDetails.addConnector(clearanceQuery);
		serverDetails.addComponent(securityManager);
		serverDetails.addConnector(sqlQuery);
		serverDetails.addComponent(database);
		serverDetails.addConnector(securityQuery);

		// Clearance query
		Port senderPort = connectionManager
				.getRequestingPort("securityAuthorization");
		Role senderRole = clearanceQuery
				.getRequestingRole("securityAuthorization");
		serverDetails.addAttachment(senderPort, senderRole);

		Port receiverPort = securityManager
				.getProvidingPort("securityAuthorization");
		Role receiverRole = clearanceQuery
				.getProvidingRole("securityAuthorization");
		serverDetails.addAttachment(receiverPort, receiverRole);

		// Security query
		senderPort = database.getRequestingPort("securityManagement");
		senderRole = securityQuery.getRequestingRole("securityManagement");
		serverDetails.addAttachment(senderPort, senderRole);

		receiverPort = securityManager.getProvidingPort("securityManagement");
		receiverRole = securityQuery.getProvidingRole("securityManagement");
		serverDetails.addAttachment(receiverPort, receiverRole);

		// SQL query
		senderPort = connectionManager.getRequestingPort("handleQuery");
		senderRole = sqlQuery.getRequestingRole("handleQuery");
		serverDetails.addAttachment(senderPort, senderRole);

		receiverPort = database.getProvidingPort("handleQuery");
		receiverRole = sqlQuery.getProvidingRole("handleQuery");
		serverDetails.addAttachment(receiverPort, receiverRole);

		// Binding
		Port configPort = serverDetails.getProvidingPort("receiveRequest");
		Port conMgrPort = connectionManager.getProvidingPort("receiveRequest");
		serverDetails.addBinding(configPort, conMgrPort);
	}

	private static void buildCS() throws IllegalLinkException {
		cs.addComponent(client);
		cs.addConnector(rpc);
		cs.addComponent(server);
		cs.addComponent(serverDetails);

		// Request
		Port reqPort = client.getRequestingPort("receiveRequest");
		Role caller = rpc.getRequestingRole("receiveRequest");
		cs.addAttachment(reqPort, caller);

		Port proPort = server.getProvidingPort("receiveRequest");
		Role callee = rpc.getProvidingRole("receiveRequest");
		cs.addAttachment(proPort, callee);

		// Binding
		Port compositePort = serverDetails.getProvidingPort("receiveRequest");
		Port componentPort = server.getProvidingPort("receiveRequest");
		cs.addBinding(compositePort, componentPort);
	}
}
