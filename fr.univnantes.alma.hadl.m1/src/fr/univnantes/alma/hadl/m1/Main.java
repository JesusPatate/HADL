package fr.univnantes.alma.hadl.m1;

import fr.univnantes.alma.hadl.m1.cs.CSClient;
import fr.univnantes.alma.hadl.m1.cs.CSConfiguration;
import fr.univnantes.alma.hadl.m1.cs.CSRPC;
import fr.univnantes.alma.hadl.m1.cs.CSServer;
import fr.univnantes.alma.hadl.m1.serverDetails.ClearanceQuery;
import fr.univnantes.alma.hadl.m1.serverDetails.ConnectionManager;
import fr.univnantes.alma.hadl.m1.serverDetails.Database;
import fr.univnantes.alma.hadl.m1.serverDetails.SQLQuery;
import fr.univnantes.alma.hadl.m1.serverDetails.SecurityManager;
import fr.univnantes.alma.hadl.m1.serverDetails.SecurityQuery;
import fr.univnantes.alma.hadl.m1.serverDetails.ServerDetailsConfiguration;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.configuration.IllegalAttachmentException;
import fr.univnantes.alma.hadl.m2.connector.Role;


public class Main {
    private final static CSClient client = new CSClient("client");
    private final static CSServer server = new CSServer("server");
    private final static CSRPC rpc = new CSRPC("RPC");
    private final static CSConfiguration cs = new CSConfiguration("cs");
    
    private final static ConnectionManager connectionManager = new ConnectionManager("connectionManager");
    private final static ClearanceQuery clearanceQuery = new ClearanceQuery("clearanceQuery");
    private final static SecurityManager securityManager = new SecurityManager("securityManager");
    private final static SQLQuery sqlQuery = new SQLQuery("sqlQuery");
    private final static Database database = new Database("database");
    private final static SecurityQuery securityQuery = new SecurityQuery("securityQuery");
    private static ServerDetailsConfiguration serverDetails =
            new ServerDetailsConfiguration("serverDetails");
    
    public static void main(String[] args) throws IllegalAttachmentException {
        buildServerDetails();
        buildCS();
        
        DBRequest req = new DBRequest("meuh", "login", "pwd");
        client.send(req);
    }
    
    private static void buildServerDetails() throws IllegalAttachmentException {
        serverDetails.addComponent(connectionManager);
        serverDetails.addConnector(clearanceQuery);
        serverDetails.addComponent(securityManager);
        serverDetails.addConnector(sqlQuery);
        serverDetails.addComponent(database);
        serverDetails.addConnector(securityQuery);
        
        // Clearance query
        Port senderPort = connectionManager.getRequestingPort(
        		"securityAuthorization");
        Role senderRole = clearanceQuery.getRequestingRole(
        		"securityAuthorization");
        serverDetails.addAttachment(senderPort, connectionManager, senderRole,
                clearanceQuery);
        
        Port receiverPort = securityManager.getProvidingPort(
        		"securityAuthorization");
        Role receiverRole = clearanceQuery.getProvidingRole(
        		"securityAuthorization");
        serverDetails.addAttachment(receiverPort, securityManager, receiverRole,
                clearanceQuery);
        
        // Security query
        senderPort = database.getRequestingPort("securityManagement");
        senderRole = securityQuery.getRequestingRole("securityManagement");        		
        serverDetails.addAttachment(senderPort, database, senderRole,
                securityQuery);
        
        receiverPort = securityManager.getProvidingPort("securityManagement");
        receiverRole = securityQuery.getProvidingRole("securityManagement");
        serverDetails.addAttachment(receiverPort, securityManager, receiverRole,
                securityQuery);
        
        // SQL query
        senderPort = connectionManager.getRequestingPort("handleQuery");
        senderRole = sqlQuery.getRequestingRole("handleQuery");        		
        serverDetails.addAttachment(senderPort, connectionManager, senderRole,
                sqlQuery);
        
        receiverPort = database.getProvidingPort("handleQuery");
        receiverRole = sqlQuery.getProvidingRole("handleQuery");
        serverDetails.addAttachment(receiverPort, database, receiverRole,
        		sqlQuery);
        
        // Binding
        Port configPort = serverDetails.getProvidingPort("receiveRequest");
        Port conMgrPort = connectionManager.getProvidingPort("receiveRequest");
        serverDetails.addBinding(configPort, conMgrPort);
    }
    
    private static void buildCS() throws IllegalAttachmentException {
        cs.addComponent(client);
        cs.addConnector(rpc);
        cs.addComponent(server);
        
        // Request
        Port reqPort = client.getRequestingPort("receiveRequest");
        Role caller = rpc.getRequestingRole("receiveRequest");
        cs.addAttachment(reqPort, client, caller, rpc);
        
        Port proPort = server.getProvidingPort("receiveRequest");
        Role callee = rpc.getProvidingRole("receiveRequest");
        cs.addAttachment(proPort, server, callee, rpc);
        
        // Binding
        Port compositePort = serverDetails.getProvidingPort("receiveRequest");
        Port componentPort = server.getProvidingPort("receiveRequest");
        cs.addBinding(compositePort, componentPort);
    }
}
